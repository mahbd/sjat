package com.sajjadsjat.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Payment extends RealmObject {
    @PrimaryKey
    private long id;
    private double amount;
    private Client client;
    private long createdAt;
    private double discount;
    private PayMethod payMethod;
    private Employee receiver;

    public long getId() {
        return id;
    }

    public Payment() {
    }

    public Payment(double amount, Client client, LocalDateTime createdAt, double discount, PayMethod payMethod, Employee receiver) {
        Realm realm = Realm.getDefaultInstance();
        Number maxId = realm.where(Payment.class).max("id");
        this.id = maxId == null ? 1 : maxId.longValue() + 1;
        this.amount = amount;
        this.createdAt = createdAt.toInstant(ZoneOffset.UTC).toEpochMilli();
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null");
        }
        this.client = client;
        this.discount = discount;
        if (payMethod == null) {
            throw new IllegalArgumentException("PayMethod cannot be null");
        }
        this.payMethod = payMethod;
        if (receiver == null) {
            throw new IllegalArgumentException("Receiver cannot be null");
        }
        this.receiver = receiver;
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

    public static void delete(long id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Payment payment = realm.where(Payment.class).equalTo("id", id).findFirst();
        if (payment != null) {
            payment.deleteFromRealm();
        }
        realm.commitTransaction();
    }

}
