package com.mthree.daos;

import java.util.List;

import com.mthree.models.Order;
import com.mthree.models.OrderBook;
import com.mthree.models.Ric;
import com.mthree.models.Sort;

public interface OrderBookDAO {
	
	OrderBook addOrder(int orderBookId, int orderId);
	OrderBook cancelOrder(int orderBookId, int orderId);
	List<OrderBook> getOrderBooks();
	OrderBook findOrderBook(int orderBookId);
	List<Order> getBuyOrders(List<OrderBook> orderBooks);
	List<Order> getSellOrders(List<OrderBook> orderBooks);
	List<OrderBook> generateRandomOrders();
	List<OrderBook> getOrderbooksForRic(Sort sort, Ric ric);
	int calculateNumberOfOrders(List<OrderBook> orderBooks);
	int calculateVolume(List<OrderBook> orderBooks);

}
