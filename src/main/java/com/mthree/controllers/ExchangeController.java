package com.mthree.controllers;

import org.springframework.stereotype.Controller;

@Controller
public class ExchangeController {
	
	/*
		public boolean determineLiquidity(Order o) {
			
			if (o.getQuantity()>0) {
				return false;
			}else {
				return true;
			}
		}
		
		
		public boolean determineIfHasGoodPrice(ArrayList<Order> exchanges, OrderBook originalOrderBook) {
			
			//if at least one trade has higher price than the buyers in the original orderbook then this
			//exchange is considered to as a not "Good Price Exchange".
			int comparator;
			for (Order exc: exchanges) {
				for (Order original: originalOrderBook.getOrders()) {
					if (original.getType()==OrderType.BUY) {
						comparator = exc.getPrice().compareTo(original.getPrice());
						if (comparator==1) {
							return false;
						}
						
					}
				}
			}
			
			return true;
		}
	*/
}
