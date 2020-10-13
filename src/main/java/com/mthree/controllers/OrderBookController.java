package com.mthree.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mthree.services.OrderBookService;

@Controller
public class OrderBookController {

	@Autowired
	private OrderBookService orderBookService;

	@GetMapping("/createNewOrder")
	public void addOrder(@RequestParam("orderBookId") int orderBookId, @RequestParam("orderId") int orderId) {
		orderBookService.addOrder(orderBookId, orderId);
	}

	@GetMapping("/cancelOrder")
	public void cancelOrder(@RequestParam("orderBookId") int orderBookId, @RequestParam("orderId") int orderId) {
		orderBookService.cancelOrder(orderBookId, orderId);
	}
}
