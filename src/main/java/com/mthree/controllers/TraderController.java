package com.mthree.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mthree.dtos.OrderBookDTO;
import com.mthree.dtos.OrderDTO;
import com.mthree.dtos.TraderDTO;
import com.mthree.models.Exchange;
import com.mthree.models.ExchangeMpid;
import com.mthree.models.Order;
import com.mthree.models.Ric;
import com.mthree.models.Trade;
import com.mthree.models.Trader;
import com.mthree.models.TraderUserDetails;
import com.mthree.services.OrderService;
import com.mthree.services.SecurityService;
import com.mthree.services.SortService;
import com.mthree.services.TraderService;
import com.mthree.utils.StockInfo;
import com.mthree.utils.TraderValidator;

@Controller
@SessionAttributes({ "trader", "tempTrades", "orderBook", "sortExchanges" })
public class TraderController {
	
	@Autowired
    private TraderService traderService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SortService sortService;

    @Autowired
    private TraderValidator traderValidator;

    @Autowired
    private StockInfo stockInfo;

    @Autowired
    private ModelMapper modelMapper;

    
    /** 
     * @return TraderUserDetails
     */
    @ModelAttribute("trader")
	public TraderUserDetails trader() {
		return new TraderUserDetails();
    }
    
    @ModelAttribute("tempTrades")
    public Map<Trade, List<ExchangeMpid>> trades() {
        return new HashMap<>();
    }

    @ModelAttribute("orderBook")
    public OrderBookDTO orderBook() {
        return new OrderBookDTO();
    }

    @ModelAttribute("sortExchanges")
    public List<ExchangeMpid> sortExchanges() {
        return new ArrayList<>();
    }
    
    /** 
     * User welcome page.
     * 
     * @param model
     * @return String
     */
    @GetMapping({"/", "/user/index"})
    public String welcome(Model model) {
        return "/user/index";
    }
    
    /** 
     * User registration page.
     * 
     * @param model
     * @return String
     */
    @GetMapping("/user/register")
    public String registration(Model model) {
        
    	model.addAttribute("userForm", new TraderDTO());

        return "/user/register";
    }

    /** 
-     * User registration success/fail.
-     * 
      * @param userForm
      * @param bindingResult
      * @return String
     */
    @PostMapping("/user/new")
    public String registerUser(@ModelAttribute("userForm") TraderDTO userForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/user/register";
        } else {
            Trader trader = convertToEntity(userForm);

            traderValidator.validate(trader, bindingResult);

            traderService.addTrader(trader);

            securityService.autoLogin(trader.getUsername(), trader.getPasswordConfirm());
        }

        return "redirect:/user/home";
    }
    
    /** 
     * User login page.
     *  
     * @param model
     * @param error
     * @param logout
     * @return String
     */
    @GetMapping("/user/login")
    public String login(Model model, String error, String logout) {
    	
    	if (error != null) {
    		model.addAttribute("error", "Your username and password is invalid.");
    	}
            
        if (logout != null) {
        	model.addAttribute("message", "You have been logged out successfully.");
        }
        
       return "/user/login";
    }


    /** 
     * User home page.
     * 
     * @param model
     * @return String
     */
    @GetMapping("/user/home")
    public String home(Model model, @ModelAttribute("trader") TraderUserDetails trader, @ModelAttribute("orderBook") OrderBookDTO orderBook, @ModelAttribute("order") OrderDTO order, @ModelAttribute("tempTrades") Map<Trade, List<ExchangeMpid>> tempTrades, @ModelAttribute("orderBook") OrderBookDTO orderBookDto, @ModelAttribute("sortExchanges") List<ExchangeMpid> sortExchanges) {

        if (trader.getTrader() == null) {

            TraderUserDetails traderUd = (TraderUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            model.addAttribute("trader", traderUd);
            OrderBookDTO newOrderBookDto = new OrderBookDTO();
            newOrderBookDto.setRic(Ric.values()[0]);
            model.addAttribute("orderBook", newOrderBookDto);
            model.addAttribute("order", new OrderDTO());

            HashMap<String, Object> stockInfoData = (HashMap<String, Object>) stockInfo.stockInfoLoader(null, traderUd.getTrader());

            @SuppressWarnings("unchecked")
            Map<Trade, List<ExchangeMpid>> tempTradesLocal = (Map<Trade, List<ExchangeMpid>>) stockInfoData.get("tempTrades");

            List<Exchange> exchanges = sortService.findSortForTrader(traderUd.getTrader()).getExchanges();
            List<ExchangeMpid> exchangeMpids = new ArrayList<>();

            for (Exchange exchange : exchanges) {
                exchangeMpids.add(exchange.getMpid());
            }

            model.addAttribute("tempTrades", tempTradesLocal);
            model.addAttribute("totalOrders", stockInfoData.get("totalOrders"));
            model.addAttribute("totalVolume", stockInfoData.get("totalVolume"));
            model.addAttribute("sortExchanges", exchangeMpids);

            return "/user/home";
        } 

    	 return "/user/home";
    }

    
    /** 
     * @param request
     * @param response
     * @param status
     * @return String
     */
    @PostMapping("/user/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, SessionStatus status) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {    
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        status.setComplete();
        
        return "/user/logout";
    }

    /** 
     * Generates a TraderDTO object from the given entity object.
     * 
     * @param trader
     * @return TraderDTO
     */
    public TraderDTO convertToDto(Trader trader) {

        TraderDTO traderDto = modelMapper.map(trader, TraderDTO.class);

        traderDto.setUsername(trader.getUsername());
        traderDto.setPassword(trader.getPassword());
        traderDto.setPasswordConfirm(trader.getPasswordConfirm());
        traderDto.setRegion(trader.getRegion());
        traderDto.setOrders(trader.getOrders());

        return traderDto;
    }

    /** 
     * Generates a Trader entity object from the given DTO object.
     * 
     * @param traderDto
     * @return Trader
     */
    public Trader convertToEntity(TraderDTO traderDto) {

        Trader trader = modelMapper.map(traderDto, Trader.class);

        trader.setUsername(traderDto.getUsername());
        trader.setPassword(traderDto.getPassword());
        trader.setPasswordConfirm(traderDto.getPasswordConfirm());
        trader.setRegion(traderDto.getRegion());
        List<Order> traderOrders = orderService.generateRandomOrdersForTrader(trader);
        trader.setOrders(traderOrders);
     
        if (traderDto.getId() != 0) {

            Trader oldTrader = traderService.findByTraderId(traderDto.getId());
            trader.setRole(oldTrader.getRole());
        }

        return trader;
    }
}
