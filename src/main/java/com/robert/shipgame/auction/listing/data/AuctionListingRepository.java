package com.robert.shipgame.auction.listing.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface AuctionListingRepository extends JpaRepository<AuctionListingDAO, UUID> {

    List<AuctionListingDAO> findByWhenExpiredBeforeAndWhenFinalisedIsNull(Instant now);

}
