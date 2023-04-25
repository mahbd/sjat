package com.sajjadsjat.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Payment extends RealmObject {
    @PrimaryKey
    private long id;
    private long createdAt;
    private double amount;
    private double discount;
    private PayMethod payMethod;
    private Client client;

    public long getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
        this.save();
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
        this.save();
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
        this.save();
    }

    public PayMethod getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(PayMethod payMethod) {
        this.payMethod = payMethod;
        this.save();
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
        this.save();
    }

    public Payment() {
    }

    public Payment(LocalDateTime createdAt, double amount, double discount, PayMethod payMethod) {
        Realm realm = Realm.getDefaultInstance();
        Number maxId = realm.where(Payment.class).max("id");
        this.id = maxId == null ? 1 : maxId.longValue() + 1;
        this.createdAt = createdAt.toInstant(ZoneOffset.UTC).toEpochMilli();
        this.amount = amount;
        this.discount = discount;
        this.payMethod = payMethod;
        this.save();
    }

    private void save() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(this);
        realm.commitTransaction();
    }

    public static Payment get(long id) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Payment.class).equalTo("id", id).findFirst();
    }

    void delete() {
        this.deleteFromRealm();
    }

}
