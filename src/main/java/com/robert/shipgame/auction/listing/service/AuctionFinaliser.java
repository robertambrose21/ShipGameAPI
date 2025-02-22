package com.robert.shipgame.auction.listing.service;

import com.robert.shipgame.account.AccountMapper;
import com.robert.shipgame.auction.bid.data.BidDAO;
import com.robert.shipgame.auction.bid.data.BidRepository;
import com.robert.shipgame.auction.listing.data.AuctionListingDAO;
import com.robert.shipgame.auction.listing.data.AuctionListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuctionFinaliser {

    private final AuctionListingRepository auctionListingRepository;
    private final BidRepository bidRepository;
    private final AuctionListingService auctionListingService;

    @Scheduled(cron = "0 0/15 * * * *")
    private void finalisePendingAuctions() {
        auctionListingRepository
                .findByWhenExpiredBeforeAndWhenFinalisedIsNull(Instant.now())
                .forEach(this::finaliseAuction);
    }

    private void finaliseAuction(final AuctionListingDAO auction) {
        final Optional<BidDAO> topBid = bidRepository.findTopBidByAuctionId(auction.getId());

        if(topBid.isEmpty()) {
            System.out.println("Auction expired");
        }
        else {
            auctionListingService.purchase(
                    auction.getId(),
                    topBid.get().getPrice(),
                    AccountMapper.INSTANCE.daoToPojo(topBid.get().getPlacedBy()));
        }

        auction.setWhenFinalised(Instant.now());
        auctionListingRepository.save(auction);
    }

}
