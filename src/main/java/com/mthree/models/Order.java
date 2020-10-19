package com.mthree.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
	@Enumerated(value=EnumType.STRING)
	private Ric ric;
	
	@Column(name="price")
	private BigDecimal price;
	
	@Column(name="quantity")
	private int quantity;
	
	@Column(name="type")
	@Enumerated(value=EnumType.STRING)
	private OrderType type;

	@Column(name="submit_time")
	private LocalDateTime submitTime;
	
	public Order() {}
	
	public Order(int id, Ric ric, BigDecimal price, int quantity, OrderType type, LocalDateTime submitTime) {
		this.id = id;
		this.ric = ric;
		this.price = price;
		this.quantity = quantity;
		this.type = type;
		this.submitTime = submitTime;
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
	 * @return BigDecimal
	 */
	public BigDecimal getPrice() {
		return price;
	}

	
	/** 
	 * @param price
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	
	/** 
	 * @return int
	 */
	public int getQuantity() {
		return quantity;
	}

	
	/** 
	 * @param quantity
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
	/** 
	 * @return OrderType
	 */
	public OrderType getType() {
		return type;
	}

	
	/** 
	 * @param type
	 */
	public void setType(OrderType type) {
		this.type = type;
	}

	/** 
	 * @return LocalDateTime
	 */
	public LocalDateTime getSubmitTime() {
		return submitTime;
	}

	
	/** 
	 * @param submitTime
	 */
	public void setSubmitTime(LocalDateTime submitTime) {
		this.submitTime = submitTime;
	}

	
	/** 
	 * @return String
	 */
	@Override
	public String toString() {
		return "Order [id=" + id + ", ric=" + ric + ", price=" + price + ", quantity=" + quantity + ", type=" + type + ", submitTime=" + submitTime +"]";
	}
}
