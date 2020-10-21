package com.mthree.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.mthree.models.Exchange;
import com.mthree.models.Order;
import com.mthree.models.OrderBook;
import com.mthree.models.OrderType;
import com.mthree.models.Region;
import com.mthree.models.Ric;
import com.mthree.models.Sort;
import com.mthree.daos.SortDAO;
import com.mthree.models.Trade;
import com.mthree.models.Trader;
import com.mthree.repositories.SortRepository;

@Service
public class SortService implements SortDAO {

	@Autowired
	private SortRepository sortRepository;

	@Autowired
	private ExchangeService exservice;

	@Autowired
	private TransactionService tservice;

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private TradeService tradeservice;
	

	//Store in this instance all the values of orderbooks.
	public List<OrderBook> bufferOrderBook;

	//Get this Trader from the front-end.
	public Trader traderInstance;

	public Sort tradersSort;

	public boolean isRunningOnce = true;


	@Override
	public void executeTrade() {
		
	
		//traderInstance: is the trader currently logged in. Get this from the front-end.
		
		
		if (isRunningOnce){
			
			//Switch state of isRunningOnce in order not to perform the retrieval again by the DB.
			//For Faster results.
			isRunningOnce = false;

			 tradersSort = findSortForTrader(traderInstance);

			 //Generate combined Orderbooks for this Sort.	
			 bufferOrderBook = combineOrderBooks(tradersSort);	
		}

		
		
		//TODO: Could modify combinedOrderBooks to store Banks orderbook into index = 0;
		OrderBook banksOrderBook = bufferOrderBook.get(0);
		List<OrderBook> exchangesOrderbooks = bufferOrderBook;
		//Remove index 0. It is the banks orderbook.
		exchangesOrderbooks.remove(0);

		//Perform SORT for this Specific Trader. Call matchTradersOrders method to find the best match orders 
		//for all of traders orders.
		List<Trade> tradersTrades = null;
		for (Order tradersOrders : traderInstance.getOrders()){
			tradersTrades = performSort(banksOrderBook, exchangesOrderbooks, tradersOrders);
		}

		
		 //Displays the tradersTrades?
		
		
		
	
	}	

	
	/** 
	 * Find all sort instances.
	 * 
	 * 
	 * @return List<Sort>
	 */
	public List<Sort> retrieveSortFromDB() {
		return sortRepository.findAll();
	}
	
	
	
	// TODO: add 'bank's' orderbooks
	// TODO: sort orderbooks based on price and submit time
	/**
	 * For each orderbook in each exchange listed in the sort class, collate orders
	 * for a given instrument.
	 * 
	 * @param sort
	 * @return List<OrderBook>
	 */
	@Override
	public List<OrderBook> combineOrderBooks(Sort sort) {

		List<OrderBook> combinedOrderBooks = new ArrayList<>();

		// for all rics in enum type
		for (Ric instrument : Ric.values()) {

			OrderBook instrumentOrderBook = new OrderBook();
			instrumentOrderBook.setRic(instrument);

			List<Order> instrumentOrders = new ArrayList<>();

			// for same ric add all orders to one orderbook
			for (Exchange exchange : sort.getExchanges()) {
				for (OrderBook orderBook : exchange.getOrderBooks()) {

					String currentRic = instrument.getNotation();
					String orderBookRic = orderBook.getRic().getNotation();

					if (orderBookRic.equals(currentRic)) {
						instrumentOrders.addAll(orderBook.getOrders());
					}
				}
			}

			instrumentOrderBook.setOrders(instrumentOrders);
			combinedOrderBooks.add(instrumentOrderBook);
		}
		//Use this to store orders.
		//bufferOrderBook = combinedOrderBooks;
		
		return combinedOrderBooks;
	}

	/**
	 * Create a Sort Object in order to combine all exchanges into a single object.
	 * 
	 * @return List<Sort>
	 */
	public List<Sort> createSort() {

		List<Sort> sortInstances = new ArrayList<>();

		for (Region region : Region.values()) {

			Sort sort = new Sort();
			sort.setExchanges(exservice.generateExchanges(region));
			sort.setRegion(region);
			sortInstances.add(sort);
			
			// save to the db
			sortRepository.save(sort);
		}

		return sortInstances;
	}

