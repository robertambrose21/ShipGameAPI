package com.robert.shipgame.auction.listing.api.dto;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record PlaceBidDTO(UUID auctionListingId, @Positive BigDecimal price) {
}
