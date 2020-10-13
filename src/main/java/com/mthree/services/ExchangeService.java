package com.mthree.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.mthree.daos.ExchangeDAO;
import com.mthree.models.Exchange;
import com.mthree.models.Order;
import com.mthree.models.OrderBook;
import com.mthree.repositories.ExchangeRepository;

import org.springframework.beans.factory.annotation.Autowired;

public class ExchangeService implements ExchangeDAO {
	
	@Autowired
	private ExchangeRepository exchangeRepository;

	/** 
	 * @param exchangeId
	 * @return BigDecimal
	 */
	@Override
	public BigDecimal calculateTodaysTradeValue(int exchangeId) {
		
		Exchange exchange = findExchange(exchangeId);
		List<OrderBook> orderBooks = exchange.getOrderBooks();
		BigDecimal todaysTradeValue = BigDecimal.valueOf(0);
		
		for (OrderBook orderBook : orderBooks) {
			for (Order order : orderBook.getOrders()) {
				
				todaysTradeValue = todaysTradeValue.add(order.getPrice());
				
			}
		}
		
		return todaysTradeValue;
	}
	
	
	/** 
	 * @param exchangeId
	 * @return Exchange
	 */
	@Override
	public Exchange findExchange(int exchangeId) {

		Optional<Exchange> exchange = exchangeRepository.findById(exchangeId);

		if (exchange.isPresent()) {
			return exchange.get();
		}
		
		return null;
	}
}
