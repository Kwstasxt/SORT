package com.mthree.models;

public enum Ric {
    VOD_LONDON("VOD.L"),
    BT_LONDON("BT.L");

    String notation;

    Ric(String notation) {
        this.notation = notation;
    }

    public String getNotation() { return notation; }
}
