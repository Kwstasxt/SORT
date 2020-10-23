package com.mthree.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mthree.dtos.OrderBookDTO;
import com.mthree.dtos.OrderDTO;
import com.mthree.models.ExchangeMpid;
import com.mthree.models.Order;
import com.mthree.models.OrderBook;
import com.mthree.models.Trade;
import com.mthree.models.TraderUserDetails;
import com.mthree.services.OrderBookService;
import com.mthree.services.SortService;
import com.mthree.utils.StockInfo;

@Controller
@SessionAttributes({ "trader", "tempTrades", "orderBook", "executedTrades", "sortExchanges" })
public class OrderBookController {

    @Autowired
    private OrderBookService orderBookService;

    @Autowired
    private SortService sortService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private StockInfo stockInfo;

    
    /** 
     * @return TraderUserDetails
     */
    @ModelAttribute("trader")
    public TraderUserDetails trader() {
        return new TraderUserDetails();
    }

    
    /** 
     * @return Map<Trade, List<ExchangeMpid>>
     */
    @ModelAttribute("tempTrades")
    public Map<Trade, List<ExchangeMpid>> trades() {
        return new HashMap<>();
    }

    
    /** 
     * @return OrderBookDTO
     */
    @ModelAttribute("orderBook")
    public OrderBookDTO orderBook() {
        return new OrderBookDTO();
    }

    
    /** 
     * @return List<ExchangeMpid>
     */
    @ModelAttribute("sortExchanges")
    public List<ExchangeMpid> sortExchanges() {
        return new ArrayList<>();
    }

    
    /** 
     * @return Map<Trade, List<ExchangeMpid>>
     */
    @ModelAttribute("executedTrades")
    public Map<Trade, List<ExchangeMpid>> executedTrades() {
        return new HashMap<>();
    }

    
    /** 
     * @param "orderBook"
     * @return String
     */
    @PostMapping("/user/executeTrade")
    public String executeTrade(@ModelAttribute("executedTrades") Map<Trade, List<ExchangeMpid>> executedTrades,
            Model model, @ModelAttribute("orderBook") OrderBookDTO orderBookDto,
            @RequestParam(value = "executeTradeButton") int tradeId,
            @ModelAttribute("tempTrades") Map<Trade, List<ExchangeMpid>> tempTrades, SessionStatus status, 
            @ModelAttribute("trader") TraderUserDetails trader) { 
                //TODO: Make sure that when merging the trader is inputter as argument here ~Anna
        Trade chosenTrade = new Trade();
        for (Trade trade : tempTrades.keySet()) {
            if (trade.getBuyOrder().getRic().equals(orderBookDto.getRic()) && trade.getId() == tradeId) {
                chosenTrade = trade;
                break;
            }
        }
        executedTrades.put(chosenTrade, tempTrades.get(chosenTrade));
        System.out.println(trader.getTrader().getOrders().toString());
       // trader.getTrader().setOrders();
        executedTrades = sortService.tradePrice(executedTrades); //// getting trade price (either null bcs trade not executable or midpoint between buyPrice and sellPrice)
        for (Trade trade : executedTrades.keySet()) {
            if (trade.getBuyOrder().getPrice() != null) { //if trade executable
                model.addAttribute("executedTrades", executedTrades);
                //TODO: Make sure that when merging that for the equivalent Complete/ success page whatever the session is NOT refreshed ~Anna
                // status.setComplete(); //refreshes the session so previous trade data is disregarded
                return "redirect:/user/executeTradeSuccess";
            } else { //if trade non executable
                status.setComplete(); //refreshes the session so previous trade data is disregarded
                return "redirect:/user/errorTrade";
                //TODO: Make sure that when merging to include an extra else for the error page :)  ~Anna
            } 
        }
        return "/user/home";
    }

    
    /** 
     * @return String
     */
    @GetMapping("/user/executeTradeSuccess")
    public String tradeSuccess() {
        return "/user/executeTradeSuccess";
    }

    
    /** 
     * @return String
     */
    @GetMapping("/user/errorTrade")
    public String tradeError() {
        return "/user/errorTrade";
    }

    
    /** 
     * @param trader
     * @return String
     */
    @GetMapping("/user/personalBook")
    public String populateBook(@ModelAttribute("executedTrades") Map<Trade, List<ExchangeMpid>> executedTrades, Model model, @ModelAttribute("trader") TraderUserDetails trader) {
//TODO: Make sure that when merging the trader is inputter as argument here ~Anna
System.out.println(trader.getTrader().getOrders().toString());
       // List<Order> trOrders = trader.getTrader().getOrders();
       // System.out.println(trOrders.toString());
        // for(Order o : trOrders){
        //     if(o.getType().equals("BUY")){
        //         model.addAttribute("buy", o);
        //     }
        // }
       // model.addAttribute("trOrders", trOrders);

        return "/user/personalBook";
    }

    
    /** 
     * @param orderBook
     * @param order
     * @param bindingResult
     * @param model
     * @param trader
     * @param sortExchanges
     * @return String
     */
    @PostMapping("/user/stock-info")
    public String populateTable(@ModelAttribute("orderBook") OrderBookDTO orderBook, @ModelAttribute("order") OrderDTO order, BindingResult bindingResult, Model model, @ModelAttribute("trader") TraderUserDetails trader, @ModelAttribute("sortExchanges") List<ExchangeMpid> sortExchanges) {

        HashMap<String, Object> stockInfoData = (HashMap<String, Object>) stockInfo.stockInfoLoader(orderBook, trader.getTrader());

        @SuppressWarnings("unchecked")
        Map<Trade, List<ExchangeMpid>> tempTrades = (Map<Trade, List<ExchangeMpid>>) stockInfoData.get("tempTrades");
        
        model.addAttribute("tempTrades", tempTrades);
        model.addAttribute("totalOrders", stockInfoData.get("totalOrders"));
        model.addAttribute("totalVolume", stockInfoData.get("totalVolume"));

		return "/user/home";
	}

	/** 
     * Generates a OrderBookDTO object from the given entity object.
     * 
     * @param orderBook
     * @return OrderBookDTO
     */
    public OrderBookDTO convertToDto(OrderBook orderBook) {

        OrderBookDTO orderBookDto = modelMapper.map(orderBook, OrderBookDTO.class);

        orderBookDto.setRic(orderBook.getRic());

        return orderBookDto;
    }

    /** 
     * Generates a OrderBook entity object from the given DTO object.
     * 
     * @param orderBookDto
     * @return OrderBook
     */
    public OrderBook convertToEntity(OrderBookDTO orderBookDto) {

        OrderBook orderBook = modelMapper.map(orderBookDto, OrderBook.class);

        orderBook.setRic(orderBookDto.getRic());
     
        if (orderBookDto.getId() != 0) {

            OrderBook oldOrderBook = orderBookService.findOrderBook(orderBookDto.getId());
            orderBook.setOrders(oldOrderBook.getOrders());
        }

        return orderBook;
    }

}
