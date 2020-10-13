package com.mthree.models;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="exchanges")
public class Exchange {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "exchange_id")
	private int id;
	
	@OneToMany
	private List<Fee> feeLadder;
	
	@OneToMany
	private List<OrderBook> orderBooks;
	
	// total of all transactions in a day
	@Column(name="todays_trade_value")
	private BigDecimal todaysTradeValue;
	
	public Exchange() {}
	
	public Exchange(int id, List<Fee> feeLadder, List<OrderBook> orderBooks, BigDecimal todaysTradeValue) {
		this.id = id;
		this.feeLadder = feeLadder;
		this.orderBooks = orderBooks;
		this.todaysTradeValue = todaysTradeValue;
	}
	
	
	/** 
	 * @return int
	 */
	public int getId() {
		return id;
	}

	
	/** 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	
	/** 
	 * @return List<Fee>
	 */
	public List<Fee> getFeeLadder() {
		return feeLadder;
	}

	
	/** 
	 * @param feeLadder
	 */
	public void setFeeLadder(List<Fee> feeLadder) {
		this.feeLadder = feeLadder;
	}

	
	/** 
	 * @return List<OrderBook>
	 */
	public List<OrderBook> getOrderBooks() {
		return orderBooks;
	}

	
	/** 
	 * @param orderBooks
	 */
	public void setOrderBooks(List<OrderBook> orderBooks) {
		this.orderBooks = orderBooks;
	}

	
	/** 
	 * @return BigDecimal
	 */
	public BigDecimal getTodaysTradeValue() {
		return todaysTradeValue;
	}

	
	/** 
	 * @param todaysTradeValue
	 */
	public void setTodaysTradeValue(BigDecimal todaysTradeValue) {
		this.todaysTradeValue = todaysTradeValue;
	}

	
	/** 
	 * @return String
	 */
	@Override
	public String toString() {
		return "Exchange [id=" + id + ", feeLadder=" + feeLadder + ", orderBooks=" + orderBooks + ", todaysTradeValue="
				+ todaysTradeValue + "]";
	}
}
