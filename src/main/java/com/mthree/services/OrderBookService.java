package com.mthree.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mthree.daos.OrderBookDAO;
import com.mthree.models.Order;
import com.mthree.models.OrderBook;
import com.mthree.models.OrderType;
import com.mthree.models.Ric;
import com.mthree.models.Sort;
import com.mthree.repositories.OrderBookRepository;
import com.mthree.repositories.OrderRepository;

@Service
public class OrderBookService implements OrderBookDAO {
	
	@Autowired
	private OrderBookRepository orderBookRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderService orderService;

	@Autowired
	private SortService sortService;

	private Random random = new Random();
	
	
	/** 
	 * Return list of orderBooks from the DB.
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
	 * Return a list of orderbooks for a given ric found using the relevant smart order router.
	 * 
	 * @param ric
	 * @return List<OrderBook>
	 */
	@Override
	public List<OrderBook> getOrderbooksForRic(Sort sort, Ric ric) {

		// generate combined order book 
		List<OrderBook> combinedOrderBook = sortService.combineOrderBooks(sort);

		List<OrderBook> orderBooksForRic = new ArrayList<>();

		// find orderbooks for given ric 
		combinedOrderBook.stream().forEach(combinedOrders -> {
			String givenRic = ric.getNotation();
			String currentOrderBookRic = combinedOrders.getRic().getNotation();

			if (currentOrderBookRic.equals(givenRic)) {
				orderBooksForRic.add(combinedOrders);
			}
		});

		return orderBooksForRic;
	}


	// TODO: refactor
	/**
	 * Generates random orders using predetermined instrument types.
	 * 
	 * @return Order
	 */
	@Override
	public List<OrderBook> generateRandomOrders() {

		OrderType[] orderTypes = OrderType.values();
		Ric[] ricTypes = Ric.values();
		BigDecimal price;
		int quantity;

		ArrayList<OrderBook> orderBookList = new ArrayList<>();

		// create 100 orders for each Instrument type.
		for (int i = 0; i < ricTypes.length; i++) {

			ArrayList<Order> orders = new ArrayList<>();

			for (int j = 0; j < 5; j++) {

				OrderType type = orderTypes[random.nextInt(orderTypes.length)];

				quantity = random.nextInt(3000);
				price = generatePrice(type);

				Order order = new Order();
				order.setRic(ricTypes[i]);
				order.setPrice(price);
				order.setQuantity(quantity);
				order.setType(type);
				order.setSubmitTime(LocalDateTime.now());
				orders.add(order);

				// add to the database.
				orderRepository.save(order);
			}

			OrderBook orderBook = new OrderBook();
			orderBook.setRic(ricTypes[i]);
			orderBook.setOrders(orders);
			orderBookRepository.save(orderBook);

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

		BigDecimal price = BigDecimal.valueOf(base * modifier);

		return price.setScale(2, RoundingMode.CEILING);
	}

	/** 
	 * Calculate total number of orders for a given list of orderbooks.
	 * 
	 * @param orderBooks
	 * @return int
	 */
	@Override
	public int calculateNumberOfOrders(List<OrderBook> orderBooks) {

		AtomicInteger numOfOrders = new AtomicInteger(0);

		orderBooks.stream().forEach(orderBook -> {
			numOfOrders.set(numOfOrders.get() + orderBook.getOrders().size());
		});

		return numOfOrders.get();
	}

	
	/** 
	 * Calculate total volume for a given list of orderbooks.
	 * 
	 * @param orderBook
	 * @return int
	 */
	@Override
	public int calculateVolume(List<OrderBook> orderBooks) {

		AtomicInteger volume = new AtomicInteger(0);

		orderBooks.stream().forEach(orderBook -> 
			orderBook.getOrders().stream().forEach(order -> {
				volume.set(volume.get() + order.getQuantity());
			})
		);

		return volume.get();
	}


	
	/** 
	 * @param orderBook
	 */
	public void saveOrderBook(OrderBook orderBook) {
		orderBookRepository.save(orderBook);
	}

}
