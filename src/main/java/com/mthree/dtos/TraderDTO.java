package com.mthree.dtos;

import java.util.List;

import com.mthree.models.Order;
import com.mthree.models.Region;

public class TraderDTO {

    private int id;
    private String username;
	private String password;
	private String passwordConfirm;
	private Region region;
	private List<Order> orders;
	
	public TraderDTO() {}
    
    public TraderDTO(int id, String username, String password, String passwordConfirm, Region region, List<Order> orders) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.passwordConfirm = passwordConfirm;
		this.region = region;
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
	public String getUsername() {
		return username;
	}

	
	/** 
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	
	/** 
	 * @return String
	 */
	public String getPassword() {
		return password;
	}

	
	/** 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	
	/** 
	 * @return String
	 */
	public String getPasswordConfirm() {
        return passwordConfirm;
    }

    
	/** 
	 * @param passwordConfirm
	 */
	public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
	}
	

	/** 
	 * @return Region
	 */
	public Region getRegion() {
		return region;
	}

	
	/** 
	 * @param Region
	 */
	public void setRegion(Region region) {
		this.region = region;
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

	@Override
	public String toString() {
		return "TraderDTO [id=" + id + ", username=" + username + ", password=" + password + ", region=" + region + ", orders=" + orders + "]";
	}

}
