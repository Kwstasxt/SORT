package com.mthree;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.mthree.controllers.OrderBookController;
import com.mthree.dtos.OrderBookDTO;
import com.mthree.models.Exchange;
import com.mthree.models.ExchangeMpid;
import com.mthree.models.Order;
import com.mthree.models.OrderBook;
import com.mthree.models.OrderType;
import com.mthree.models.Region;
import com.mthree.models.Ric;
import com.mthree.models.Sort;
import com.mthree.utils.StockInfo;
import com.mthree.repositories.OrderBookRepository;
import com.mthree.services.OrderBookService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderBookControllerTest {

    @Mock
    private OrderBookRepository obr;
	
	@Mock
    private OrderBookService obs;

    @Mock
    private StockInfo stockInfo;

    // TODO: mock sort service? 
    // TODO: mock exchange service?

    @InjectMocks
	private OrderBookController obc = new OrderBookController();

    @Autowired
    private ModelMapper modelMapper;

    private int totalOrders;
    private int totalVolume;

    @BeforeEach
    void setUp() {

        // create dummy sort with 2 exchanges (both of which contain BT orders)
        Sort sort = new Sort();
        sort.setRegion(Region.EMEA);

        Exchange exchange1 = new Exchange();
        exchange1.setMpid(ExchangeMpid.LONDON_STOCK_EXCHANGE);
        Exchange exchange2 = new Exchange();
        exchange2.setMpid(ExchangeMpid.NEW_YORK_STOCK_EXCHANGE);

		List<OrderBook> orderBooks = new ArrayList<>();
		List<OrderBook> orderBooks2 = new ArrayList<>();

		OrderBook orderBook1 = new OrderBook();
		orderBook1.setRic(Ric.BT);
		OrderBook orderBook2 = new OrderBook();
		orderBook2.setRic(Ric.BT);

		List<Order> orders1 = new ArrayList<>();
		List<Order> orders2 = new ArrayList<>();
		orders1.add(new Order(1, Ric.BT, BigDecimal.valueOf(10.99), 100, OrderType.BUY, LocalDateTime.now()));
		orders2.add(new Order(2, Ric.BT, BigDecimal.valueOf(11.00), 100, OrderType.SELL, LocalDateTime.now()));
		orders1.add(new Order(3, Ric.BT, BigDecimal.valueOf(10.99), 100, OrderType.BUY, LocalDateTime.now()));
		orders2.add(new Order(4, Ric.BT, BigDecimal.valueOf(11.00), 100, OrderType.SELL, LocalDateTime.now()));

		orderBook1.setOrders(orders1);
		orderBook2.setOrders(orders2);

		orderBooks.add(orderBook1);
        orderBooks2.add(orderBook2);
		
		exchange1.setOrderBooks(orderBooks);
		exchange2.setOrderBooks(orderBooks2);

		List<Exchange> exchanges = new ArrayList<>();
		exchanges.add(exchange1);
		exchanges.add(exchange2);
        sort.setExchanges(exchanges);

        for (Exchange exchange : sort.getExchanges()) {
            for (OrderBook exchangeOrderBooks : exchange.getOrderBooks()) {
                for (Order exchangeOrder : exchangeOrderBooks.getOrders()) {
                    totalOrders++;
                    totalVolume += exchangeOrder.getQuantity();
                }
            }
        }

    }

    // @Test
    // void testPopulateTable() {

    //     OrderBookDTO orderBook = new OrderBookDTO();
    //     orderBook.setRic(Ric.BT);
    //     Trader trader = new Trader();
    //     trader.setRegion(Region.EMEA);

    //     Map<String, Object> stockInfoData = stockInfo.stockInfoLoader(orderBook, trader);

    //     Map<Trade, List<ExchangeMpid>> tempTrades = new HashMap<>();
        
    //     assertEquals(stockInfoData.get("tempTrades"), null);
    //     assertEquals(stockInfoData.get("totalOrders"), totalOrders);
    //     assertEquals(stockInfoData.get("totalVolume"), totalVolume);
    // }
    
    @Test
    public void testConvertToEntity() {
        
        OrderBookDTO orderBookDTO = new OrderBookDTO(1, Ric.BT);

        OrderBook orderBook = modelMapper.map(orderBookDTO, OrderBook.class);
        
        assertEquals(orderBookDTO.getId(), orderBook.getId());
        assertEquals(orderBookDTO.getRic(), orderBook.getRic());
    }

    @Test
    public void testConvertToDto() {

        OrderBook orderBook = new OrderBook();
        orderBook.setId(1);
        orderBook.setRic(Ric.BT);

        OrderBookDTO orderBookDTO = modelMapper.map(orderBook, OrderBookDTO.class);

        assertEquals(orderBook.getId(), orderBookDTO.getId());
        assertEquals(orderBook.getRic(), orderBookDTO.getRic());
    }
}
