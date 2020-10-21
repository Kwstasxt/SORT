package com.mthree.utils;

import java.util.ArrayList;
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

    public Map<String, Object> stockInfoLoader(OrderBookDTO orderBook, Trader trader) {

        Sort sort = new Sort();

        Ric ric = Ric.values()[0];

        if (orderBook != null) {
            ric = orderBook.getRic();
        }
        if (trader != null) {
            sort = sortService.findSortForTrader(trader);
        }

        List<OrderBook> orderBooksForRic = orderBookService.getOrderbooksForRic(sort, ric);
        List<Order> buyOrdersForRic = orderBookService.getBuyOrders(orderBooksForRic);
        List<Order> sellOrdersForRic = orderBookService.getSellOrders(orderBooksForRic);

        Map<Trade, List<ExchangeMpid>> tempTrades = new HashMap<>();

        // TODO: remove
        int listSize = 0; 

        if (buyOrdersForRic.size() < sellOrdersForRic.size()) {
            listSize = buyOrdersForRic.size();
        } else {
            listSize = sellOrdersForRic.size();
        }

        // TODO: add
        // List<Trade> trades = sortService.performSort(...);
        // banks orderbooks
        // combined orderbooks
        // traders order??

        for (int i=0; i < listSize; i++) {
        // for (Trade trade : trades) {

            Trade trade = new Trade(); // remove
            Order buyOrder = buyOrdersForRic.get(i); // Order buyOrder = trade.getBuyOrder();
            Order sellOrder = sellOrdersForRic.get(i); // Order sellOrder = trade.getSellOrder();
            trade.setBuyOrder(buyOrder); // remove
            trade.setSellOrder(sellOrder); // remove

            ExchangeMpid buyOrderExchangeMpid = exchangeService.findMpidForOrder(buyOrder);
            ExchangeMpid sellOrderExchangeMpid = exchangeService.findMpidForOrder(sellOrder); 

            List<ExchangeMpid> mpids = new ArrayList<>();
            mpids.add(buyOrderExchangeMpid);
            mpids.add(sellOrderExchangeMpid);

            tempTrades.put(trade, mpids);
        }

        int totalOrders = orderBookService.calculateNumberOfOrders(orderBooksForRic);
        int totalVolume = orderBookService.calculateVolume(orderBooksForRic);

        Map<String, Object> stockInfo = new HashMap<>();
        
        stockInfo.put("tempTrades", tempTrades);
        stockInfo.put("totalOrders", totalOrders);
        stockInfo.put("totalVolume", totalVolume);

        return stockInfo;
    }
}
