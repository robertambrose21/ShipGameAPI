package com.robert.shipgame.auction.listing.data;

import com.robert.shipgame.account.data.AccountDAO;
import com.robert.shipgame.auction.bid.data.BidDAO;
import com.robert.shipgame.auction.sale.data.SaleDAO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity(name = "AuctionListing")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class AuctionListingDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    private String name;

    @Positive
    @NotNull
    private BigDecimal buyoutPrice;

    @Positive
    @NotNull
    private BigDecimal minimumBidPrice;

    @CreationTimestamp
    private Instant whenListed;

    private Instant whenExpired;

    private Instant whenFinalised;

    @OneToOne(cascade = CascadeType.ALL)
    private SaleDAO sale;

    @OneToMany(cascade = CascadeType.ALL)
    private List<BidDAO> bids;

    @ManyToOne
    private AccountDAO placedBy;

}
