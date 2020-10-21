package com.mthree.models;

public enum Ric {
    BARCLAYS("BARC"),
    BHP("BHP"),
    BP("BP."),
    BT("BT"),
    COCA_COLA("CCH"),
    EXPERIAN("EXPN"),
    GSK("GSK"),
    HSBC("HSBA"),
    NATWEST_GROUP("NWG"),
    ROLLS_ROYCE("RR."),
    SAINSBURYS("SBRY"),
    TESCO("TSCO"),
    UNILEVER("ULVR"),
    VODAFONE("VOD");

   private String notation;

    Ric(String notation) {
        this.notation = notation;
    }

    public String getNotation() { return notation; }
}
