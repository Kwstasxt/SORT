package com.mthree.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
	@Enumerated(value=EnumType.STRING)
	private Ric ric;

	@OneToMany
	private List<Order> orders;

	
	public OrderBook() {} 
	
	public OrderBook(int id, Ric ric, List<Order> orders) {
		this.id = id;
		this.ric = ric;
		this.orders = orders;
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
	 * @return String
	 */
	public Ric getRic() {
		return ric;
	}

	
	/** 
	 * @param ric
	 */
	public void setRic(Ric ric) {
		this.ric = ric;
	}

	
	/** 
	 * @return List<Order>
	 */
	public List<Order> getOrders() {
		return orders;
	}

	
	/** 
	 * @param orders
	 */
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	
	/** 
	 * @return String
	 */
	@Override
	public String toString() {
		return "OrderBook [id=" + id + ", ric=" + ric + ", orders=" + orders + "]";
	}
}
