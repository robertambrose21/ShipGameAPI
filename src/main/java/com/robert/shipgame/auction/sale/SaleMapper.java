package com.robert.shipgame.auction.sale;

import com.robert.shipgame.account.AccountMapper;
import com.robert.shipgame.auction.listing.AuctionListingMapper;
import com.robert.shipgame.auction.sale.api.SaleDTO;
import com.robert.shipgame.auction.sale.data.SaleDAO;
import com.robert.shipgame.auction.sale.service.Sale;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = { AuctionListingMapper.class, AccountMapper.class })
public interface SaleMapper {

    SaleMapper INSTANCE = Mappers.getMapper(SaleMapper.class);

    @Mapping(source = "auctionListing.id", target = "auctionListingId")
    @Mapping(source = "auctionListing.buyoutPrice", target = "price")
    @Mapping(source = "purchaser.id", target = "purchaserId")
    SaleDTO pojoToDto(Sale sale);

    Sale daoToPojo(SaleDAO saleDAO);

}
