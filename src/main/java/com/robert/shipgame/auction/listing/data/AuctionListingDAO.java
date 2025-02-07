package com.robert.shipgame.auction.listing.data;

import com.robert.shipgame.auction.sale.data.SaleDAO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
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

    private String name;

    @Positive
    private BigDecimal price;

    @CreationTimestamp
    private Instant whenListed;

    private Instant whenExpires;

    @OneToOne(cascade = CascadeType.ALL)
    private SaleDAO sale;

}
