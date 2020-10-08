package com.mthree.models;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class Exchange {
	
	private Map<FeeType, BigDecimal> feeLadder;
	private List<OrderBook> orderBooks;
	private BigDecimal todaysTradeValue;
	
	public Exchange(Map<FeeType, BigDecimal> feeLadder, List<OrderBook> orderBooks, BigDecimal todaysTradeValue) {
		this.feeLadder = feeLadder;
		this.orderBooks = orderBooks;
		this.todaysTradeValue = todaysTradeValue;
	}

	public Map<FeeType, BigDecimal> getFeeLadder() {
		return feeLadder;
	}

	public void setFeeLadder(Map<FeeType, BigDecimal> feeLadder) {
		this.feeLadder = feeLadder;
	}

	public List<OrderBook> getOrderBooks() {
		return orderBooks;
	}

	public void setOrderBooks(List<OrderBook> orderBooks) {
		this.orderBooks = orderBooks;
	}

	public BigDecimal getTodaysTradeValue() {
		return todaysTradeValue;
	}

	public void setTodaysTradeValue(BigDecimal todaysTradeValue) {
		this.todaysTradeValue = todaysTradeValue;
	}

	@Override
	public String toString() {
		return "Exchange [feeLadder=" + feeLadder + ", orderBooks=" + orderBooks + ", todaysTradeValue="
				+ todaysTradeValue + "]";
	}
}
