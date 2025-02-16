package com.robert.shipgame.auction.bid.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BidRepository extends JpaRepository<BidDAO, UUID> {
}
