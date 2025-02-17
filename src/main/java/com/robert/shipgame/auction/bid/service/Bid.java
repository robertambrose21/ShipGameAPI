package com.robert.shipgame.auction.bid.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record Bid(UUID id,  UUID placedBy, BigDecimal price, Instant whenPlaced) {
}