package com.mthree.models;

import java.util.List;

public class Sort {
	
	private List<Exchange> exchanges;
	private List<OrderBook> orderBooks;
	
	public Sort(List<Exchange> exchanges, List<OrderBook> orderBooks) {
		this.exchanges = exchanges;
		this.orderBooks = orderBooks;
	}

	public List<Exchange> getExchanges() {
		return exchanges;
	}

	public void setExchanges(List<Exchange> exchanges) {
		this.exchanges = exchanges;
	}

	public List<OrderBook> getOrderBooks() {
		return orderBooks;
	}

	public void setOrderBooks(List<OrderBook> orderBooks) {
		this.orderBooks = orderBooks;
	}

	@Override
	public String toString() {
		return "Sort [exchanges=" + exchanges + ", orderBooks=" + orderBooks + "]";
	}
}
