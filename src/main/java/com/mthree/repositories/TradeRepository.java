package com.mthree.repositories;

import com.mthree.models.Trade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Integer> {
    
  

}
