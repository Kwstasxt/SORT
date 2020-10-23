package com.mthree.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mthree.models.Exchange;
import com.mthree.models.ExchangeMpid;
import com.mthree.models.Fee;
import com.mthree.models.Order;
import com.mthree.models.OrderBook;
import com.mthree.models.OrderType;
import com.mthree.models.Region;
import com.mthree.models.Ric;
import com.mthree.models.Sort;
import com.mthree.models.Trade;
import com.mthree.daos.SortDAO;
import com.mthree.models.Trader;
import com.mthree.repositories.SortRepository;

@Service
public class SortService implements SortDAO {

	@Autowired
	private SortRepository sortRepository;

	@Autowired
	private ExchangeService exservice;

	@Autowired
	private OrderService orderService;

	@Autowired
	private TradeService tradeservice;

	@Autowired
	private TraderService traderService;

	/**
	 * @param Map<Trade
	 * @param tempTrades
	 * @return Map<Trade, List<ExchangeMpid>>
	 */
	// //Store in this instance all the values of orderbooks.
	// public List<OrderBook> bufferOrderBook;

	// //Get this Trader from the front-end.
	// public Trader traderInstance;

	// public Sort tradersSort;

	// public boolean isRunningOnce = true;

	@Override
	public Map<Trade, List<ExchangeMpid>> tradePrice(Map<Trade, List<ExchangeMpid>> tempTrades) {
		// Compares prices of two orders and is used in OrderBookController to deem the
		// trade as executable or not
		BigDecimal tradePrice = null;
		for (Map.Entry<Trade, List<ExchangeMpid>> trade : tempTrades.entrySet()) {
			BigDecimal buyPrice = trade.getKey().getBuyOrder().getPrice();
			BigDecimal sellPrice = trade.getKey().getSellOrder().getPrice();
			int res = buyPrice.compareTo(sellPrice);

			if (res < 0) {
				tradePrice = null;
			} // sell>buy: therefore trade not allowed to be executed
			if (res == 0) {
				tradePrice = (sellPrice.add(buyPrice)).divide(new BigDecimal(2));
			} // sell==buy: trade can be executed
			if (res > 0) {
				tradePrice = (sellPrice.add(buyPrice)).divide(new BigDecimal(2));
			} // buy>sell: trade can be executed

			trade.getKey().getBuyOrder().setPrice(tradePrice);
			trade.getKey().getSellOrder().setPrice(tradePrice);

		}
		return tempTrades;

	}

	public void executeTrade() {

		// //traderInstance: is the trader currently logged in. Get this from the
		// front-end.

		// if (isRunningOnce){

		// //Switch state of isRunningOnce in order not to perform the retrieval again
		// by the DB.
		// //For Faster results.
		// isRunningOnce = false;

		// tradersSort = findSortForTrader(traderInstance);

		// //Generate combined Orderbooks for this Sort.
		// bufferOrderBook = combineOrderBooks(tradersSort);
		// }

		// OrderBook banksOrderBook = bufferOrderBook.get(0);
		// List<OrderBook> exchangesOrderbooks = bufferOrderBook;
		// //Remove index 0. It is the banks orderbook.
		// exchangesOrderbooks.remove(0);

		// //Perform SORT for this Specific Trader. Call matchTradersOrders method to
		// find the best match orders
		// //for all of traders orders.
		// List<Trade> tradersTrades = null;
		// for (Order tradersOrders : traderInstance.getOrders()){
		// tradersTrades = performSort(banksOrderBook, exchangesOrderbooks,
		// tradersOrders);
		// }

		// //Displays the tradersTrades?

	}

	@Override
	public List<Trade> matchOrdersForRic(List<OrderBook> banksOrderBooks, List<OrderBook> exchangesOrderBooks,
			Ric ric) {

		Set<Order> combinedOrders = new HashSet<>();

		banksOrderBooks.stream().forEach(bankOrderBook -> {
			if (ric.equals(bankOrderBook.getRic())) {
				combinedOrders.addAll(bankOrderBook.getOrders());
			}
		});

		exchangesOrderBooks.stream().forEach(exchangeOrderBook -> {
			if (ric.equals(exchangeOrderBook.getRic())) {
				combinedOrders.addAll(exchangeOrderBook.getOrders());
			}
		});

		return bestSpread(combinedOrders, ric);
	}

