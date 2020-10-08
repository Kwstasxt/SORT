package com.mthree.models;

import java.math.BigDecimal;

import javax.persistence.Enumerated;

public class Order {
	
	private String ric;
	private BigDecimal price;
	private int quantity;
	
//	@Enumerated(value=OrderType.STRING)
	private OrderType type;
	
	public Order(String ric, BigDecimal price, int quantity, OrderType type) {
		this.ric = ric;
		this.price = price;
		this.quantity = quantity;
		this.type = type;
	}

	public String getRic() {
		return ric;
	}

	public void setRic(String ric) {
		this.ric = ric;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public OrderType getType() {
		return type;
	}

	public void setType(OrderType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Order [ric=" + ric + ", price=" + price + ", quantity=" + quantity + "]";
	}
}
