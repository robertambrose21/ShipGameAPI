package com.robert.shipgame.auction.bid;

import com.robert.shipgame.auction.bid.api.dto.BidDTO;
import com.robert.shipgame.auction.bid.data.BidDAO;
import com.robert.shipgame.auction.bid.service.Bid;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BidMapper {

    BidMapper INSTANCE = Mappers.getMapper(BidMapper.class);

    BidDTO pojoToDTO(Bid bid);

    Bid daoToPojo(BidDAO dao);

}
