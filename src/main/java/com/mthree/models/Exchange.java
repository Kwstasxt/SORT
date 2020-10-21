package com.mthree.models;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "exchanges")
public class Exchange {

	@Id
	@Column(name = "mpid")
	private String mpid;
	
	@OneToMany(cascade=CascadeType.ALL)
	private List<Fee> fees;

	@OneToMany(cascade=CascadeType.ALL)
	private List<OrderBook> orderBooks;

	// total of all transactions in a day
	@Column(name = "todays_trade_value")
	private BigDecimal todaysTradeValue;

	// adjust the quantity threshold by this value to calculate fees
	@Column(name = "share_quantity_threshold")
	private int shareQuantityThreshold;
	
	@OneToMany
	private List<Trade> trades;

	public Exchange() {}
	
	public Exchange(ExchangeMpid mpid, List<Fee> feeLadder, List<OrderBook> orderBooks, BigDecimal todaysTradeValue, int shareQuantityThreshold) {
		this.mpid = mpid.getNotation();
		this.fees = feeLadder;
		this.orderBooks = orderBooks;
		this.todaysTradeValue = todaysTradeValue;
		this.shareQuantityThreshold = shareQuantityThreshold;
	}


	public List<Trade> getTrades() {
		return trades;
	}

	public void setTrades(List<Trade> trades) {
		this.trades = trades;
	}

	/** 
	 * @return ExchangeMpid
	 */
	public ExchangeMpid getMpid() {
		
		for (ExchangeMpid exchangeMpid : ExchangeMpid.values()) {
			if (exchangeMpid.getNotation().equals(mpid)) {
				return exchangeMpid;
			}
		}
		return null;
	}

	
	/** 
	 * @param mpid
	 */
	public void setMpid(ExchangeMpid mpid) {
		this.mpid = mpid.getNotation();
	}

	
	/** 
	 * @return List<Fee>
	 */
	public List<Fee> getFeeLadder() {
		return fees;
	}


	/**
	 * @param feeLadder
	 */
	public void setFeeLadder(List<Fee> feeLadder) {
		this.fees = feeLadder;
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
	 * @return int
	 */
	public int getShareQuantityThreshold() {
		return shareQuantityThreshold;
	}

	
	/** 
	 * @param shareQuantityThreshold
	 */
	public void setShareQuantityThreshold(int shareQuantityThreshold) {
		this.shareQuantityThreshold = shareQuantityThreshold;
	}
	

	/**
	 * @return String
	 */
	@Override
	public String toString() {
		return "Exchange [mpid=" + mpid + ", feeLadder=" + fees + ", orderBooks=" + orderBooks + ", todaysTradeValue=" + todaysTradeValue + ", shareQuantityThreshold=" + shareQuantityThreshold + "]";
	}
}
