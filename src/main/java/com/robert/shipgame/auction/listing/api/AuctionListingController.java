package com.robert.shipgame.auction.listing.api;

import com.robert.shipgame.auction.listing.AuctionListingMapper;
import com.robert.shipgame.auction.listing.api.dto.AuctionListingDTO;
import com.robert.shipgame.auction.listing.api.dto.CreateOrUpdateAuctionListingDTO;
import com.robert.shipgame.auction.listing.exception.AuctionListingNotFoundException;
import com.robert.shipgame.auction.listing.service.AuctionListingService;
import com.robert.shipgame.auction.sale.SaleMapper;
import com.robert.shipgame.auction.sale.api.SaleDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("auctions")
@RequiredArgsConstructor
@CrossOrigin
public class AuctionListingController {

    private final AuctionListingService auctionListingService;

    @GetMapping
    public List<AuctionListingDTO> getAllListings() {
        return auctionListingService.getAllListings().stream()
                .map(AuctionListingMapper.INSTANCE::pojoToDTO)
                .toList();
    }

    @PostMapping
    public AuctionListingDTO addListing(@RequestBody @Valid final CreateOrUpdateAuctionListingDTO listing) {
        return AuctionListingMapper.INSTANCE.pojoToDTO(auctionListingService.addListing(listing));
    }

    @PostMapping("/purchase/{auctionListingId}")
    public SaleDTO purchase(@PathVariable final UUID auctionListingId) {
        return SaleMapper.INSTANCE.pojoToDto(auctionListingService.purchase(auctionListingId));
    }

    @ExceptionHandler(AuctionListingNotFoundException.class)
    public ResponseEntity<Object> handleAuctionListingException(AuctionListingNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

}
