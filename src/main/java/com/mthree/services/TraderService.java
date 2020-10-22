package com.mthree.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.mthree.daos.TraderDAO;
import com.mthree.models.Order;
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

		// if trader exists simply update, otherwise set password and role
		if (findByTraderId(t.getId()) != null) {
			return traderRepository.save(t);
		} else {
			t.setPassword(bCryptPasswordEncoder.encode(t.getPassword()));
			t.setRole(Role.ROLE_ADMIN);
			return traderRepository.save(t);
		}
	}
	
	
	/** 
	 * @param username
	 * @return Trader
	 */
	@Override
	public Trader findByUsername(String username) {
		return traderRepository.findByUsername(username);
	}

	@Override
	public Trader findByOrder(Order order) {
		List<Trader> traders = traderRepository.findAll();

		for (Trader trader : traders) {
			for (Order tradersOrder : trader.getOrders()) {
				if (order.equals(tradersOrder)) return trader;
			}
		}
		
		return null;
	}

	
	/** 
	 * @param traderId
	 * @return Trader
	 */
	@Override
	public Trader findByTraderId(int traderId) {

		Optional<Trader> trader = traderRepository.findById(traderId);
		
		if (trader.isPresent()) {
			return trader.get();
		}

		return null;
	}
	
	
	/** 
	 * @param t
	 */
	@Override
	public void removeTrader(Trader t) {
		traderRepository.delete(t);
	}
}
