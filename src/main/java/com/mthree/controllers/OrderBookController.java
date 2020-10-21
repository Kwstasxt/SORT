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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mthree.dtos.OrderBookDTO;
import com.mthree.models.ExchangeMpid;
import com.mthree.models.OrderBook;
import com.mthree.models.Trade;
import com.mthree.models.TraderUserDetails;
import com.mthree.services.OrderBookService;
import com.mthree.utils.StockInfo;

@Controller
@SessionAttributes("trader")
public class OrderBookController {

    @Autowired
    private OrderBookService orderBookService;

    @Autowired
    private StockInfo stockInfo;

    @Autowired
    private ModelMapper modelMapper;

    @ModelAttribute("trader")
	public TraderUserDetails trader() {
		return new TraderUserDetails();
	}

    @GetMapping("/order/new")
    public void addOrder(@RequestParam("orderBookId") int orderBookId, @RequestParam("orderId") int orderId) {
        orderBookService.addOrder(orderBookId, orderId);
    }

    @GetMapping("/order/cancel")
    public void cancelOrder(@RequestParam("orderBookId") int orderBookId, @RequestParam("orderId") int orderId) {
        orderBookService.cancelOrder(orderBookId, orderId);
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
