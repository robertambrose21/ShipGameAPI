package com.robert.shipgame.auction.listing.api.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
public record AuctionListingDTO(UUID id,
                                UUID placedBy,
                                String name,
                                BigDecimal price,
                                BigDecimal highestBid,
                                Instant whenListed,
                                Instant whenExpires,
                                Instant whenSold) {
}
