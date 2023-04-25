package com.sajjadsjat.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Payment extends RealmObject {
    @PrimaryKey
    public long id;
    public long createdAt;
    public double amount;
    public double discount;
    public PayMethod payMethod;
    public Client client;

    void print() {
        System.out.println("Payment: " + id + " " + createdAt + " " + amount + " " + discount + " " + payMethod.name);
    }

    void delete() {
        this.deleteFromRealm();
    }

    void save() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(this);
        realm.commitTransaction();
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

}
