package com.robert.shipgame.auction.bid.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface BidRepository extends JpaRepository<BidDAO, UUID> {

    @Query("SELECT bid FROM Bid bid WHERE bid.auctionListing.id = :auctionId ORDER BY bid.price DESC LIMIT 1")
    Optional<BidDAO> findTopBidByAuctionId(@Param("auctionId") UUID auctionId);

}