	/**
	 * @param orders
	 * @param ric
	 * @return List<Trade>
	 */
	private List<Trade> bestSpread(Set<Order> orderSet, Ric ric) {

		List<Order> orders = new ArrayList<>();
		orders.addAll(orderSet);

		List<Trade> trades = new ArrayList<>();

		boolean bidMatch = false;
		boolean askMatch = false;

		// loop until no match is found
		do {

			bidMatch = false;
			askMatch = false;

			Order highestBidOrder = new Order();
			BigDecimal maxBidPrice = BigDecimal.valueOf(Integer.MIN_VALUE);
			Order lowestAskOrder = new Order();
			BigDecimal minAskPrice = BigDecimal.valueOf(Integer.MAX_VALUE);

			for (Order order : orders) {

				if (order.getRic().equals(ric) && order.getQuantity() > 0) {

					// new highest bid price
					if (order.getType() == OrderType.BUY && order.getPrice().compareTo(maxBidPrice) > 0) {

						maxBidPrice = order.getPrice();
						highestBidOrder = order;
						bidMatch = true;

					}
					// new lowest ask price
					else if (order.getType() == OrderType.SELL && order.getPrice().compareTo(minAskPrice) < 0) {

						minAskPrice = order.getPrice();
						lowestAskOrder = order;
						askMatch = true;

					}
				}
			}

			if (bidMatch && askMatch) {
				// add to trades
				Trade trade = new Trade();
				trade.setBuyOrder(highestBidOrder);
				trade.setSellOrder(lowestAskOrder);
				trades.add(trade);

				// remove from copy of list

				int index = orders.indexOf(highestBidOrder);
				orders.remove(index);

				int indexLowestAskOrder = orders.indexOf(lowestAskOrder);
				orders.remove(indexLowestAskOrder);

				System.out.println(orders.size());
			}

		} while (bidMatch && askMatch);

		return trades;
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
		Arrays.stream(Ric.values()).forEach(instrument -> {

			OrderBook instrumentOrderBook = new OrderBook();
			instrumentOrderBook.setRic(instrument);

			List<Order> instrumentOrders = new ArrayList<>();

			// for same ric add all orders to one orderbook
			sort.getExchanges().stream().forEach(exchange -> exchange.getOrderBooks().stream().forEach(orderBook -> {
				String currentRic = instrument.getNotation();
				String orderBookRic = orderBook.getRic().getNotation();

				if (orderBookRic.equals(currentRic)) {
					instrumentOrders.addAll(orderBook.getOrders());
				}
			}));

			instrumentOrderBook.setOrders(instrumentOrders);
			combinedOrderBooks.add(instrumentOrderBook);
		});

		return combinedOrderBooks;
	}

	/**
	 * Create a Sort Object in order to combine all exchanges into a single object.
	 * 
	 * @return List<Sort>
	 */
	public List<Sort> createSort() {

		List<Sort> sortInstances = new ArrayList<>();

		Arrays.stream(Region.values()).forEach(region -> {
			Sort sort = new Sort();
			sort.setExchanges(exservice.generateExchanges(region));
			sort.setRegion(region);
			sortInstances.add(sort);

			// save to the db
			sortRepository.save(sort);
		});

		return sortInstances;
	}

	/**
	 * Matches a given order to an order returned by the bestSpead function and
	 * creates a Trade.
	 * 
	 * @param banksOrderBooks
	 * @param exchangesOrderBooks
	 * @param traderOrder
	 * @return Trade
	 */
	public Trade matchOrder(List<OrderBook> banksOrderBooks, List<OrderBook> exchangesOrderBooks, Order tradersOrder) {

		List<Order> combinedOrders = new ArrayList<>();

		for (OrderBook bankOrderBook : banksOrderBooks) {
			combinedOrders.addAll(bankOrderBook.getOrders());
		}
		for (OrderBook exchangeOrderBook : exchangesOrderBooks) {
			combinedOrders.addAll(exchangeOrderBook.getOrders());
		}

		Order match = exservice.bestSpread(combinedOrders, tradersOrder);

		Trade trade = new Trade();

		if (tradersOrder.getType() == OrderType.BUY) {
			trade.setBuyOrder(tradersOrder);
			trade.setSellOrder(match);
		} else {
			trade.setBuyOrder(match);
			trade.setSellOrder(tradersOrder);
		}

		return trade;
	}

