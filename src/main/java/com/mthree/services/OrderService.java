package com.mthree.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.mthree.daos.OrderDAO;
import com.mthree.models.Exchange;
import com.mthree.models.ExchangeMpid;
import com.mthree.models.Order;
import com.mthree.models.OrderBook;
import com.mthree.models.OrderType;
import com.mthree.models.Ric;
import com.mthree.models.Sort;
import com.mthree.models.Trader;
import com.mthree.repositories.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService implements OrderDAO {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private SortService sortService;

	@Autowired
	private OrderBookService orderBookService;

	@Autowired
	private ExchangeService exchangeService;

	private Random random = new Random();

	/**
	 * @param orderId
	 * @return Order
	 */
	@Override
	public Order findOrder(int orderId) {

		Optional<Order> order = orderRepository.findById(orderId);

		if (order.isPresent()) {
			return order.get();
		}

		return null;
	}

	
	/** 
	 * Slices a method in 2 based on a given quantity (slices price in same ratio). Note: caller must remember to replace the existing order from the orderbook with the orders produced by this function.
	 * 
	 * @param order
	 * @param tradeQuantity
	 * @return List<Order>
	 */
	@Override
	public List<Order> sliceOrder(Order order, int tradeQuantity) {

		List<Order> slicedOrder = new ArrayList<>();

		int originalQuantity = order.getQuantity();

		// if order can be sliced 
		if (originalQuantity > tradeQuantity) {

			Order order1 = new Order();
			Order order2 = new Order();

			Ric ric = order.getRic();
			OrderType type = order.getType();

			int quantity1 = tradeQuantity;
			int quantity2 = originalQuantity - tradeQuantity;

			double sliceRatio1 = (double) quantity1 / originalQuantity;
			double sliceRatio2 = (double) quantity2 / originalQuantity;

			BigDecimal orderPrice1 = order.getPrice().multiply(BigDecimal.valueOf(sliceRatio1));
			BigDecimal orderPrice2 = order.getPrice().multiply(BigDecimal.valueOf(sliceRatio2));

			order1.setRic(ric);
			order1.setPrice(orderPrice1);
			order1.setQuantity(quantity1);
			order1.setType(type);

			order2.setRic(ric);
			order2.setPrice(orderPrice2);
			order2.setQuantity(quantity2);
			order2.setType(type);

			slicedOrder.add(order1);
			slicedOrder.add(order2);
		}

		return slicedOrder;
	}



	/** 
	 * Returns all orders from the DB. 
	 * @return List<Order>
	 */
	public List<Order> findAllOrders() {
		return orderRepository.findAll();
	}


	/** 
	 * Delete order with this ID. 
	 * @return int (Number of rows affected)
	 */
	public void deleteOrderByID(int id) {
		orderRepository.deleteById(id);
	}


	/** 
	 * @param order
	 * @return Order
	 */
	public Order addOrder(Order order) {
		return orderRepository.save(order);
	}


	/** 
	 * Update order quantity with this ID. 
	 * @return int (Number of rows affected)
	 */
	public int updateOrderQuantityByID(int id, int value) {
		return orderRepository.updateOrderQuantityByID(id, value);
	}

	// TODO: refactor
	/** 
	 * @return List<Order>
	 */
	public List<Order> generateRandomOrdersForTrader(Trader trader) {
		
		List<Order> orders = new ArrayList<>();

		OrderType[] orderTypes = OrderType.values();
		BigDecimal price;
		int quantity;

		// exchange to store trader's orders (chosen randomly from exchanges that are part of their sort instance)
		Sort sort = sortService.findSortForTrader(trader);
		List<ExchangeMpid> mpids = new ArrayList<>();

		sort.getExchanges().stream().forEach(sortExchange -> {
			mpids.add(sortExchange.getMpid());
		});

		int mpidIndex = random.nextInt(mpids.size());
		Exchange exchange = exchangeService.findExchange(mpids.get(mpidIndex));

		List<OrderBook> exchangeOrderBooks = exchange.getOrderBooks();

		for (Ric ric : Ric.values()) {

			List<Order> exchangeOrders = new ArrayList<>();

			for (int j = 0; j < 5; j++) {

				OrderType type = orderTypes[random.nextInt(orderTypes.length)];
	
				quantity = random.nextInt(3000);
				price = generatePrice(type);
	
				Order order = new Order();
				order.setRic(ric);
				order.setPrice(price);
				order.setQuantity(quantity);
				order.setType(type);
				order.setSubmitTime(LocalDateTime.now());
				orders.add(order);
				exchangeOrders.add(order); // separate order book for exchange so don't have to clear after each round
	
				// add to the database
				orderRepository.save(order);
			}

			OrderBook orderBook = new OrderBook();
			orderBook.setRic(ric);
			orderBook.setOrders(exchangeOrders);
			// add to the database
			orderBookService.saveOrderBook(orderBook);
			exchangeOrderBooks.add(orderBook);
		}

		exchange.setOrderBooks(exchangeOrderBooks);
		// add to the database
		exchangeService.saveExchange(exchange);

		return orders;			
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

		BigDecimal price = BigDecimal.valueOf(base * modifier);

		return price.setScale(2, RoundingMode.CEILING);
	}

}
