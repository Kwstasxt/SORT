package com.mthree.daos;

import com.mthree.models.Trader;

public interface TraderDAO {
	
	Trader addTrader(Trader user);
	Trader findByUsername(String username);
	Trader findByTraderId(int traderId);
	void removeTrader(Trader t);
	
}
