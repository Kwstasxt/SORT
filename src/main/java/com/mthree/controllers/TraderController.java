package com.mthree.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.mthree.dtos.TraderDTO;
import com.mthree.models.Trader;
import com.mthree.services.SecurityService;
import com.mthree.services.TraderService;
import com.mthree.utils.TraderValidator;

@Controller
public class TraderController {
	
	@Autowired
    private TraderService traderService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private TraderValidator traderValidator;

    @Autowired
    private ModelMapper modelMapper;

    
    // initial screen
    @GetMapping({"/", "/user/index"})
    public String welcome(Model model) {
        return "/user/index";
    }
    
    /** 
     * @param model
     * @return String
     */
    @GetMapping("/user/registration")
    public String registration(Model model) {
        
    	model.addAttribute("userForm", new TraderDTO());

        return "/user/add-trader";
    }

    /** 
     * @param userForm
     * @param bindingResult
     * @return String
     */
    @PostMapping("/user/add-trader")
    public String registerUser(@ModelAttribute("userForm") TraderDTO userForm, BindingResult bindingResult) {
    	Trader trader = convertToEntity(userForm);

        traderValidator.validate(trader, bindingResult);
        
        if (bindingResult.hasErrors()) {
            return "/user/add-trader";
        } else {

            traderService.addTrader(trader);

            securityService.autoLogin(trader.getUsername(), trader.getPasswordConfirm());

            return "/user/homepage";
        }
    }
    
    /** 
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
     * @param model
     * @return String
     */
    // user home screen
    @GetMapping("/user/homepage")
    public String homepage(Model model) {
    	 return "/user/homepage";
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
