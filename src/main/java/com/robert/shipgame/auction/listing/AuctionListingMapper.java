package com.robert.shipgame.auction.listing;

import com.robert.shipgame.auction.listing.api.dto.AuctionListingDTO;
import com.robert.shipgame.auction.listing.data.AuctionListingDAO;
import com.robert.shipgame.auction.listing.service.AuctionListing;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuctionListingMapper {

    AuctionListingMapper INSTANCE = Mappers.getMapper(AuctionListingMapper.class);

    AuctionListingDTO pojoToDTO(AuctionListing auctionListing);

    @Mapping(source = "sale.whenSold", target = "whenSold")
    AuctionListing daoToPojo(AuctionListingDAO auctionListingDAO);

}
