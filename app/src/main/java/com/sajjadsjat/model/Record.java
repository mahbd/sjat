package com.sajjadsjat.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class Record extends RealmObject {
    @PrimaryKey
    private long id;
    private long createdAt;
    private Item item;
    private double quantity;
    private Unit unit;
    private double unitPrice;
    private double discount;
    private String seller;
    private Client client;

    public long getId() {
        return id;
    }

    public String getItem() {
        return item.getName();
    }

    public void setItem(Item item) {
        this.item = item;
        this.save();
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit.getName();
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public String getSeller() {
        return seller;
    }

    public Record() {}

    public Record(LocalDateTime createdAt, Item item, double quantity, Unit unit, double unitPrice, double discount, String seller) {
        this.createdAt = createdAt.toInstant(ZoneOffset.UTC).toEpochMilli();
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
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

    private void save() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(this);
        realm.commitTransaction();
    }

    public static Record get(long id) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Record.class).equalTo("id", id).findFirst();
    }

    public static List<Record> getByClient(Client client) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Record.class).equalTo("client", client.getId()).findAll();
    }
}
