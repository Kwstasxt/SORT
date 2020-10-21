package com.mthree.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mthree.models.Transaction;
import com.mthree.repositories.OrderRepository;
import com.mthree.repositories.TradeRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.mthree.models.Exchange;
import com.mthree.models.ExchangeMpid;
import com.mthree.models.Order;
import com.mthree.models.OrderBook;
import com.mthree.models.Trade;

@Service
public class TransactionService {
    
    @Autowired
    private OrderRepository orderRepository;
   
    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private ExchangeService exchangeService;

    // TODO: performTransactions should be executeTrade in sort service
    /** 
     * Create transactoins based on Exchange orderbooks, trades will be completed based on best bidder first.
     * 
     * @param exchange
     * @return List<Transaction>
     */
	public List<Transaction> performTransactions(Exchange exchange) {

		List<Transaction> transactions = new ArrayList<>();
		Order target;  // lowest sell price of a seller order
        Order bestBidder; // best bid in the orderbook

		for(OrderBook orderbooks: exchange.getOrderBooks()) {
            
            bestBidder = exchangeService.findBestBidder(orderbooks);
            target = exchangeService.bestSpread(orderbooks.getOrders(), bestBidder);

            if (target != null) {
                if (target.getQuantity() > bestBidder.getQuantity()) {
                
                    target.setQuantity(target.getQuantity() - bestBidder.getQuantity());
                    
                    // record the new trade made
                    Trade t = new Trade();
                    t.setBuyOrder(bestBidder);
                    t.setSellOrder(target);
                    t.setTradeDate(LocalDateTime.now());

                    // save trade in the database.
                    tradeRepository.save(t);

                    Transaction trans = new Transaction();
                    trans.setExchangeMpid(exchange.getMpid());

                    bestBidder.setQuantity(0);
                        
                    // remove order from the database as this method runs
                    orderRepository.deleteById(bestBidder.getId());
                        
                    // notify user about the status of order.
                    exchangeService.notifyUserIfOrderComplete(bestBidder);
                
                } else { 
                    bestBidder.setQuantity(bestBidder.getQuantity() - target.getQuantity());
                    
                    Trade t = new Trade();
                    t.setBuyOrder(bestBidder);
                    t.setSellOrder(target);
                    t.setTradeDate(LocalDateTime.now());

                    // save trade into the database
                    tradeRepository.save(t);
                    
                    target.setQuantity(0);
                    
                    // remove order from the database as this method runs
                    orderRepository.deleteById(target.getId());
                    
                    // notify user about the status of order
                    exchangeService.notifyUserIfOrderComplete(bestBidder);
                }
            }
		}
		return transactions;
    }


    
    /** 
     * @param mpid
     * @return List<Transaction>
     */
    public List<Transaction> getTransactions(ExchangeMpid mpid) {

        Exchange exchange = exchangeService.findExchange(mpid);
        return performTransactions(exchange);
    }

}