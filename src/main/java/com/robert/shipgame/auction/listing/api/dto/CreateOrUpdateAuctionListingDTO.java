package com.robert.shipgame.auction.listing.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.Instant;

public record CreateOrUpdateAuctionListingDTO(@NotNull String name,
                                              @Positive BigDecimal price,
                                              Instant whenExpires) {
}
