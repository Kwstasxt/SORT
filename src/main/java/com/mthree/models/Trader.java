package com.mthree.models;

import java.util.List;
import java.util.Objects;

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
@Table(name = "traders")
public class Trader {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "trader_id")
	private int id;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Transient
	private String passwordConfirm;

	@Column(name = "role")
	@Enumerated(value = EnumType.STRING)
	private Role role;

	@OneToMany
	private List<Order> orders;

	public Trader() {
	}

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
		return "Trader [id=" + id + ", username=" + username + ", password=" + password + ", role=" + role + ", orders="
				+ orders + "]";
	}

	/**
	 * @param o
	 * @return boolean
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Trader)) {
			return false;
		}
		Trader trader = (Trader) o;
		return id == trader.id && Objects.equals(username, trader.username) && Objects.equals(password, trader.password) 
					 && Objects.equals(role, trader.role) && Objects.equals(orders, trader.orders);
	}

	/**
	 * @return int
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id, username, password, role, orders);
	}

}
