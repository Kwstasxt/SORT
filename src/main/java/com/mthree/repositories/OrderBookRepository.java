package com.mthree.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mthree.models.OrderBook;

@Repository
public interface OrderBookRepository extends JpaRepository<OrderBook, Integer> { 

	public Optional<OrderBook> findById(int orderBookId);

	
}
