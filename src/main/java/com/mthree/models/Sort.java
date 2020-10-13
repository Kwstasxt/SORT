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
@Table(name="sort")
public class Sort {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "sort_id")
	private int id;
	
	@OneToMany
	private List<Exchange> exchanges;
	
	@OneToMany
	private List<OrderBook> orderBooks;
	
	@Column(name="region")
	@Enumerated(value=EnumType.STRING)
	private Region region;
	
	public Sort() {}
	
	public Sort(int id, List<Exchange> exchanges, List<OrderBook> orderBooks, Region region) {
		this.id = id;
		this.exchanges = exchanges;
		this.orderBooks = orderBooks;
		this.region = region;
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
	 * @return List<Exchange>
	 */
	public List<Exchange> getExchanges() {
		return exchanges;
	}

	
	/** 
	 * @param exchanges
	 */
	public void setExchanges(List<Exchange> exchanges) {
		this.exchanges = exchanges;
	}

	
	/** 
	 * @return List<OrderBook>
	 */
	public List<OrderBook> getOrderBooks() {
		return orderBooks;
	}

	
	/** 
	 * @param orderBooks
	 */
	public void setOrderBooks(List<OrderBook> orderBooks) {
		this.orderBooks = orderBooks;
	}
	
	
	/** 
	 * @return Region
	 */
	public Region getRegion() {
		return region;
	}

	
	/** 
	 * @param region
	 */
	public void setRegion(Region region) {
		this.region = region;
	}

	
	/** 
	 * @return String
	 */
	@Override
	public String toString() {
		return "Sort [id=" + id + ", exchanges=" + exchanges + ", orderBooks=" + orderBooks + ", region=" + region + "]";
	}
}
