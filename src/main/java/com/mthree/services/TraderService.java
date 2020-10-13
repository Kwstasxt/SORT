package com.mthree.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mthree.daos.TraderDAO;
import com.mthree.models.Role;
import com.mthree.models.Trader;
import com.mthree.repositories.TraderRepository;

@Service
public class TraderService implements TraderDAO {
	
	@Autowired
	private TraderRepository traderRepository;
	
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

	
	/** 
	 * @param t
	 * @return Trader
	 */
	@Override
	public Trader addTrader(Trader t) {
		t.setPassword(bCryptPasswordEncoder.encode(t.getPassword()));
		t.setRole(Role.ROLE_ADMIN);
		return traderRepository.save(t);
	}
	
	
	/** 
	 * @param username
	 * @return Trader
	 */
	@Override
	public Trader findByUsername(String username) {
		return traderRepository.findByUsername(username);
	}
	
	
	/** 
	 * @param t
	 */
	@Override
	public void removeTrader(Trader t) {
		traderRepository.delete(t);
	}
}
