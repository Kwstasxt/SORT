package com.mthree.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mthree.models.Trader;
import com.mthree.models.TraderUserDetails;
import com.mthree.repositories.TraderRepository;

@Service
public class TraderDetailsService implements UserDetailsService {
	
	@Autowired
	private TraderRepository traderRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Trader user = traderRepository.findByUsername(username);
        
        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }
         
        return new TraderUserDetails(user);
	}

}
