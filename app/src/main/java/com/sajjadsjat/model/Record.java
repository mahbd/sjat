package com.sajjadsjat.model;

import com.sajjadsjat.enums.Goods;
import com.sajjadsjat.enums.Units;

import java.time.LocalDateTime;

public class Record {
    public LocalDateTime creationDateTime;
    public Goods item;
    public double quantity;
    public Units unit;
    public double unitPrice;
    public double discount;
    public String seller;

    public Record() {
        this.creationDateTime = LocalDateTime.now();
    }

    public Record(Goods item, double quantity, Units unit, double unitPrice, double discount, String seller) {
        this.creationDateTime = LocalDateTime.now();
        this.item = item;
        this.quantity = quantity;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.discount = discount;
        this.seller = seller;
    }

    public String getDateTime() {
        // show DD-MM-YY HH
        String hour = creationDateTime.getHour() < 10 ? "0" + creationDateTime.getHour() : String.valueOf(creationDateTime.getHour());
        String year = creationDateTime.getYear() < 10 ? "0" + creationDateTime.getYear() : String.valueOf(creationDateTime.getYear());
        year = year.substring(2);
        String month = creationDateTime.getMonthValue() < 10 ? "0" + creationDateTime.getMonthValue() : String.valueOf(creationDateTime.getMonthValue());
        String day = creationDateTime.getDayOfMonth() < 10 ? "0" + creationDateTime.getDayOfMonth() : String.valueOf(creationDateTime.getDayOfMonth());
        return day + "-" + month;
    }
}
