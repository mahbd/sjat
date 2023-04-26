package com.sajjadsjat.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Payment extends RealmObject {
    @PrimaryKey
    private long id;
    private double amount;
    private Client client;
    private long createdAt;
    private boolean isDiscount;
    private String payMethod;
    private String receiver;

    public long getId() {
        return id;
    }

    public Payment() {
    }

    public Payment(double amount, Client client, LocalDateTime createdAt, boolean isDiscount, String payMethod, String receiver) {
        Realm realm = Realm.getDefaultInstance();
        Number maxId = realm.where(Payment.class).max("id");
        this.id = maxId == null ? 1 : maxId.longValue() + 1;
        this.amount = amount;
        this.createdAt = createdAt.toInstant(ZoneOffset.UTC).toEpochMilli();
        if (client == null) {
            throw new IllegalArgumentException("Client cannot be null");
        }
        this.client = client;
        this.isDiscount = isDiscount;
        if (payMethod == null || payMethod.isEmpty()) {
            throw new IllegalArgumentException("PayMethod cannot be null");
        }
        this.payMethod = payMethod;
        if (receiver == null || receiver.isEmpty()) {
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
