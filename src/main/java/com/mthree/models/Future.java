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

	public Future(int id, String ric, BigDecimal price, int quantity, OrderType type, LocalDateTime expiryDateTime) {
		super(id, ric, price, quantity, type);
		this.expiryDateTime = expiryDateTime;
	}

	public LocalDateTime getExpiryDateTime() {
		return expiryDateTime;
	}

	public void setExpiryDateTime(LocalDateTime expiryDateTime) {
		this.expiryDateTime = expiryDateTime;
	}

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
