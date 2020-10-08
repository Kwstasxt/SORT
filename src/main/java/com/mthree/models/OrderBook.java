package com.mthree.models;

import java.util.List;

public class OrderBook {
	
	private String ric;
	private List<Order> orders;

	public OrderBook(String ric, List<Order> orders) {
		this.ric = ric;
		this.orders = orders;
	}

	public String getRic() {
		return ric;
	}

	public void setRic(String ric) {
		this.ric = ric;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	@Override
	public String toString() {
		return "OrderBook [ric=" + ric + ", orders=" + orders + "]";
	}
}
