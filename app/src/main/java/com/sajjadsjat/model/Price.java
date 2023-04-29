package com.sajjadsjat.model;

import androidx.annotation.NonNull;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class Price extends RealmObject {
    @PrimaryKey
    private long id;
    private String item;
    private double price;
    private String unit;

    public static String format(String str) {
        if (str == null) {
            return "";
        }
        str = str.trim().toLowerCase();
        if (str.isEmpty()) {
            return "";
        }
        if (str.length() == 1) return str.toUpperCase();
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public long getId() {
        return id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        item = format(item);
        if (this.item.equals(item)) {
            return;
        }
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        this.item = item;
        realm.commitTransaction();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (this.price == price) {
            return;
        }
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        this.price = price;
        realm.commitTransaction();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        unit = format(unit);
        if (this.unit.equals(unit)) {
            return;
        }
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        this.unit = unit;
        realm.commitTransaction();
    }

    @NonNull
    public String toString() {
        return this.item + " -> " + this.price + " -> " + this.unit;
    }

    public Price() {
    }

    public Price(String item, double price, String unit) {
        this.item = format(item);
        this.price = price;
        this.unit = format(unit);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Number maxId = realm.where(Price.class).max("id");
        this.id = maxId == null ? 1 : maxId.longValue() + 1;
        realm.copyToRealm(this);
        realm.commitTransaction();
    }

    public static boolean doesExist(String item, String unit) {
        return Realm.getDefaultInstance().where(Price.class).equalTo("item", item).equalTo("unit", unit).findFirst() != null;
    }

    public static boolean hasItem(String item) {
        if (item == null || item.isEmpty()) return false;
        return Realm.getDefaultInstance().where(Price.class).equalTo("item", item).findFirst() != null;
    }

    public static Price get(long id) {
        return Realm.getDefaultInstance().where(Price.class).equalTo("id", id).findFirst();
    }

    public static RealmResults<Price> getAll() {
        return Realm.getDefaultInstance().where(Price.class).findAll();
    }

    public static void delete(long id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Price price1 = realm.where(Price.class).equalTo("id", id).findFirst();
        if (price1 != null) {
            price1.deleteFromRealm();
        }
        realm.commitTransaction();
    }
}
