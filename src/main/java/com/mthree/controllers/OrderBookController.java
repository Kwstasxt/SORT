package com.mthree.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mthree.models.Order;
import com.mthree.models.OrderBook;
import com.mthree.models.OrderType;
import com.mthree.services.OrderBookService;

@Controller
public class OrderBookController {

	@Autowired
	private OrderBookService orderBookService;

	@GetMapping("/createNewOrder")
	public void AddOrder(@RequestParam("order") Order o) {
		orderBookService.addOrder(o);
	}

	@GetMapping("/cancelOrder")
	public void CancelOrder(@RequestParam("order") Order o) {
		orderBookService.cancelOrder(o);
	}

	// Instrument type is predetermined.
	public List<OrderBook> GenerateOrderBook() {
		Random random = new Random();
		// String [] ricTypes = {"VOD.L", "BT.L","IBM.L","CL.L"};

		// Instrument types
		String[] ricTypes = { "VOD.L", "BT.L" };
		int index;
		BigDecimal price;

		int quantity;
		OrderType[] type = { OrderType.BUY, OrderType.SELL };

		ArrayList<OrderBook> orderBooks = new ArrayList<OrderBook>();
		ArrayList<Order> orders = new ArrayList<Order>();
		// Create 100 orders for each Instrument type.
		for (int i = 0; i < ricTypes.length; i++) {
			for (int j = 0; j < 100; j++) {
				quantity = random.nextInt() * 3000;
				price = generatePrice(i);
				index = random.nextInt(ricTypes.length);

				Order order = new Order();
				order.setRic(ricTypes[i]);
				order.setPrice(price);
				order.setQuantity(quantity);
				order.setType(type[index]);
				orders.add(order);
			}

			OrderBook orderBook = new OrderBook();
			orderBook.setRic(ricTypes[i]);
			orderBook.setOrders(orders);
			orderBooks.add(orderBook);
			orders.clear();
		}

		return orderBooks;

	}

	public BigDecimal generatePrice(int type) {

		// With type we calculate different the price based on the instrument

		String[] multiplierByEachRic = { "200", "500" };

		String range = multiplierByEachRic[type];

		BigDecimal max = new BigDecimal(range + ".0");
		BigDecimal randFromDouble = new BigDecimal(Math.random());

		BigDecimal actualRandomDec = randFromDouble.multiply(max);

		// BigInteger actualRandom = actualRandomDec.toBigInteger();

		return actualRandomDec;

	}

}
