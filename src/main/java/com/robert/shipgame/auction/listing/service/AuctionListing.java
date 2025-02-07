package com.robert.shipgame.auction.listing.service;

import com.robert.shipgame.auction.bid.service.Bid;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record AuctionListing(UUID id,
                             String name,
                             BigDecimal price,
                             List<Bid> bids,
                             Instant whenListed,
                             Instant whenExpires,
                             Instant whenSold) {
}
