package com.mthree.daos;

import java.util.List;

import com.mthree.models.OrderBook;
import com.mthree.models.Sort;
import com.mthree.models.Trader;

public interface SortDAO {
	
	void executeTrade();
	List<OrderBook> combineOrderBooks(Sort sort);
	Sort findSortForTrader(Trader trader);
}
