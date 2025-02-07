package com.robert.shipgame.auction.sale.service;

import com.robert.shipgame.auction.sale.SaleMapper;
import com.robert.shipgame.auction.sale.data.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;

    public List<Sale> getAllSales() {
        return saleRepository.findAll().stream().
                map(SaleMapper.INSTANCE::daoToPojo)
                .toList();
    }

}
