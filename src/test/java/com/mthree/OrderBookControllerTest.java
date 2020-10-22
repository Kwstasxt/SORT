package com.mthree;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.mthree.models.Trade;
import com.mthree.models.Trader;
import com.mthree.utils.StockInfo;
import com.mthree.repositories.OrderBookRepository;
import com.mthree.services.ExchangeService;
import com.mthree.services.OrderBookService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderBookControllerTest {

    // @Mock
    // private OrderBookRepository obr;
	
	@Mock
    private OrderBookService obs;

    @Autowired
    private StockInfo stockInfo;

    @Autowired
    private ExchangeService exchangeService;

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

    }

    @AfterEach
    void breakDown() {
        // TODO: remove entry added to db
    }

    // @Test
    // void testPopulateTable() {

    //     OrderBookDTO orderBook = new OrderBookDTO();
    //     orderBook.setRic(Ric.BT);

    //     Trader trader = new Trader();
    //     trader.setRegion(Region.EMEA);
    //     List<Order> tradersOrders = new ArrayList<>();
    //     Order order = new Order();
    //     order.setRic(Ric.BT);
    //     order.setPrice(BigDecimal.valueOf(550));
    //     order.setQuantity(848);
    //     order.setType(OrderType.BUY);
    //     order.setSubmitTime(LocalDateTime.now());
    //     tradersOrders.add(order);
    //     trader.setOrders(tradersOrders);

    //     Map<String, Object> stockInfoData = stockInfo.stockInfoLoader(orderBook, trader);

    //     Map<Trade, List<ExchangeMpid>> tempTrades = new HashMap<>();

    //     Trade trade = new Trade();
    //     trade.setBuyOrder(order);
    //     Order match = new Order();
    //     match.setRic(Ric.BT);
    //     match.setPrice();
    //     match.setQuantity();
    //     match.setSubmitTime(submitTime);
    //     trade.setSellOrder(null);

    //     List<ExchangeMpid> mpids = new ArrayList<>();
    //     ExchangeMpid buyMpid = exchangeService.findMpidForOrder(null);
    //     ExchangeMpid sellMpid = exchangeService.findMpidForOrder(null);
    //     mpids.add(buyMpid);
    //     mpids.add(sellMpid);

    //     tempTrades.put(trade, mpids);
        
    //     assertEquals(stockInfoData.get("tempTrades"), tempTrades);
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
