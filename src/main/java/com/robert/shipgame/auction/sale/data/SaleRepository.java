package com.robert.shipgame.auction.sale.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SaleRepository extends JpaRepository<SaleDAO, UUID> {
}
