package com.mthree.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="order_books")
public class OrderBook {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "order_book_id")
	private int id;
	
	@Column(name="ric")
	private String ric;
	
	@OneToMany
	private List<Order> orders;

	public OrderBook() {} 
	
	public OrderBook(int id, String ric, List<Order> orders) {
		this.id = id;
		this.ric = ric;
		this.orders = orders;
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

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	@Override
	public String toString() {
		return "OrderBook [id=" + id + ", ric=" + ric + ", orders=" + orders + "]";
	}
}
