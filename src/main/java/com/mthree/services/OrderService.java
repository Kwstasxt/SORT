package com.mthree.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mthree.daos.OrderDAO;
import com.mthree.models.Order;
import com.mthree.models.OrderType;
import com.mthree.models.Ric;
import com.mthree.repositories.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService implements OrderDAO {

	@Autowired
	private OrderRepository orderRepository;
	
	/** 
	 * Using a given type we can calculate different prices for an instrument.
	 * 
	 * @param type
	 * @return BigDecimal
	 */
	public BigDecimal generatePrice(int type) {

		String[] multiplierByEachRic = { "200", "500" };

		String range = multiplierByEachRic[type];

		BigDecimal max = new BigDecimal(range + ".0");
		BigDecimal randFromDouble = BigDecimal.valueOf(Math.random());

		return randFromDouble.multiply(max);
	}

	/**
	 * @param orderId
	 * @return Order
	 */
	@Override
	public Order findOrder(int orderId) {

		Optional<Order> order = orderRepository.findById(orderId);

		if (order.isPresent()) {
			return order.get();
		}

		return null;
	}

	
	/** 
	 * Slices a method in 2 based on a given quantity (slices price in same ratio). Note: caller must remember to replace the existing order from the orderbook with the orders produced by this function.
	 * 
	 * @param order
	 * @param tradeQuantity
	 * @return List<Order>
	 */
	@Override
	public List<Order> sliceOrder(Order order, int tradeQuantity) {

		List<Order> slicedOrder = new ArrayList<>();

		int originalQuantity = order.getQuantity();

		// if order can be sliced 
		if (originalQuantity > tradeQuantity) {

			Order order1 = new Order();
			Order order2 = new Order();

			Ric ric = order.getRic();
			OrderType type = order.getType();

			int quantity1 = tradeQuantity;
			int quantity2 = originalQuantity - tradeQuantity;

			double sliceRatio1 = (double) quantity1 / originalQuantity;
			double sliceRatio2 = (double) quantity2 / originalQuantity;

			BigDecimal orderPrice1 = order.getPrice().multiply(BigDecimal.valueOf(sliceRatio1));
			BigDecimal orderPrice2 = order.getPrice().multiply(BigDecimal.valueOf(sliceRatio2));

			order1.setRic(ric);
			order1.setPrice(orderPrice1);
			order1.setQuantity(quantity1);
			order1.setType(type);

			order2.setRic(ric);
			order2.setPrice(orderPrice2);
			order2.setQuantity(quantity2);
			order2.setType(type);

			slicedOrder.add(order1);
			slicedOrder.add(order2);
		}

		return slicedOrder;
	}



	/** 
	 * Returns all orders from the DB. 
	 * @return List<Order>
	 */
	public List<Order> findAllOrders(){
		return orderRepository.findAll();
	}


	/** 
	 * Delete order with this ID. 
	 * @return int (Number of rows affected)
	 */
	public void deleteOrderByID(int id){
		orderRepository.deleteById(id);
	}


	/** 
	 * Update order quantity with this ID. 
	 * @return int (Number of rows affected)
	 */
	public int updateOrderQuantityByID(int id, int value){
		return orderRepository.updateOrderQuantityByID(id, value);
	}



}
