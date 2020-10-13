package com.mthree.models;

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
    
    public Trade() {}

    public Trade(int id, Order buyOrder, Order sellOrder) {
        this.id = id;
        this.buyOrder = buyOrder;
        this.sellOrder = sellOrder;
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
     * @return String
     */
    @Override
    public String toString() {
        return "Trade [buyOrder=" + buyOrder + ", id=" + id + ", sellOrder=" + sellOrder + "]";
    }
}
