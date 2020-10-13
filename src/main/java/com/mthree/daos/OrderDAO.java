package com.mthree.daos;

import java.util.List;

import com.mthree.models.Order;

public interface OrderDAO {
	
	Order findOrder(int orderId);
	List<Order> sliceOrder(Order order, int tradeQuantity);

}
