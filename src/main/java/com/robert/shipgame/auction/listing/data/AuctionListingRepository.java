package com.robert.shipgame.auction.listing.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuctionListingRepository extends JpaRepository<AuctionListingDAO, UUID> {
}
