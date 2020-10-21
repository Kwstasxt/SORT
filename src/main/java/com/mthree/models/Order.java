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
		return "Order [id=" + id + ", ric=" + ric + ", price=" + price + ", quantity=" + quantity + ", type=" + type + ", submitTime=" + submitTime + "]";
	}

	
	/** 
	 * @return int
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + quantity;
		result = prime * result + ((ric == null) ? 0 : ric.hashCode());
		result = prime * result + ((submitTime == null) ? 0 : submitTime.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	
	/** 
	 * @param obj
	 * @return boolean
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (quantity != other.quantity)
			return false;
		if (ric != other.ric)
			return false;
		if (submitTime == null) {
			if (other.submitTime != null)
				return false;
		} else if (!submitTime.equals(other.submitTime))
			return false;
		if (type != other.type)
			return false;
		return true;
	}	
}
