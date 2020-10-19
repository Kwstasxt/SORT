package com.mthree.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mthree.daos.OrderBookDAO;
import com.mthree.models.Order;
import com.mthree.models.OrderBook;
import com.mthree.models.OrderType;
import com.mthree.models.Ric;
import com.mthree.repositories.OrderBookRepository;

@Service
public class OrderBookService implements OrderBookDAO {
	
	@Autowired
	private OrderBookRepository orderBookRepository;

	@Autowired
	private OrderService orderService;

	private Random random = new Random();

	/** 
	 * @param orderBookId
	 * @param o
	 * @return OrderBook
	 */
	@Override
	public OrderBook addOrder(int orderBookId, int orderId) {
		
		OrderBook orderBook = findOrderBook(orderBookId);
		List<Order> orders = orderBook.getOrders();
		orders.add(orderService.findOrder(orderId));
		return orderBookRepository.save(orderBook);
	}

	
	/** 
	 * @param orderBookId
	 * @param o
	 * @return OrderBook
	 */
	@Override
	public OrderBook cancelOrder(int orderBookId, int orderId) {
		
		OrderBook orderBook = findOrderBook(orderBookId);
		List<Order> orders = orderBook.getOrders();
		orders.remove(orderService.findOrder(orderId));
		return orderBookRepository.save(orderBook);
	}
	
	
	/** 
	 * @return List<OrderBook>
	 */
	@Override
	public List<OrderBook> getOrderBooks() {
		return orderBookRepository.findAll();
	}
	
	
	/** 
	 * @param orderBookId
	 * @return OrderBook
	 */
	@Override
	public OrderBook findOrderBook(int orderBookId) {
	
		Optional<OrderBook> orderBook = orderBookRepository.findById(orderBookId);

		if (orderBook.isPresent()) {
			return orderBook.get();
		}
		return null;
	}


	/**
	 * Generates random orders using predetermined instrument types.
	 * 
	 * @return Order
	 */
	@Override
	public List<OrderBook> generateRandomOrders() {

		Ric[] ricTypes = Ric.values();
		BigDecimal price;
		int quantity;

		ArrayList<OrderBook> orderBookList = new ArrayList<>();

		// create 100 buy/sell orders for each Instrument type.
		for (int i = 0; i < ricTypes.length; i++) {

			ArrayList<Order> orders = new ArrayList<>();

			for (int j = 0; j < 100; j++) {

				//buy orders
				quantity = random.nextInt() * 3000;
				price = generatePrice(OrderType.BUY);

				Order buyOrder = new Order();
				buyOrder.setRic(ricTypes[i]);
				buyOrder.setPrice(price);
				buyOrder.setQuantity(quantity);
				buyOrder.setType(OrderType.BUY);
				orders.add(buyOrder);

				// sell orders
				quantity = random.nextInt() * 3000;
				price = generatePrice(OrderType.SELL);

				Order sellOrder = new Order();
				sellOrder.setRic(ricTypes[i]);
				sellOrder.setPrice(price);
				sellOrder.setQuantity(quantity);
				sellOrder.setType(OrderType.SELL);
				orders.add(sellOrder);
			}

			OrderBook orderBook = new OrderBook();
			orderBook.setRic(ricTypes[i]);
			orderBook.setOrders(orders);
			orderBookList.add(orderBook);
		}
		return orderBookList;
	}

	
	/** 
	 * Using a given type we can calculate different prices for an instrument,
	 * ensuring price overlap bewtween buy and sell based on a base price of 500.
	 * 
	 * @param orderType
	 * @return BigDecimal
	 */
	public BigDecimal generatePrice(OrderType orderType) {

		int base = 500;
		double modifier = (Math.random() / 5) + 0.95; // 95% - 115% modifier

		if (orderType == OrderType.BUY) {
			modifier = (Math.random() / 5) + 0.85; // 85% - 105% modifier
		}

		return BigDecimal.valueOf(base * modifier);
	}


	
	/** 
	 * Calculate total number of orders for a given list of orderbooks.
	 * 
	 * @param orderBooks
	 * @return int
	 */
	@Override
	public int calculateNumberOfOrders(List<OrderBook> orderBooks) {

		int numOfOrders = 0;

		for (OrderBook orderBook : orderBooks) {
			numOfOrders += orderBook.getOrders().size();
		}

		return numOfOrders;
	}

	
	/** 
	 * Calculate total volume for a given list of orderbooks.
	 * 
	 * @param orderBook
	 * @return int
	 */
	@Override
	public int calculateVolume(List<OrderBook> orderBooks) {

		int volume = 0;

		for (OrderBook orderBook : orderBooks) {
			for (Order order : orderBook.getOrders()) {
				volume += order.getQuantity();
			}
		}

		return volume;
	}
}
