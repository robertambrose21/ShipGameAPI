package com.robert.shipgame.auction.listing.service;

import com.robert.shipgame.auction.bid.service.Bid;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record AuctionListing(UUID id,
                             UUID placedBy,
                             String name,
                             BigDecimal buyoutPrice,
                             BigDecimal minimumBidPrice,
                             List<Bid> bids,
                             Instant whenListed,
                             Instant whenExpired,
                             Instant whenFinalised,
                             Instant whenSold) {
}
