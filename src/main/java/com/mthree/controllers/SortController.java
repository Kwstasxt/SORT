package com.mthree.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.mthree.services.SortService;

@Controller
public class SortController {
	
	@Autowired
	private SortService sortService;
	/*	
	
	//SORT trades on various exchanges based on best prices and fees.
	public ArrayList<Exchange> SortTrades(ArrayList<Exchange> exchanges,ArrayList<OrderBook> quotesOrderbook) {
		
		//Sorted exchanges are inserted in this field.
	
		ArrayList<Exchange> sortedExchanges = new ArrayList<Exchange>();
		
		
		for (Exchange exc : exchanges) {
			for (OrderBook excOrderbok: exc.getOrderBooks()) {
			
			}
			//if (exc.HasGoodPrice()) {
				
			//}
			
		}
		
		
		
		return exchanges;
		
	}
	*/
	
	public void executeOrder() {
		sortService.executeTrade();
	}
}
