package com.robert.shipgame.auction.sale.data;

import com.robert.shipgame.auction.listing.data.AuctionListingDAO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity(name = "Sale")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    private Instant whenSold;

    @OneToOne
    private AuctionListingDAO auctionListing;

}
