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
        
    	model.addAttribute("userForm", new Trader());

        return "/user/add-trader";
    }

    /** 
     * @param userForm
     * @param bindingResult
     * @return String
     */
    @PostMapping("/user/add-trader")
    public String registerUser(@ModelAttribute("userForm") Trader userForm, BindingResult bindingResult) {
    	//System.out.println(userForm);
    	traderValidator.validate(userForm, bindingResult);
    	//System.out.println(userForm);
        if (bindingResult.hasErrors()) {
            return "/user/add-trader";
        }

        traderService.addTrader(userForm);

        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());

        return "redirect:/user/index";
    }
    
    /** 
     * @param model
     * @param error
     * @param logout
     * @return String
     */
    @GetMapping("/user/login")
    public String login(Model model, String error, String logout) {
    	//System.out.println("loginnnn");
    	
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
  
    
}
