package com.robert.shipgame.auction.listing;

import com.robert.shipgame.auction.bid.BidMapper;
import com.robert.shipgame.auction.bid.service.Bid;
import com.robert.shipgame.auction.listing.api.dto.AuctionListingDTO;
import com.robert.shipgame.auction.listing.data.AuctionListingDAO;
import com.robert.shipgame.auction.listing.service.AuctionListing;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Mapper(uses = BidMapper.class)
public interface AuctionListingMapper {

    AuctionListingMapper INSTANCE = Mappers.getMapper(AuctionListingMapper.class);

    @Mapping(source = "bids", target = "highestBid", qualifiedByName = "mapHighestBid")
    AuctionListingDTO pojoToDTO(AuctionListing auctionListing);

    @Mapping(source = "placedBy.id", target = "placedBy")
    @Mapping(source = "sale.whenSold", target = "whenSold")
    AuctionListing daoToPojo(AuctionListingDAO auctionListingDAO);

    @Named("mapHighestBid")
    default BigDecimal mapHighestBid(final List<Bid> bids) {
        if (bids == null || bids.isEmpty()) {
            return null;
        }

        return bids.stream()
                .max(Comparator.comparing(Bid::price))
                .map(Bid::price)
                .orElse(null);
    }

}
