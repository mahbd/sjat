package com.sajjadsjat.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Record extends RealmObject {
    @PrimaryKey
    private long id;
    private Client client;
    private long createdAt;
    private double discount;
    private String item;
    private double quantity;
    private String seller;
    private String unit;
    private double unitPrice;

    public long getId() {
        return id;
    }

    public String getDateTime() {
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(this.createdAt, 0, ZoneOffset.UTC);
        String month = dateTime.getMonthValue() < 10 ? "0" + dateTime.getMonthValue() : "" + dateTime.getMonthValue();
        String day = dateTime.getDayOfMonth() < 10 ? "0" + dateTime.getDayOfMonth() : "" + dateTime.getDayOfMonth();
        int full_hour = dateTime.getHour();
        if (full_hour > 12) {
            full_hour -= 12;
        }
        String hour = full_hour < 10 ? "0" + full_hour : "" + full_hour;
        String minute = dateTime.getMinute() < 10 ? "0" + dateTime.getMinute() : "" + dateTime.getMinute();
        return month + "-" + day + " " + hour + ":" + minute + " " + (dateTime.getHour() < 12 ? "am" : "pm");
    }

    public double getDiscount() {
        return discount;
    }

    public String getItem() {
        return item;
    }
    public double getQuantity() {
        return quantity;
    }

    public String getSeller() {
        return seller;
    }

    public String getUnit() {
        return unit;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public Record() {
    }

    public Record(Client client, long createdAt, double discount, String item, double quantity, String seller, String unit, double unitPrice) {
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null");
        }
        this.client = client;
        this.createdAt = createdAt;
        if (item == null || item.isEmpty()) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        this.item = item;
        this.quantity = quantity;
        if (unit == null || unit.isEmpty()) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.discount = discount;
        if (seller == null || seller.isEmpty()) {
            throw new IllegalArgumentException("Seller cannot be null");
        }
        this.seller = seller;

        Realm.getDefaultInstance().executeTransaction(realm -> {
            Number maxId = realm.where(Record.class).max("id");
            this.id = maxId == null ? 1 : maxId.longValue() + 1;
            realm.copyToRealmOrUpdate(this);
        });
    }

    public static Record get(long id) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Record.class).equalTo("id", id).findFirst();
    }

    public static List<Record> getByClient(Client client) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Record.class).equalTo("client.id", client.getId()).findAll();
    }
}
