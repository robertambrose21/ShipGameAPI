package com.robert.shipgame.auction.listing.api.dto;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PurchaseAuctionListingDTO(@Positive BigDecimal price) {
}
