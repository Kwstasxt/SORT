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
	
	@Column(name="name")
	private String name;
	
	@Column(name="role")
	@Enumerated(value=EnumType.STRING)
	private Role role;
	
	@OneToMany
	private List<OrderBook> orderBooks;
	
	public Trader() {}

	public Trader(int id, String username, String password, String name, Role role, List<OrderBook> orderBooks) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.role = role;
		this.orderBooks = orderBooks;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<OrderBook> getOrderBooks() {
		return orderBooks;
	}

	public void setOrderBooks(List<OrderBook> orderBooks) {
		this.orderBooks = orderBooks;
	}

	@Override
	public String toString() {
		return "Trader [id=" + id + ", username=" + username + ", password=" + password + ", name=" + name + ", role=" + role + ", orderBooks=" + orderBooks + "]";
	}
}
