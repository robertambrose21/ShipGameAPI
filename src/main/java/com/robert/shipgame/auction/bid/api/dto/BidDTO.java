package com.robert.shipgame.auction.bid.api.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record BidDTO(UUID id, BigDecimal price, Instant whenPlaced) {
}
