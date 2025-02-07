package com.robert.shipgame.auction.listing.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record AuctionListing(UUID id,
                             String name,
                             BigDecimal price,
                             Instant whenListed,
                             Instant whenExpires,
                             Instant whenSold) {
}
