package com.mthree.daos;

import com.mthree.models.Order;
import com.mthree.models.Trader;

public interface TraderDAO {
	
	Trader addTrader(Trader user);
	Trader findByUsername(String username);
	Trader findByTraderId(int traderId);
	Trader findByOrder(Order order);
	void removeTrader(Trader t);
	
}