	// TODO: not using value?
	//TODO: Use fees for exchanges.
	/**
	 * When a new order arrives SORT will search for an exchange orderbook that has
	 * the lowest price for this order.
	 * 
	 * @param banksOrderBook
	 * @param combinedOrderBook
	 * @param tradersOrder
	 * @return List<Trade>
	 */
	public List<Trade> performSort(OrderBook banksOrderBook, List<OrderBook> combinedOrderBook, Order tradersOrder) {

		List<Trade> trades = new ArrayList<>();
		//Each exchange has its fees when buying bulk/Normal and its prices.
		//When a new order arrives SORT will perform the trade searching for an exchange orderbook that has the lowest price for this order.
		
		//Best Spread target. Or lowest/highest sell/buy order that match this order.
		Order target; 

		for (Order ordersWithinBank : banksOrderBook.getOrders()){
			 
			 target = exservice.bestSpread(banksOrderBook.getOrders(), tradersOrder);
			  
			 //Find the target's index in the Ordebook to make the changes.
			  int index = banksOrderBook.getOrders().indexOf(target);

			 //Target has enough quantity to complete the trade.
			  if (target.getQuantity()> tradersOrder.getQuantity()){
				

				//--------------------------------------------------
				//Remove current Order from the DB. It is completed.
				int orderId = tradersOrder.getId();
				orderService.deleteOrderByID(orderId);

				int partiallyCompletedID = target.getId();
				int modifiedQuantity = target.getQuantity() - tradersOrder.getQuantity();
				orderService.updateOrderQuantityByID(partiallyCompletedID, modifiedQuantity);

				//---------------------------------------------
				
			    banksOrderBook.getOrders().get(index).setQuantity(modifiedQuantity);
				tradersOrder.setQuantity(0);

				
				
				
				// traders Order completed

				Trade transaction = new Trade();
				if (tradersOrder.getType()==OrderType.BUY){
					transaction.setBuyOrder(tradersOrder);
					transaction.setSellOrder(target);
				}else{
					transaction.setBuyOrder(target);
					transaction.setSellOrder(tradersOrder);
				}
				
				//--------------------------------------------------
				//Save Trade into the DB.
				tradeservice.saveTradeIntoDB(transaction);
				//--------------------------------------------------

				trades.add(transaction);
				exservice.notifyUserIfOrderComplete(tradersOrder);
				
				return trades;

			} else {
				//--------------------------------------------------
				int orderId = target.getId();
				//Delete target Order from DB.
				orderService.deleteOrderByID(orderId);
				//Update Order quantity.
				int partiallyCompletedID = tradersOrder.getId();
				int modifiedQuantity = tradersOrder.getQuantity() - target.getQuantity();
				orderService.updateOrderQuantityByID(partiallyCompletedID, modifiedQuantity);

				//--------------------------------------------------

				tradersOrder.setQuantity(tradersOrder.getQuantity() - target.getQuantity());
				banksOrderBook.getOrders().get(index).setQuantity(0);

				exservice.notifyUserIfOrderComplete(tradersOrder);
			}
		}

		// If even after searching Banks Orderbook to see if there is a good match for
		// this particular order
		// did not returned any succesfull trade, then perform SORT across all
		// Exchanges.

		OrderType tradersType = tradersOrder.getType();
		BigDecimal bestMatch = null;
		Order bestOrder = null;
		OrderBook orderBookMatch = null;

		if (tradersType.equals(OrderType.BUY)) {
			bestMatch = new BigDecimal(Integer.MAX_VALUE);
		} else {
			bestMatch = new BigDecimal(Integer.MIN_VALUE);
		}

		while (tradersOrder.getQuantity() > 0) {
			for (OrderBook co : combinedOrderBook) {
				
				target = exservice.bestSpread(co.getOrders(), tradersOrder);

				// search all exchange orderbooks
				if (target != null) {
					if (tradersType.equals(OrderType.BUY)) {
						if (target.getPrice().compareTo(bestMatch) < 0) {
							bestMatch = target.getPrice();
							bestOrder = target;
							orderBookMatch = co;
						}
					} else {
						if (target.getPrice().compareTo(bestMatch) > 0) {
							bestMatch = target.getPrice();
							bestOrder = target;
							orderBookMatch = co;
						}
					}
				}
			}

			if (bestOrder.getQuantity() > tradersOrder.getQuantity()) {
				// traders order completed
				tradersOrder.setQuantity(0);

				//----------------------------
				//Update the DB.
				int orderID = tradersOrder.getId();
				orderService.deleteOrderByID(orderID);

				int modifiedOrderID = bestOrder.getId();
				int modifiedQuantity = bestOrder.getQuantity()-tradersOrder.getQuantity();
				orderService.updateOrderQuantityByID(modifiedOrderID, modifiedQuantity);
				//----------------------------

				Trade transaction = new Trade();
				transaction.setBuyOrder(tradersOrder);
				transaction.setSellOrder(bestOrder);

				//----------------------------
				//Save trade into the DB.
				tradeservice.saveTradeIntoDB(transaction);
				//----------------------------
				
				trades.add(transaction);

				//Notify the user that this order is COMPLETED!
				exservice.notifyUserIfOrderComplete(tradersOrder);

				return trades;

			} else {

				//----------------------------
				//Update the DB.
				int orderID = bestOrder.getId();
				orderService.deleteOrderByID(orderID);

				int modifiedOrderID = tradersOrder.getId();
				int modifiedQuantity = tradersOrder.getQuantity()-bestOrder.getQuantity();
				orderService.updateOrderQuantityByID(modifiedOrderID, modifiedQuantity);
				//----------------------------

				int index = orderBookMatch.getOrders().indexOf(bestOrder);
				orderBookMatch.getOrders().get(index).setQuantity(0);
				tradersOrder.setQuantity(tradersOrder.getQuantity()-bestOrder.getQuantity());
				
				exservice.notifyUserIfOrderComplete(tradersOrder);
			}
		}



		return trades;
	}

	
	//Find the best match for traders orders based across bank and Exchanges orderbooks.
	public List<Trade> matchTradersOrders(OrderBook banksOrderBook, List<OrderBook> combinedOrderBook, List<Trade> tradersOrders ){
		List<Trade> trades = new ArrayList<>();
	
		List<Trade> matchBuy = null;
		List<Trade> matchSell = null;

		//Trade tr = new Trade(123, buyOrder, sellOrder, tradeDate);
		for (Trade trade : tradersOrders) {

			Order buyOrder = trade.getBuyOrder();
			Order sellOrder = trade.getSellOrder();

			if (buyOrder != null) {
			//if found a match call 	
				 matchBuy =  performSort(banksOrderBook, combinedOrderBook, buyOrder);

			}

			if (sellOrder != null) {
			    matchSell = performSort(banksOrderBook, combinedOrderBook, sellOrder);
			}
			

			if (matchBuy.isEmpty()) {
				for (Trade buyMatch : matchBuy){
					trades.add(buyMatch);
				}
			}

			if (matchSell.isEmpty()) {
				for (Trade sellMatch : matchSell){
					trades.add(sellMatch);
				}
			}

			


		}
		
		
		

		return trades;

	}

	
	/** 
	 * Find the trader's regional instance of sort.
	 * 
	 * @param trader
	 * @return Sort
	 */
	@Override
	public Sort findSortForTrader(Trader trader) {

		List<Sort> orderRouters = sortRepository.findAll();

		if (orderRouters.isEmpty()) {
			orderRouters = createSort();
		}

		for (Sort sort : orderRouters) {
			if (sort.getRegion() == trader.getRegion()) {
				return sort;
			}
		}

		return null;
	}

}
