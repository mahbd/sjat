package com.sajjadsjat.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Price extends RealmObject {
    @PrimaryKey
    private long id;
    private String item;
    private double price;
    private String unit;

    public Price() {
    }

    public Price(String item, double price, String unit) {
        this.item = item;
        this.price = price;
        this.unit = unit;
    }
}
