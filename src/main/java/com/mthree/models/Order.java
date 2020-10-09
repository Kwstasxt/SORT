package com.mthree.models;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="orders")
public class Order {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "order_id")
	private int id;
	
	@Column(name="ric")
	private String ric;
	
	@Column(name="price")
	private BigDecimal price;
	
	@Column(name="quantity")
	private int quantity;
	
	@Column(name="type")
	@Enumerated(value=EnumType.STRING)
	private OrderType type;
	
	public Order() {}
	
	public Order(int id, String ric, BigDecimal price, int quantity, OrderType type) {
		this.id = id;
		this.ric = ric;
		this.price = price;
		this.quantity = quantity;
		this.type = type;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
		return "Order [id=" + id + ", ric=" + ric + ", price=" + price + ", quantity=" + quantity + "]";
	}
}
