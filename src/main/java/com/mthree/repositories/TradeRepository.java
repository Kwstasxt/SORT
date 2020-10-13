package com.mthree.repositories;

import com.mthree.models.Trade;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Integer> {
    
}
