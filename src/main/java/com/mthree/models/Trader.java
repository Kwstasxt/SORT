package com.mthree.models;

import java.util.List;

public class Trader {
	
	private String username;
	
	private String password;
	
	private List<OrderBook> orderBooks;

	public Trader(String username, String password, List<OrderBook> orderBooks) {
		this.username = username;
		this.password = password;
		this.orderBooks = orderBooks;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<OrderBook> getOrderBooks() {
		return orderBooks;
	}

	public void setOrderBooks(List<OrderBook> orderBooks) {
		this.orderBooks = orderBooks;
	}

	@Override
	public String toString() {
		return "Trader [username=" + username + ", password=" + password + ", orderBooks=" + orderBooks + "]";
	}
}
