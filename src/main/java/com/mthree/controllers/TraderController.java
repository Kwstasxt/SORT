package com.mthree.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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

    
    /** 
     * @param model
     * @return String
     */
    @GetMapping("/registration")
    public String registration(Model model) {
        
    	model.addAttribute("userForm", new Trader());

        return "registration";
    }

    
    /** 
     * @param userForm
     * @param bindingResult
     * @return String
     */
    @PostMapping("/registerUser")
    public String registerUser(@ModelAttribute("userForm") Trader userForm, BindingResult bindingResult) {
        
    	traderValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        traderService.addTrader(userForm);

        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());

        return "redirect:/welcome";
    }
    
    
    /** 
     * @param model
     * @param error
     * @param logout
     * @return String
     */
    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
    	
    	if (error != null) {
    		model.addAttribute("error", "Your username and password is invalid.");
    	}
            
        if (logout != null) {
        	model.addAttribute("message", "You have been logged out successfully.");
        }
        
        return "login";
    }

    
    /** 
     * @param model
     * @return String
     */
    // user home screen
    @GetMapping({"/", "/welcome"})
    public String welcome(Model model) {
        return "index";
    }
}
