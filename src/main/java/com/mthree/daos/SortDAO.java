package com.mthree.daos;

import java.util.List;
import java.util.Map;

import com.mthree.models.ExchangeMpid;
import com.mthree.models.OrderBook;
import com.mthree.models.Ric;
import com.mthree.models.Sort;
import com.mthree.models.Trade;
import com.mthree.models.Trader;

public interface SortDAO {
	
	void executeTrade();
	Map<Trade, List<ExchangeMpid>> tradePrice(Map<Trade, List<ExchangeMpid>> tempTrades);
	List<OrderBook> combineOrderBooks(Sort sort);
	Sort findSortForTrader(Trader trader);
	List<Trade> matchOrdersForRic(List<OrderBook> banksOrderBooks, List<OrderBook> exchangesOrderBooks, Ric ric);
}
