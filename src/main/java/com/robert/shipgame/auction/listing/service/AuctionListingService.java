package com.robert.shipgame.auction.listing.service;

import com.robert.shipgame.auction.bid.data.BidDAO;
import com.robert.shipgame.auction.bid.service.Bid;
import com.robert.shipgame.auction.listing.AuctionListingMapper;
import com.robert.shipgame.auction.listing.api.dto.CreateOrUpdateAuctionListingDTO;
import com.robert.shipgame.auction.listing.data.AuctionListingDAO;
import com.robert.shipgame.auction.listing.data.AuctionListingRepository;
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

    public List<AuctionListing> getAllListings() {
        return auctionListingRepository.findAll().stream()
                .map(AuctionListingMapper.INSTANCE::daoToPojo)
                .toList();
    }

    public AuctionListing addListing(final CreateOrUpdateAuctionListingDTO dto) {
        final AuctionListingDAO listing = AuctionListingDAO.builder()
                .name(dto.name())
                .price(dto.price())
                .whenExpires(dto.whenExpires())
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
    public AuctionListing placeBid(final UUID auctionListingId, final BigDecimal price) {
        final AuctionListingDAO listing = auctionListingRepository.findById(auctionListingId).orElseThrow(() ->
                new AuctionListingNotFoundException("Auction listing with id " + auctionListingId + " does not exist."));

        final BigDecimal highestBid = listing.getBids().stream()
                .max(Comparator.comparing(BidDAO::getPrice))
                .map(BidDAO::getPrice)
                .orElse(BigDecimal.ZERO);

        if(price.compareTo(highestBid) < 0) {
            throw new AuctionListingPurchaseException("Bid does not exceed current highest bid of " + highestBid);
        }

        listing.getBids().add(
                BidDAO.builder()
                        .price(price)
                        .whenPlaced(Instant.now())
                        .build());

        return AuctionListingMapper.INSTANCE.daoToPojo(auctionListingRepository.save(listing));
    }

    @Transactional
    public Sale purchase(final UUID auctionListingId, final BigDecimal price) {
        final AuctionListingDAO listing = auctionListingRepository.findById(auctionListingId).orElseThrow(() ->
                new AuctionListingNotFoundException("Auction listing with id " + auctionListingId + " does not exist."));

        if(price.compareTo(listing.getPrice()) < 0) {
            throw new AuctionListingPurchaseException("Purchase price does not match auction price of " + listing.getPrice());
        }

        listing.setPrice(price);

        final SaleDAO sale = SaleDAO.builder()
                .auctionListing(listing)
                .whenSold(Instant.now())
                .build();

        listing.setSale(sale);

        return SaleMapper.INSTANCE.daoToPojo(auctionListingRepository.save(listing).getSale());
    }

}
