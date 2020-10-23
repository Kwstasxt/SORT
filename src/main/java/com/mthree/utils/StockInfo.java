package com.mthree.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mthree.dtos.OrderBookDTO;
import com.mthree.models.OrderBook;
import com.mthree.models.ExchangeMpid;
import com.mthree.models.Order;
import com.mthree.models.Ric;
import com.mthree.models.Sort;
import com.mthree.models.Trade;
import com.mthree.models.Trader;
import com.mthree.services.ExchangeService;
import com.mthree.services.OrderBookService;
import com.mthree.services.SortService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StockInfo {

    @Autowired
    private OrderBookService orderBookService;

    @Autowired
    private SortService sortService;

    @Autowired
    private ExchangeService exchangeService;

    Ric ric;
    private Sort sort;
    private List<OrderBook> banksOrderBooks;
    private List<OrderBook> exchangesOrderBooks;
    List<Order> tradersOrders;

    
    /** 
     * Returns info required to populate table on home page.
     * 
     * @param orderBook
     * @param trader
     * @return Map<String, Object>
     */
    public Map<String, Object> stockInfoLoader(OrderBookDTO orderBook, Trader trader) { 

        long start = System.currentTimeMillis();

        if (orderBook != null) {
            ric = orderBook.getRic();
        } else {
            ric = Ric.values()[0];
        }

        if (trader != null) {
            sort = sortService.findSortForTrader(trader);
            banksOrderBooks = sort.getOrderBooks();
            exchangesOrderBooks = sortService.combineOrderBooks(sort);
            tradersOrders = trader.getOrders();
        } 

        Map<Trade, List<ExchangeMpid>> tempTrades = new HashMap<>(); 

        // find matching orders
         List<Trade> trades = sortService.matchOrdersForRic(banksOrderBooks, exchangesOrderBooks, ric);

        // avoid natural ordering being used
        //Trade[] sortedTrades = sortTrades(trades);

        // find exchange mpids and add to map
        trades.stream().forEach(trade -> {
            Order buyOrder = trade.getBuyOrder();
            Order sellOrder = trade.getSellOrder();

            if (buyOrder.getPrice().compareTo(sellOrder.getPrice()) > 0) {
                ExchangeMpid buyOrderExchangeMpid = exchangeService.findMpidForOrder(buyOrder);
                ExchangeMpid sellOrderExchangeMpid = exchangeService.findMpidForOrder(sellOrder); 
                List<ExchangeMpid> mpids = new ArrayList<>();
                mpids.add(buyOrderExchangeMpid);
                mpids.add(sellOrderExchangeMpid);
    
                tempTrades.put(trade, mpids);
            }
        });

        List<OrderBook> orderBooksForRic = orderBookService.getOrderbooksForRic(sort, ric);
        int totalOrders = orderBookService.calculateNumberOfOrders(orderBooksForRic);
        int totalVolume = orderBookService.calculateVolume(orderBooksForRic);

        Map<String, Object> stockInfo = new HashMap<>();
        
        stockInfo.put("tempTrades", tempTrades);
        stockInfo.put("totalOrders", totalOrders);
        stockInfo.put("totalVolume", totalVolume);

        long time = System.currentTimeMillis() - start;
        System.out.println("\n\n\n*************** time taken : " + time + "\n\n\n");

        return stockInfo;
    }


    
    /** 
     * Find matches for the trader's orders. 
     * 
     * @param ric
     * @return List<Trade>
     */
    private List<Trade> findMatchingOrders() {

        List<Trade> trades = new ArrayList<>();

        tradersOrders.stream().forEach(tradersOrder -> {

            System.out.println("************* trader order: " + tradersOrder);

            String currentRic = ric.getNotation();
            String tradersOrderRic = tradersOrder.getRic().getNotation();
            
            if (tradersOrderRic.equals(currentRic)) {

                Trade match = sortService.matchOrder(banksOrderBooks, exchangesOrderBooks, tradersOrder);

                // if a match has been found
                if (match.getBuyOrder() != null && match.getSellOrder() != null) {
                    trades.add(match);
                }
            }
        });

        return trades;
    }


    
    /** 
     * Sort trades based on minimium spread.
     * 
     * @param trades
     * @return List<Trade>
     */
    private Trade[] sortTrades(List<Trade> trades) {
        
        Comparator<Trade> tradeSorter = (Trade trade1, Trade trade2) -> {
            
            BigDecimal trade1Spread = (trade1.getBuyOrder().getPrice().subtract(trade1.getSellOrder().getPrice())).abs();
            BigDecimal trade2Spread = (trade2.getBuyOrder().getPrice().subtract(trade2.getSellOrder().getPrice())).abs();

            return trade2Spread.compareTo(trade1Spread);
        };

        trades.sort(tradeSorter);

        return trades.toArray(new Trade[trades.size()]);
    }
}
