package com.sajjadsjat.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

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

    public double getAmount() {
        return amount;
    }

    public String getDateTime() {
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(this.createdAt / 1000, 0, ZoneOffset.UTC);
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

    public String getReceiver() {
        return receiver;
    }

    public Payment() {
    }

    public Payment(double amount, Client client, long createdAt, boolean isDiscount, String payMethod, String receiver) {
        this.amount = amount;
        this.createdAt = createdAt;
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
        Realm.getDefaultInstance().executeTransaction(realm -> {
            Number maxId = realm.where(Payment.class).max("id");
            this.id = maxId == null ? 1 : maxId.longValue() + 1;
            realm.copyToRealmOrUpdate(this);
        });
    }

    public static Payment get(long id) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Payment.class).equalTo("id", id).findFirst();
    }

    public static List<Payment> getByClient(Client client) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Payment.class).equalTo("client.id", client.getId()).findAll();
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
