package com.mthree.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mthree.dtos.OrderBookDTO;
import com.mthree.dtos.OrderDTO;
import com.mthree.models.Exchange;
import com.mthree.models.ExchangeMpid;
import com.mthree.models.Order;
import com.mthree.models.OrderBook;
import com.mthree.models.Ric;
import com.mthree.models.Trade;
import com.mthree.models.TraderUserDetails;
import com.mthree.services.ExchangeService;
import com.mthree.services.OrderService;
import com.mthree.services.TraderService;
import com.mthree.utils.StockInfo;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@SessionAttributes({ "trader", "tempTrades", "orderBook", "executedTrades", "sortExchanges" })
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private TraderService traderService;

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
     * @return Ric
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
     * @param trader
     * @param ric
     * @param price
     * @param quantity
     * @param type
     */
    @PostMapping("/order/new")
    public String addOrder(@ModelAttribute("trader") TraderUserDetails trader, @ModelAttribute("orderBook") OrderBookDTO orderBookDto, @ModelAttribute("order") OrderDTO orderDto, BindingResult bindingResult, Model model, @ModelAttribute("sortExchanges") List<ExchangeMpid> sortExchanges) {

        Ric ric = orderBookDto.getRic();
        ExchangeMpid mpid = orderDto.getMpid();
        Order order = convertToEntity(orderDto);
        order.setRic(ric);
        order.setSubmitTime(LocalDateTime.now());

        // save order to db
        orderService.addOrder(order);

        Exchange exchange = exchangeService.findExchange(mpid);
        List<OrderBook> exchangeOrderBooks = exchange.getOrderBooks();

        // add to exchanges orderbook
        for (int i=0; i < exchangeOrderBooks.size(); i++) {

            OrderBook currentOrderBook = exchangeOrderBooks.get(i);
            String currentRic = ric.getNotation();
            String orderBookRic = currentOrderBook.getRic().getNotation();

            if (orderBookRic.equals(currentRic)) {

                List<Order> orders = currentOrderBook.getOrders();
                orders.add(order);
                currentOrderBook.setOrders(orders);

                break;

            } else {
                // if exchange has no order book for ric, create one
                if (i == exchangeOrderBooks.size() - 1) {

                    OrderBook newExchangeOrderBook = new OrderBook();
                    newExchangeOrderBook.setRic(ric);
                    List<Order> orders = new ArrayList<>();
                    orders.add(order);
                    newExchangeOrderBook.setOrders(orders);
                    exchangeOrderBooks.add(newExchangeOrderBook);
                    exchange.setOrderBooks(exchangeOrderBooks);

                }
            }
        }

        // update traders orders
        List<Order> orders = trader.getTrader().getOrders();
        orders.add(order);
        trader.getTrader().setOrders(orders);
        traderService.addTrader(trader.getTrader());

        HashMap<String, Object> stockInfoData = (HashMap<String, Object>) stockInfo.stockInfoLoader(orderBookDto, trader.getTrader());

        @SuppressWarnings("unchecked")
        Map<Trade, List<ExchangeMpid>> tempTrades = (Map<Trade, List<ExchangeMpid>>) stockInfoData.get("tempTrades");
        
        model.addAttribute("tempTrades", tempTrades);
        model.addAttribute("totalOrders", stockInfoData.get("totalOrders"));
        model.addAttribute("totalVolume", stockInfoData.get("totalVolume"));

        // reset order fields
        OrderDTO clearedOrderDto = new OrderDTO();
        clearedOrderDto.setRic(ric);

        model.addAttribute("order", clearedOrderDto);

		return "/user/home";
    }

    
    /** 
     * @param orderBookId
     * @param orderId
     */
    // TODO: implement in front-end
    @PostMapping("/order/cancel")
    public void cancelOrder(@RequestParam("orderBookId") int orderBookId, @RequestParam("orderId") int orderId) {

        // remove from exchange
        // remove from orderbook
        orderService.deleteOrderByID(orderId);
    }

    // TODO: test
    /** 
     * Generates a OrderDTO object from the given entity object.
     * 
     * @param order
     * @return OrderDTO
     */
    public OrderDTO convertToDto(Order order) {

        OrderDTO orderDto = modelMapper.map(order, OrderDTO.class);

        orderDto.setRic(order.getRic());
        orderDto.setPrice(order.getPrice());
        orderDto.setQuantity(order.getQuantity());
        orderDto.setType(order.getType());

        return orderDto;
    }

    /** 
     * Generates a Order entity object from the given DTO object.
     * 
     * @param orderDto
     * @return Order
     */
    public Order convertToEntity(OrderDTO orderDto) {

        Order order = modelMapper.map(orderDto, Order.class);

        order.setRic(orderDto.getRic());
        order.setPrice(orderDto.getPrice());
        order.setQuantity(orderDto.getQuantity());
        order.setType(orderDto.getType());
     
        if (orderDto.getId() != 0) {

            Order oldOrder = orderService.findOrder(orderDto.getId());
            order.setSubmitTime(oldOrder.getSubmitTime());
        }

        return order;
    }
}
