package com.robert.shipgame.auction.sale.service;

import com.robert.shipgame.account.service.Account;
import com.robert.shipgame.auction.listing.service.AuctionListing;

import java.time.Instant;
import java.util.UUID;

public record Sale(UUID id, Instant whenSold, AuctionListing auctionListing, Account purchaser) {
}
