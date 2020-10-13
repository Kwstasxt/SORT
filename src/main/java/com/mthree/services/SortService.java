package com.mthree.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import com.mthree.models.Exchange;
import com.mthree.models.Order;
import com.mthree.models.OrderBook;
import com.mthree.models.Ric;
import com.mthree.models.Sort;
import com.mthree.daos.SortDAO;

@Service
public class SortService implements SortDAO {

	@Override
	public void executeTrade() {
		// TODO Auto-generated method stub
	}

	
	/** 
	 * For each orderbook in each exchange listed in the sort class, collate orders for a given instrument.
	 * 
	 * @param sort
	 * @return List<OrderBook>
	 */
	@Override
	public List<OrderBook> combineOrderBooks(Sort sort) {

		List<OrderBook> combinedOrderBooks = new ArrayList<>();

		// for all rics in enum type
		for (Ric instrument : Ric.values()) {
			
			OrderBook instrumentOrderBook = new OrderBook();
			instrumentOrderBook.setRic(instrument);

			List<Order> instrumentOrders = new ArrayList<>();

			// for same ric add all orders to one orderbook
			for (Exchange exchange : sort.getExchanges()) {
				for (OrderBook orderBook : exchange.getOrderBooks()) {

					String currentRic = instrument.getNotation();
					String orderBookRic = orderBook.getRic().getNotation();
					
					if (orderBookRic.equals(currentRic)) {
						instrumentOrders.addAll(orderBook.getOrders());
					}
				}
			}

			instrumentOrderBook.setOrders(instrumentOrders);
			combinedOrderBooks.add(instrumentOrderBook);
		}

		return combinedOrderBooks;
	}

}
