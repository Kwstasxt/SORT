package com.mthree.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="trades")
public class Trade {
    
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "trade_id")
    private int id; 
    
    @OneToOne
    private Order buyOrder;
    
    @OneToOne
    private Order sellOrder;

    @Column(name = "trade_date")
    private LocalDateTime tradeDate;
    
    public Trade() {}

    public Trade(int id, Order buyOrder, Order sellOrder, LocalDateTime tradeDate) {
        this.id = id;
        this.buyOrder = buyOrder;
        this.sellOrder = sellOrder;
        this.tradeDate = tradeDate;
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
     * @return Order
     */
    public Order getBuyOrder() {
        return buyOrder;
    }

    
    /** 
     * @param buyOrder
     */
    public void setBuyOrder(Order buyOrder) {
        this.buyOrder = buyOrder;
    }

    
    /** 
     * @return Order
     */
    public Order getSellOrder() {
        return sellOrder;
    }

    
    /** 
     * @param sellOrder
     */
    public void setSellOrder(Order sellOrder) {
        this.sellOrder = sellOrder;
    }

    
    
    /** 
     * @return LocalDateTime
     */
    public LocalDateTime getTradeDate() {
        return tradeDate;
    }

    
    /** 
     * @param tradeDate
     */
    public void setTradeDate(LocalDateTime tradeDate) {
        this.tradeDate = tradeDate;
    }

    
    /** 
     * @return String
     */
    @Override
    public String toString() {
        return "Trade [buyOrder=" + buyOrder + ", id=" + id + ", sellOrder=" + sellOrder + ", tradeDate=" + tradeDate
                + "]";
    }

}
