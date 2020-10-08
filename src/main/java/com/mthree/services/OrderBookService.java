package com.mthree.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mthree.daos.OrderBookDAO;
import com.mthree.daos.OrderBookRepository;
import com.mthree.models.Order;
import com.mthree.models.OrderBook;

@Service
public class OrderBookService implements OrderBookDAO {
	
	@Autowired
	private OrderBookRepository orderBookRepository;

	@Override
	public void newOrder(Order o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelOrder(Order o) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<OrderBook> getOrders() {
		return orderBookRepository.findAll();
	}

}
