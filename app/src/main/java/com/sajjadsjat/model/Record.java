package com.sajjadsjat.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Record extends RealmObject {
    @PrimaryKey
    public long id;
    public long createdAt;
    public Item item;
    public double quantity;
    public Unit unit;
    public double unitPrice;
    public double discount;
    public String seller;
    public Client client;

    public Record() {}

    public Record(LocalDateTime createdAt, Item item, double quantity, Unit unit, double unitPrice, double discount, String seller) {
        this.createdAt = createdAt.toInstant(ZoneOffset.UTC).toEpochMilli();
        this.item = item;
        this.quantity = quantity;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.discount = discount;
        this.seller = seller;
    }

    public String getDateTime() {
        return "Incomplete";
    }
}
