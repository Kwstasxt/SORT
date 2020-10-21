package com.mthree.models;

public enum ExchangeMpid {
    HONG_KONG_STOCK_EXCHANGE("SEHK"),
    JAPAN_EXCHANGE_GROUP("JXO"),
    LONDON_STOCK_EXCHANGE("LSE"),
    NASDAQ("NASDAQ"),
    NEW_YORK_STOCK_EXCHANGE("NYSE"),
    SHANGHAI_STOCK_EXCHANGE("SSE"),
    SWISS_EXCHANGE("SIX"),
    TORONTO_STOCK_EXCHANGE("TSX");

   private String notation;

    ExchangeMpid(String notation) {
        this.notation = notation;
    }

    public String getNotation() { return notation; }
}