	// TODO: Use fees for exchanges.
	/**
	 * When a new order arrives SORT will search for an orderbook that has the
	 * lowest price for this order.
	 * 
	 * @param banksOrderBook
	 * @param combinedOrderBook
	 * @param tradersOrder
	 * @return List<Trade>
	 */
	public List<Trade> performSort(List<OrderBook> banksOrderBooks, List<OrderBook> combinedOrderBook,
			Order tradersOrder) {

		List<Trade> trades = new ArrayList<>();
		// Each exchange has its fees when buying bulk/Normal and its prices.
		// When a new order arrives SORT will perform the trade searching for an
		// exchange orderbook that has the lowest price for this order.

		// Best Spread target. Or lowest/highest sell/buy order that match this order.
		Order target;

		for (OrderBook banksOrderBook : banksOrderBooks) {
			for (int i = 0; i < banksOrderBook.getOrders().size(); i++) {

				target = exservice.bestSpread(banksOrderBook.getOrders(), tradersOrder);

				// find the target's index in the Ordebook to make the changes
				int index = banksOrderBook.getOrders().indexOf(target);

				// target has enough quantity to complete the trade
				if (target.getQuantity() > tradersOrder.getQuantity()) {

					// traders order completed
					// removeOrderFromTrader(tradersOrder);
					completeOrder(tradersOrder, target);

					int modifiedQuantity = target.getQuantity() - tradersOrder.getQuantity();
					banksOrderBook.getOrders().get(index).setQuantity(modifiedQuantity);

					return trade(trades, tradersOrder, target);

				} else {

					completeOrder(target, tradersOrder);
					updateQuantities(banksOrderBook, tradersOrder, target);
					exservice.notifyUserIfOrderComplete(tradersOrder);
				}
			}
		}

		// if after searching banks orderbook to see if there is a good match for
		// this particular order did not return any succesfull trade, then perform SORT
		// across all
		// exchanges.

		OrderType tradersType = tradersOrder.getType();
		BigDecimal bestMatch;
		Order bestOrder = new Order();
		OrderBook orderBookMatch = new OrderBook();

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
				// removeOrderFromTrader(tradersOrder);
				completeOrder(tradersOrder, bestOrder);
				return trade(trades, tradersOrder, bestOrder);

			} else {

				completeOrder(bestOrder, tradersOrder);
				updateQuantities(orderBookMatch, tradersOrder, bestOrder);
				exservice.notifyUserIfOrderComplete(tradersOrder);
			}
		}

		return trades;
	}

	/**
	 * Remove current Order from the DB as it is completed.
	 * 
	 * @param tradersOrder
	 * @param target
	 */
	public void completeOrder(Order order1, Order order2) {
		int partiallyCompletedID = order2.getId();
		int modifiedQuantity = order2.getQuantity() - order1.getQuantity();
		orderService.updateOrderQuantityByID(partiallyCompletedID, modifiedQuantity);
	}

	/**
	 * @param tradersOrder
	 */
	public void removeOrderFromTrader(Order tradersOrder) {
		Trader trader = traderService.findByOrder(tradersOrder);
		List<Order> tradersOrders = trader.getOrders();
		// tradersOrders.remove(tradersOrder);
		trader.setOrders(tradersOrders);
	}

	/**
	 * @param trades
	 * @param order1
	 * @param order2
	 * @return List<Trade>
	 */
	public List<Trade> trade(List<Trade> trades, Order order1, Order order2) {

		Trade trade = new Trade();
		order1.setQuantity(0);

		if (order1.getType() == OrderType.BUY) {
			trade.setBuyOrder(order1);
			trade.setSellOrder(order2);
		} else {
			trade.setBuyOrder(order2);
			trade.setSellOrder(order1);
		}

		tradeservice.saveTradeIntoDB(trade);

		trades.add(trade);
		exservice.notifyUserIfOrderComplete(order1);

		return trades;
	}

	/**
	 * @param banksOrderBook
	 * @param index
	 * @param order1
	 * @param order2
	 */
	public void updateQuantities(OrderBook orderBook, Order order1, Order order2) {
		int index = orderBook.getOrders().indexOf(order2);
		int modifiedQuantity = order1.getQuantity() - order2.getQuantity();
		order1.setQuantity(modifiedQuantity);
		orderBook.getOrders().get(index).setQuantity(0);
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


	/**
	 * Find the correct fee for a trade.
	 * 
	 * @param trader
	 * @return Sort
	 */
	public BigDecimal findFeeForTradersTrade(Order o, Order tradersOrder) {
		BigDecimal fee = null;

		ExchangeMpid mpid = exservice.findMpidForOrder(o);
		Exchange tempExc = exservice.findExchange(mpid);
		List<Fee> excFee = tempExc.getFeeLadder();
		if (tradersOrder.getQuantity() > tempExc.getShareQuantityThreshold()) {
			fee = excFee.get(0).getValue();
		} else {
			fee = excFee.get(1).getValue();
		}

		return fee;

	}

}
