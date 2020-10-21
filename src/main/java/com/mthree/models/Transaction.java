package com.mthree.models;

import java.math.BigDecimal;

//Represents individual transactions made by orderbook quotes.
public class Transaction {

	private ExchangeMpid exchangeMpid;
	private BigDecimal spread;
	private int transactionId;
	private String ric;
	private int quantity;
	// value traded is the price of sellers order. A buyer only payes the amount the seller asks.
	private BigDecimal valueTraded;

	public Transaction() {}

	public Transaction(ExchangeMpid exchangeMpid, BigDecimal spread, int transactionId, String ric, int quantity, BigDecimal valueTraded) {
		super();
		this.exchangeMpid = exchangeMpid;
		this.spread = spread;
		this.transactionId = transactionId;
		this.ric = ric;
		this.quantity = quantity;
		this.valueTraded = valueTraded;
	}

	public ExchangeMpid getExchangeMpid() {
		return exchangeMpid;
	}

	public void setExchangeMpid(ExchangeMpid exchangeMpid) {
		this.exchangeMpid = exchangeMpid;
	}

	public BigDecimal getSpread() {
		return spread;
	}

	public void setSpread(BigDecimal spread) {
		this.spread = spread;
	}

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public String getRic() {
		return ric;
	}

	public void setRic(String ric) {
		this.ric = ric;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getValueTraded() {
		return this.valueTraded;
	}

	public void setValueTraded(BigDecimal valueTraded) {
		this.valueTraded = valueTraded;
	}

	@Override
	public String toString() {
		return "Transaction [exchangeMpid=" + exchangeMpid + ", spread=" + spread + ", transactionId=" + transactionId + ", ric=" + ric + ", quantity=" + quantity + ", valueTraded=" + valueTraded + "]";
	}

}
