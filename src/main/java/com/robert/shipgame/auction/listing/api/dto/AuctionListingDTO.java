package com.robert.shipgame.auction.listing.api.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
public record AuctionListingDTO(UUID id,
                                UUID placedBy,
                                String name,
                                BigDecimal buyoutPrice,
                                BigDecimal minimumBidPrice,
                                BigDecimal highestBid,
                                Instant whenListed,
                                Instant whenExpired,
                                Instant whenFinalised,
                                Instant whenSold) {
}
