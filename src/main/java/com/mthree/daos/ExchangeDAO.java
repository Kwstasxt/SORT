package com.mthree.daos;

import java.math.BigDecimal;

import com.mthree.models.Exchange;

public interface ExchangeDAO {
	
	BigDecimal calculateTodaysTradeValue(int exchangeId);
	Exchange findExchange(int exchangeId);
}
