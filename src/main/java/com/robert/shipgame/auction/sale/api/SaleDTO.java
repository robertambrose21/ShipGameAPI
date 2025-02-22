package com.robert.shipgame.auction.sale.api;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record SaleDTO(UUID id, UUID auctionListingId, Instant whenSold, BigDecimal price, UUID purchaserId) {
}
