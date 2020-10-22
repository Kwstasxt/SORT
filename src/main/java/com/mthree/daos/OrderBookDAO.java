package com.mthree.daos;

import java.util.List;

import com.mthree.models.OrderBook;
import com.mthree.models.Ric;
import com.mthree.models.Sort;

public interface OrderBookDAO {
	
	List<OrderBook> getOrderBooks();
	OrderBook findOrderBook(int orderBookId);
	List<OrderBook> generateRandomOrders();
	List<OrderBook> getOrderbooksForRic(Sort sort, Ric ric);
	int calculateNumberOfOrders(List<OrderBook> orderBooks);
	int calculateVolume(List<OrderBook> orderBooks);

}
