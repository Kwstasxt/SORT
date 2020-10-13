package com.mthree.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class Future extends Order {

	private LocalDateTime expiryDateTime;
	
	public Future() {}

	public Future(int id, Ric ric, BigDecimal price, int quantity, OrderType type, LocalDateTime expiryDateTime) {
		super(id, ric, price, quantity, type);
		this.expiryDateTime = expiryDateTime;
	}

	
	/** 
	 * @return LocalDateTime
	 */
	public LocalDateTime getExpiryDateTime() {
		return expiryDateTime;
	}

	
	/** 
	 * @param expiryDateTime
	 */
	public void setExpiryDateTime(LocalDateTime expiryDateTime) {
		this.expiryDateTime = expiryDateTime;
	}

	
	/** 
	 * @return String
	 */
	@Override
	public String toString() {
		return "Future [id=" + super.getId() + 
				", ric=" + super.getRic() + 
				", price=" + super.getPrice() + 
				", quantity=" + super.getQuantity() + 
				", type=" + super.getType() + 
				", expiryDateTime=" + expiryDateTime + "]";
	}
}
