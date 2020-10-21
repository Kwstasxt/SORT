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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mthree.dtos.OrderBookDTO;
import com.mthree.dtos.TraderDTO;
import com.mthree.models.ExchangeMpid;
import com.mthree.models.Ric;
import com.mthree.models.Trade;
import com.mthree.models.Trader;
import com.mthree.models.TraderUserDetails;
import com.mthree.services.SecurityService;
import com.mthree.services.TraderService;
import com.mthree.utils.StockInfo;
import com.mthree.utils.TraderValidator;

@Controller
@SessionAttributes({ "trader", "tempTrades", "selectedRic" })
public class TraderController {
	
	@Autowired
    private TraderService traderService;

    @Autowired
    private SecurityService securityService;

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
        return new HashMap<Trade, List<ExchangeMpid>>();
    }

    @ModelAttribute("selectedRic")
    public Ric selectedRic() {
        return Ric.values()[0];
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
    	Trader trader = convertToEntity(userForm);

        traderValidator.validate(trader, bindingResult);
        
        if (bindingResult.hasErrors()) {
            return "/user/register";
        } else {

            traderService.addTrader(trader);

            securityService.autoLogin(trader.getUsername(), trader.getPasswordConfirm());
        }

        return "redirect:/user/home";
    }
    
    /** 
-    * User login page.
-    *  
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
    public String home(Model model, @ModelAttribute TraderUserDetails trader, @ModelAttribute("orderBook") OrderBookDTO orderBook,
            @ModelAttribute("tempTrades") Map<Trade, List<ExchangeMpid>> tempTrades, @ModelAttribute("selectedRic") Ric selectedRic) {

        if (trader.getTrader() == null) {
            TraderUserDetails traderUd = (TraderUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            model.addAttribute("trader", traderUd);
            model.addAttribute("orderBook", new OrderBookDTO());

            HashMap<String, Object> stockInfoData = (HashMap<String, Object>) stockInfo.stockInfoLoader(null, traderUd.getTrader());

            @SuppressWarnings("unchecked")
            Map<Trade, List<ExchangeMpid>> tempTradesLocal = (Map<Trade, List<ExchangeMpid>>) stockInfoData.get("tempTrades");

            model.addAttribute("selectedRic", Ric.values()[0]);
            model.addAttribute("tempTrades", tempTradesLocal);
            model.addAttribute("totalOrders", stockInfoData.get("totalOrders"));
            model.addAttribute("totalVolume", stockInfoData.get("totalVolume"));

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
     
        if (traderDto.getId() != 0) {

            Trader oldTrader = traderService.findByTraderId(traderDto.getId());
            trader.setRole(oldTrader.getRole());
            trader.setOrders(oldTrader.getOrders());
        }

        return trader;
    }
}
