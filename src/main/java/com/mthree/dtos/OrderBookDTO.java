package com.mthree.dtos;

import com.mthree.models.Ric;

public class OrderBookDTO {
    
    private int id;
    private Ric ric;

    public OrderBookDTO() {}

    public OrderBookDTO(int id, Ric ric) {
        this.id = id;
        this.ric = ric;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Ric getRic() {
        return ric;
    }

    public void setRic(Ric ric) {
        this.ric = ric;
    }

    @Override
    public String toString() {
        return "OrderBookDTO [id=" + id + ", ric=" + ric + "]";
    }
}
