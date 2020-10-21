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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mthree.dtos.OrderBookDTO;
import com.mthree.models.ExchangeMpid;
import com.mthree.models.OrderBook;
import com.mthree.models.Ric;
import com.mthree.models.Trade;
import com.mthree.models.TraderUserDetails;
import com.mthree.services.OrderBookService;
import com.mthree.services.SortService;
import com.mthree.utils.StockInfo;

@Controller
@SessionAttributes({ "trader", "tempTrades", "selectedRic", "executedTrades" })
public class OrderBookController {

    @Autowired
    private OrderBookService orderBookService;

    @Autowired
    private SortService sortService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private StockInfo stockInfo;

    @ModelAttribute("trader")
    public TraderUserDetails trader() {
        return new TraderUserDetails();
    }

    @ModelAttribute("tempTrades")
    public Map<Trade, List<ExchangeMpid>> trades() {
        return new HashMap<Trade, List<ExchangeMpid>>();
    }

    @ModelAttribute("selectedRic")
    public Ric selectedRic() {
        return Ric.values()[0];
    }

    @ModelAttribute("executedTrades")
    public Map<Trade, List<ExchangeMpid>> executedTrades() {
        return new HashMap<Trade, List<ExchangeMpid>>();
    }

    @GetMapping("/order/new")
    public void addOrder(@RequestParam("orderBookId") int orderBookId, @RequestParam("orderId") int orderId) {
        orderBookService.addOrder(orderBookId, orderId);
    }

    @GetMapping("/order/cancel")
    public void cancelOrder(@RequestParam("orderBookId") int orderBookId, @RequestParam("orderId") int orderId) {
        orderBookService.cancelOrder(orderBookId, orderId);
    }

    @PostMapping("/user/executeTrade")
    public String executeTrade(@ModelAttribute("executedTrades") Map<Trade, List<ExchangeMpid>> executedTrades,
            Model model, @ModelAttribute("selectedRic") Ric selectedRic,
            @RequestParam(value = "executeTradeButton") int tradeId,
            @ModelAttribute("tempTrades") Map<Trade, List<ExchangeMpid>> tempTrades, SessionStatus status) {
        Trade chosenTrade = new Trade();
        for (Trade trade : tempTrades.keySet()) {
            if (trade.getBuyOrder().getRic().equals(selectedRic) && trade.getId() == tradeId) {
                chosenTrade = trade;
                break;
            }
        }
        executedTrades.put(chosenTrade, tempTrades.get(chosenTrade));
        executedTrades = sortService.tradePrice(executedTrades); //// getting trade price (either null bcs trade not executable or midpoint between buyPrice and sellPrice)
        for (Trade trade : executedTrades.keySet()) {
            if (trade.getBuyOrder().getPrice() != null) { //if trade executable
                model.addAttribute("executedTrades", executedTrades);
                status.setComplete(); //refreshes the session so previous trade data is disregarded
                return "redirect:/user/executeTradeSuccess";
            } else { //if trade non executable
                System.out.println("trade cannot be executed");
                status.setComplete(); //refreshes the session so previous trade data is disregarded
                return "redirect:/user/errorTrade";
            } 
        }
        return "/user/home";
    }

    @GetMapping("/user/executeTradeSuccess")
    public String tradeSuccess() {
        return "/user/executeTradeSuccess";
    }

    @GetMapping("/user/errorTrade")
    public String tradeError() {
        return "/user/errorTrade";
    }

    @GetMapping("/user/personalBook")
    public String populateBook(@ModelAttribute("executedTrades") Map<Trade, List<ExchangeMpid>> executedTrades,
            Model model, @ModelAttribute TraderUserDetails trader) {

        return "/user/personalBook";
    }

    @PostMapping("/user/stock-info")
    public String populateTable(@ModelAttribute("orderBook") OrderBookDTO orderBook, BindingResult bindingResult, Model model, @ModelAttribute("trader") TraderUserDetails trader) {

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
