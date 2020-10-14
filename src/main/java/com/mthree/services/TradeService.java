package com.mthree.services;

import java.util.List;
import java.util.Optional;

import com.mthree.daos.TradeDAO;
import com.mthree.models.Trade;
import com.mthree.repositories.TradeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeService implements TradeDAO {

    @Autowired
    private TradeRepository tradeRepository;

    @Override
    public Trade findTrade(int tradeId) {

        Optional<Trade> trade = tradeRepository.findById(tradeId);

        if (trade.isPresent()) {
            return trade.get();
        }

        return null;
    }

    @Override
    public List<Trade> findTrades() {
        return tradeRepository.findAll();
    }

}
