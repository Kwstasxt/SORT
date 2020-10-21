package com.mthree.daos;

import java.math.BigDecimal;

import com.mthree.models.Exchange;
import com.mthree.models.ExchangeMpid;
import com.mthree.models.Order;

public interface ExchangeDAO {
	
	BigDecimal calculateTodaysTradeValue(ExchangeMpid mpid);
	Exchange findExchange(ExchangeMpid exchangeMpid);
	ExchangeMpid findMpidForOrder(Order order);
}
