package com.robert.shipgame.auction.sale.api;

import com.robert.shipgame.auction.sale.SaleMapper;
import com.robert.shipgame.auction.sale.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("sales")
@RequiredArgsConstructor
@CrossOrigin
public class SaleController {

    private final SaleService saleService;

    @GetMapping
    public List<SaleDTO> getSales() {
        return saleService.getAllSales().stream()
                .map(SaleMapper.INSTANCE::pojoToDto)
                .toList();
    }

}
