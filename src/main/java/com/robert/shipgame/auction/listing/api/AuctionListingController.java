package com.robert.shipgame.auction.listing.api;

import com.robert.shipgame.auction.bid.BidMapper;
import com.robert.shipgame.auction.bid.api.dto.BidDTO;
import com.robert.shipgame.auction.listing.AuctionListingMapper;
import com.robert.shipgame.auction.listing.api.dto.AuctionListingDTO;
import com.robert.shipgame.auction.listing.api.dto.CreateOrUpdateAuctionListingDTO;
import com.robert.shipgame.auction.listing.api.dto.PlaceBidDTO;
import com.robert.shipgame.auction.listing.api.dto.PurchaseAuctionListingDTO;
import com.robert.shipgame.auction.listing.exception.AuctionListingException;
import com.robert.shipgame.auction.listing.exception.AuctionListingNotFoundException;
import com.robert.shipgame.auction.listing.exception.AuctionListingPurchaseException;
import com.robert.shipgame.auction.listing.service.AuctionListingService;
import com.robert.shipgame.auction.sale.SaleMapper;
import com.robert.shipgame.auction.sale.api.SaleDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("auctions")
@RequiredArgsConstructor
@CrossOrigin
public class AuctionListingController {

    private final AuctionListingService auctionListingService;

    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping
    public List<AuctionListingDTO> getAllListings() {
        return auctionListingService.getAllListings().stream()
                .map(AuctionListingMapper.INSTANCE::pojoToDTO)
                .toList();
    }

    @GetMapping("/bids/{auctionListingId}")
    public List<BidDTO> getBidsForAuction(@PathVariable final UUID auctionListingId) {
        return auctionListingService.getBidsForAuction(auctionListingId).stream()
                .map(BidMapper.INSTANCE::pojoToDTO)
                .toList();
    }

    @PostMapping
    public AuctionListingDTO addListing(@RequestBody @Valid final CreateOrUpdateAuctionListingDTO listing) {
        return AuctionListingMapper.INSTANCE.pojoToDTO(auctionListingService.addListing(listing));
    }

    @PostMapping("/purchase/{auctionListingId}")
    public SaleDTO purchase(@PathVariable final UUID auctionListingId,
                            @RequestBody @Valid PurchaseAuctionListingDTO dto) {
        return SaleMapper.INSTANCE.pojoToDto(auctionListingService.purchase(auctionListingId, dto.price()));
    }

    @PostMapping("/bid/{auctionListingId}")
    public AuctionListingDTO placeBid(@PathVariable final UUID auctionListingId,
                                      @RequestBody @Valid PlaceBidDTO dto) {
        return AuctionListingMapper.INSTANCE.pojoToDTO(auctionListingService.placeBid(auctionListingId, dto.price()));
    }

    @ExceptionHandler(AuctionListingNotFoundException.class)
    public ResponseEntity<Object> handleAuctionListingNotFoundException(AuctionListingNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler(AuctionListingException.class)
    public ResponseEntity<Object> handleAuctionListingException(AuctionListingException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }

}
