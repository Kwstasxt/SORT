package com.mthree.daos;

import java.util.List;

import com.mthree.models.Order;
import com.mthree.models.OrderBook;

public interface OrderBookDAO {
	
	void addOrder(Order o);
	void cancelOrder(Order o);
	List<OrderBook> getOrderBooks();

}
