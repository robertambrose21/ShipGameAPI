package com.robert.shipgame.auction.listing.service;

import com.robert.shipgame.account.AccountMapper;
import com.robert.shipgame.account.data.AccountDAO;
import com.robert.shipgame.account.service.Account;
import com.robert.shipgame.account.service.AccountService;
import com.robert.shipgame.auction.bid.data.BidDAO;
import com.robert.shipgame.auction.bid.service.Bid;
import com.robert.shipgame.auction.listing.AuctionListingMapper;
import com.robert.shipgame.auction.listing.api.dto.CreateOrUpdateAuctionListingDTO;
import com.robert.shipgame.auction.listing.data.AuctionListingDAO;
import com.robert.shipgame.auction.listing.data.AuctionListingRepository;
import com.robert.shipgame.auction.listing.exception.AuctionListingException;
import com.robert.shipgame.auction.listing.exception.AuctionListingNotFoundException;
import com.robert.shipgame.auction.listing.exception.AuctionListingPurchaseException;
import com.robert.shipgame.auction.sale.SaleMapper;
import com.robert.shipgame.auction.sale.data.SaleDAO;
import com.robert.shipgame.auction.sale.service.Sale;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuctionListingService {

    private final AuctionListingRepository auctionListingRepository;
    private final AccountService accountService;

    public List<AuctionListing> getAllListings() {
        return auctionListingRepository.findAll().stream()
                .map(AuctionListingMapper.INSTANCE::daoToPojo)
                .toList();
    }

    public AuctionListing addListing(final CreateOrUpdateAuctionListingDTO dto) {
        final AccountDAO placedBy = accountService.getLoggedInAccount()
                .map(AccountMapper.INSTANCE::pojoToDAO)
                .orElseThrow(() -> new AuctionListingException("Cannot create an auction listing without a logged in user"));

        if(dto.minimumBid() == null) {
            throw new AuctionListingException("Cannot create an auction without a minimum bid");
        }

        if(dto.whenExpires().compareTo(Instant.now()) <= 0) {
            throw new AuctionListingException("Auction cannot expire before it's creation date");
        }

        final AuctionListingDAO listing = AuctionListingDAO.builder()
                .placedBy(placedBy)
                .name(dto.name())
                .buyoutPrice(dto.price())
                .whenExpired(dto.whenExpires())
                .build();

        return AuctionListingMapper.INSTANCE.daoToPojo(auctionListingRepository.save(listing));
    }

    public Optional<AuctionListing> getListing(final UUID id) {
        return auctionListingRepository.findById(id).map(AuctionListingMapper.INSTANCE::daoToPojo);
    }

    public List<Bid> getBidsForAuction(final UUID auctionListingId) {
        final AuctionListing listing = getListing(auctionListingId).orElseThrow(() ->
                new AuctionListingNotFoundException("Auction listing with id " + auctionListingId + " does not exist."));

        listing.bids().sort(Comparator.comparing(Bid::price).reversed());

        return listing.bids();
    }

    @Transactional
    public AuctionListing placeBid(final UUID auctionListingId, final BigDecimal price, final Account purchaser) {
        final AuctionListingDAO listing = auctionListingRepository.findById(auctionListingId).orElseThrow(() ->
                new AuctionListingNotFoundException("Auction listing with id " + auctionListingId + " does not exist."));

        if(listing.getWhenExpired().compareTo(Instant.now()) <= 0) {
            throw new AuctionListingPurchaseException("Cannot place bid on expired auction");
        }

        if(listing.getSale() != null) {
            throw new AuctionListingPurchaseException("Cannot place bid on auction, auction already sold");
        }

        if(listing.getWhenFinalised() != null) {
            throw new AuctionListingPurchaseException("Auction has already been finalised, cannot place bid");
        }

        if(listing.getPlacedBy().getId() == purchaser.id()) {
            throw new AuctionListingPurchaseException("Cannot place a bid on own auction");
        }

        if(price.compareTo(listing.getMinimumBidPrice()) < 0) {
            throw new AuctionListingPurchaseException("Cannot placed bid of " +
                    price +
                    ", below minimum bid price of " +
                    listing.getMinimumBidPrice());
        }

        final BigDecimal highestBid = listing.getBids().stream()
                .max(Comparator.comparing(BidDAO::getPrice))
                .map(BidDAO::getPrice)
                .orElse(BigDecimal.ZERO);

        if(price.compareTo(highestBid) <= 0) {
            throw new AuctionListingPurchaseException("Bid does not exceed current highest bid of " + highestBid);
        }

        listing.getBids().add(
                BidDAO.builder()
                        .price(price)
                        .whenPlaced(Instant.now())
                        .placedBy(AccountMapper.INSTANCE.pojoToDAO(purchaser))
                        .build());

        return AuctionListingMapper.INSTANCE.daoToPojo(auctionListingRepository.save(listing));
    }

    @Transactional
    public Sale purchase(final UUID auctionListingId, final BigDecimal price, final Account purchaser) {
        final AuctionListingDAO listing = auctionListingRepository.findById(auctionListingId).orElseThrow(() ->
                new AuctionListingNotFoundException("Auction listing with id " + auctionListingId + " does not exist."));

        if(listing.getWhenExpired().compareTo(Instant.now()) <= 0) {
            throw new AuctionListingPurchaseException("Cannot purchase expired auction");
        }

        if(listing.getSale() != null) {
            throw new AuctionListingPurchaseException("Cannot purchase auction, auction already sold");
        }

        if(listing.getWhenFinalised() != null) {
            throw new AuctionListingPurchaseException("Auction has already been finalised, cannot purchase");
        }

        if(price.compareTo(listing.getBuyoutPrice()) < 0) {
            throw new AuctionListingPurchaseException("Purchase buyoutPrice does not match auction buyoutPrice of " + listing.getBuyoutPrice());
        }

        listing.setBuyoutPrice(price);

        final SaleDAO sale = SaleDAO.builder()
                .auctionListing(listing)
                .whenSold(Instant.now())
                .purchaser(AccountMapper.INSTANCE.pojoToDAO(purchaser))
                .build();

        listing.setSale(sale);
        listing.setWhenFinalised(Instant.now());

        return SaleMapper.INSTANCE.daoToPojo(auctionListingRepository.save(listing).getSale());
    }

}
