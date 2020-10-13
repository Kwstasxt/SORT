package com.mthree.services;

import com.mthree.daos.TradeDAO;
import com.mthree.repositories.TradeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeService implements TradeDAO {
    
    @Autowired
	private TradeRepository tradeRepository;

}
