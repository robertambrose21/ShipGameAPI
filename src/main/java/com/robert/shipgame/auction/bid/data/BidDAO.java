package com.robert.shipgame.auction.bid.data;

import com.robert.shipgame.auction.listing.data.AuctionListingDAO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity(name = "Bid")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BidDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Positive
    private BigDecimal price;

    private Instant whenPlaced;

    @ManyToOne
    private AuctionListingDAO auctionListing;

}
