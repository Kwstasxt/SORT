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
import javax.persistence.Transient;

@Entity
@Table(name="traders")
public class Trader {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "trader_id")
	private int id; 
	
	@Column(name="username")
	private String username;
	
	@Column(name="password")
	private String password;
	
	@Transient
    private String passwordConfirm;
	
	@Column(name="role")
	@Enumerated(value=EnumType.STRING)
	private Role role;
	
	@OneToMany
	private List<Order> orders;
	
	public Trader() {}

	public Trader(int id, String username, String password, Role role, List<Order> orders) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.role = role;
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
	 * @return Role
	 */
	public Role getRole() {
		return role;
	}

	
	/** 
	 * @param role
	 */
	public void setRole(Role role) {
		this.role = role;
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
		return "Trader [id=" + id + ", username=" + username + ", password=" + password + ", role=" + role + ", orders=" + orders + "]";
	}

	
	/** 
	 * @return int
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((orders == null) ? 0 : orders.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		Trader other = (Trader) obj;
		if (orders == null) {
			if (other.orders != null)
				return false;
		} else if (!orders.equals(other.orders))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
}
