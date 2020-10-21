package com.mthree.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.mthree.daos.ExchangeDAO;
import com.mthree.models.Exchange;
import com.mthree.models.ExchangeMpid;
import com.mthree.models.Fee;
import com.mthree.models.FeeType;
import com.mthree.models.Order;
import com.mthree.models.OrderBook;
import com.mthree.models.OrderType;
import com.mthree.models.Region;
import com.mthree.models.Trade;
import com.mthree.models.Transaction;
import com.mthree.repositories.ExchangeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExchangeService implements ExchangeDAO {
	
	@Autowired
	private ExchangeRepository exchangeRepository;
	
	@Autowired
	private OrderBookService orderBookService;

	private Random random = new Random();

	//TODO: Why Fee must be fixed for every exchange?
	public static final BigDecimal BULK_FEE = BigDecimal.valueOf(150.20);
	public static final BigDecimal NORMAL_FEE = BigDecimal.valueOf(330.52);

	// TODO: ask Konstantinos should this be trade or transaction? Should trade have a trade date?
	/**
	 * Sums value of all transactions that have taken place today.
	 *  
	 * @param exchangeId
	 * @return BigDecimal
	 */
	@Override
	public BigDecimal calculateTodaysTradeValue(ExchangeMpid mpid) {

		TransactionService tservice = new TransactionService();
		// get transactions that were recorded to an individual exchange.
		List<Transaction> totalTransactions = tservice.getTransactions(mpid);
		BigDecimal todaysTradeValue = BigDecimal.valueOf(0);
		
		for (Transaction trades : totalTransactions) {
			todaysTradeValue = todaysTradeValue.add(trades.getValueTraded());	
		}
		
		updateTodaysTradeValue(mpid, todaysTradeValue);
		
		return todaysTradeValue;
	}

	
	/** 
	 * @param mpid
	 * @param value
	 */
	public void updateTodaysTradeValue(ExchangeMpid mpid, BigDecimal value) {

		Exchange exchange = findExchange(mpid);

		exchange.setTodaysTradeValue(value);
		// merges changes to exchange in database
		exchangeRepository.save(exchange);
	}
	
	
	/** 
	 * @param exchangeMpid
	 * @return Exchange
	 */
	@Override
	public Exchange findExchange(ExchangeMpid exchangeMpid) {

		Optional<Exchange> exchange = exchangeRepository.findById(exchangeMpid.getNotation());

		if (exchange.isPresent()) {
			return exchange.get();
		}
		
		return null;
	}

	
	/** 
	 * Locates the exchange a given order is listed on and returns its MPID.
	 * 
	 * @param order
	 * @return ExchangeMpid
	 */
	@Override
	public ExchangeMpid findMpidForOrder(Order order) {

		ExchangeMpid mpid = null;

		List<Exchange> exchanges = exchangeRepository.findAll();

		for (Exchange exchange : exchanges) {
			for (OrderBook orderBook : exchange.getOrderBooks()) {
				for (Order exchangeOrder : orderBook.getOrders()) {
					if (order.equals(exchangeOrder)) {
						mpid = exchange.getMpid();
					}
				}
			}
		}

		return mpid;
	}
	
	
	
	/** 
	 * @param orders
	 * @param tradersOrder
	 * @return Order
	 */
	public Order bestSpread(List<Order> orders, Order tradersOrder) {
		
		Order targetOrder  = null;

		if (tradersOrder.getType() == OrderType.SELL) {
			
			BigDecimal bestSpread = new BigDecimal(Integer.MAX_VALUE);
			int comparator;
			BigDecimal buffer;
			BigDecimal orderPrice = tradersOrder.getPrice();

			for (Order o: orders) {
				if (o.getRic().equals(tradersOrder.getRic()) && o.getType() == OrderType.BUY && o.getQuantity() > 0) {
					comparator = orderPrice.compareTo(o.getPrice());
					// if comparator == 1 -> orderPrice > o.getPrice()
					// if comparator == 0 -> orderPrice == o.getPrice()
					// if comparator == -1 -> orderPrice < o.getPrice()
					if (comparator == -1) {
						buffer = o.getPrice().subtract(orderPrice);
						//buffer > bestSpread
						if (buffer.compareTo(bestSpread) > 0) {
							bestSpread = buffer;
							targetOrder = o;
						}
					}
				}		
			}

		} else { //Traders OrderType == BUY
			
			BigDecimal bestSpread = new BigDecimal(Integer.MIN_VALUE);
			int comparator;
			BigDecimal buffer;
			BigDecimal orderPrice =  tradersOrder.getPrice();
			
			for (Order o: orders) {
				if (o.getRic().equals(tradersOrder.getRic()) && o.getType() == OrderType.SELL && o.getQuantity() > 0) {
					comparator = orderPrice.compareTo(o.getPrice());
					// if comparator == 1 -> orderPrice > o.getPrice()
					// if comparator == 0 -> orderPrice == o.getPrice()
					// if comparator == -1 -> orderPrice < o.getPrice()
					if (comparator == 1) {
						buffer = orderPrice.subtract(o.getPrice());
						// buffer > bestSpread
						if (buffer.compareTo(bestSpread) > 0) {
							bestSpread = buffer;
							targetOrder = o;
						}
					}
				}		
			}
		}
	
		return targetOrder;
	}
	
	
	/** 
	 * @param orderList
	 * @param quantityThreshold
	 * @return List<Fee>
	 */
	public List<Fee> determineFee(Exchange tradeList, int quantityThreshold){
		
		List<Fee> feeLadder = new ArrayList<>();

		// if quantity is larger than the quantity threshold then return the bulk fee
		for (Trade orderItem: tradeList.getTrades()) {

			Fee fee = new Fee();

			if (orderItem.getBuyOrder().getQuantity() > quantityThreshold) {
				fee.setType(FeeType.BULK);
				fee.setValue(BULK_FEE);
				feeLadder.add(fee);
			} else {
				fee.setType(FeeType.NORMAL);
				fee.setValue(NORMAL_FEE);
				feeLadder.add(fee);
			}
		}
		
		return feeLadder;	
	}
	
	
	/** 
	 * Best bidder must purchase first.
	 * 
	 * @param ordersBookList
	 * @return Order
	 */
	public Order findBestBidder(OrderBook ordersBookList) {
		
		BigDecimal bestBid = new BigDecimal(Integer.MIN_VALUE);
		Order bestBidderOrder = null;

		for (Order order: ordersBookList.getOrders()) {
			// if quantity for an order = 0 then this bidder has already purchased all the stocks that they want
			if (order.getType() == OrderType.BUY && order.getQuantity() > 0 && order.getPrice().compareTo(bestBid) > 0) {
				bestBid = order.getPrice();
				bestBidderOrder = order;
			}
		}
		
		return bestBidderOrder;		
	}
	
	
	/** 
	 * @param completedOrder
	 * @return String
	 */
	public String notifyUserIfOrderComplete(Order completedOrder) {
		
		//completedOrderNotification -> .jsp page
		//partiallyCompletedOrder -> .jsp page

		String status = "";

		if (completedOrder.getQuantity() == 0) {
			status =  "completedOrderNotification";
		}
		
		if (completedOrder.getQuantity() > 0) {
			status =  "partiallyCompletedOrder";
		}

		return status;
	}


	/** 
	 * Creates exchanges using orderBookService.generateRandomOrders() method for Sort.
	 * 
	 * @return List<Exchange>
	 */	
	public List<Exchange> generateExchanges(Region sortRegion) {

		List<Exchange> exchanges = new ArrayList<>();
		int [] quantityThresholds = {100000, 6000, 20000, 15000};
		int thresholdIndex = random.nextInt(quantityThresholds.length);
		ArrayList<ExchangeMpid> mpids = new ArrayList<>();

		if (sortRegion == Region.AMER) {
			mpids.add(ExchangeMpid.NASDAQ);
			mpids.add(ExchangeMpid.NEW_YORK_STOCK_EXCHANGE);
			mpids.add(ExchangeMpid.TORONTO_STOCK_EXCHANGE);
		} else if (sortRegion == Region.APAC) {
			mpids.add(ExchangeMpid.HONG_KONG_STOCK_EXCHANGE);
			mpids.add(ExchangeMpid.JAPAN_EXCHANGE_GROUP);
			mpids.add(ExchangeMpid.SHANGHAI_STOCK_EXCHANGE);
		} else {
			mpids.add(ExchangeMpid.LONDON_STOCK_EXCHANGE);
			mpids.add(ExchangeMpid.SWISS_EXCHANGE);
		}

		for (ExchangeMpid mpid : mpids) {
			
			Exchange exchange = new Exchange();
			
			exchange.setMpid(mpid);
			exchange.setOrderBooks(orderBookService.generateRandomOrders());
			exchange.setFeeLadder(generateFees());
			exchange.setTodaysTradeValue(BigDecimal.valueOf(0));
			exchange.setShareQuantityThreshold(quantityThresholds[thresholdIndex]);
			// save to the database
			exchangeRepository.save(exchange);
			
			exchanges.add(exchange);
		}	
			
		return exchanges;
	}	
			
	
	/** 
	 * @return List<Fee>
	 */
	public List<Fee> generateFees() {

		List<Fee> exchangeFees = new ArrayList<>();

		for (FeeType feeType : FeeType.values()) {
			Fee fee = new Fee();
			fee.setType(feeType);
			BigDecimal feePrice = generateFeePrice(feeType);
			fee.setValue(feePrice); 

			exchangeFees.add(fee);
		}
		
		return exchangeFees;
	}

	
	/** 
	 * @param feeType
	 * @return BigDecimal
	 */
	public BigDecimal generateFeePrice(FeeType feeType) {

		int base = 150;
		double modifier = (Math.random() * 2);

		if (feeType == FeeType.BULK) {
			 modifier = Math.random();
		}

		return BigDecimal.valueOf(base * modifier);
	}

	
	/** 
	 * @param exchangeID
	 * @return List<Fee>
	 */
	public List<Fee> getExchangeFee(ExchangeMpid exchangeMpid) {
		return findExchange(exchangeMpid).getFeeLadder();
	}



	public List<Exchange> findExchanges(){
		return exchangeRepository.findAll();
	}

}
