package com.mthree.daos;

import java.util.List;

import com.mthree.models.Trade;

public interface TradeDAO {

    Trade findTrade(int tradeId);
    List<Trade> findTrades();
    
}
