package com.mthree.daos;

import java.util.List;

import com.mthree.models.OrderBook;

public interface OrderBookDAO {
	
	OrderBook addOrder(int orderBookId, int orderId);
	OrderBook cancelOrder(int orderBookId, int orderId);
	List<OrderBook> getOrderBooks();
	OrderBook findOrderBook(int orderBookId);
	List<OrderBook> generateRandomOrders();
}
