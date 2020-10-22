package com.mthree.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.mthree.models.ExchangeMpid;
import com.mthree.models.OrderType;
import com.mthree.models.Ric;

public class OrderDTO {
	
	private int id;
	private Ric ric;
	private BigDecimal price;
	private int quantity;
	private OrderType type;
    private LocalDateTime submitTime;
    
    private ExchangeMpid mpid;
	
	public OrderDTO() {}
	
	public OrderDTO(int id, Ric ric, BigDecimal price, int quantity, OrderType type, LocalDateTime submitTime, ExchangeMpid mpid) {
		this.id = id;
		this.ric = ric;
		this.price = price;
		this.quantity = quantity;
		this.type = type;
        this.submitTime = submitTime;
        this.mpid = mpid;
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
     * @return ExchangeMpid
     */
    public ExchangeMpid getMpid() {
        return mpid;
    }

    
    /** 
     * @param mpid
     */
    public void setMpid(ExchangeMpid mpid) {
        this.mpid = mpid;
    }

    @Override
    public String toString() {
        return "OrderDTO [id=" + id + ", mpid=" + mpid + ", price=" + price + ", quantity=" + quantity + ", ric=" + ric + ", submitTime=" + submitTime + ", type=" + type + "]";
    }

}
