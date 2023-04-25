package com.sajjadsjat.model;

import java.util.Random;

import javax.annotation.Nullable;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;

public class Client extends RealmObject {
    @PrimaryKey
    public long id;
    public String name;
    @Nullable
    public String fathersName;
    private String phone;
    public String union;
    public String village;
    public String para;
    @Nullable
    public String extraIdentifier;
    @LinkingObjects("client")
    public final RealmResults<Record> records;
    @LinkingObjects("client")
    public final RealmResults<Payment> payments;

    public Client() {
        this.records = null;
        this.payments = null;
    }

    void save() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(this);
        realm.commitTransaction();
    }

    public Client(String name, @androidx.annotation.Nullable String fathersName, String phone, String union, String village, String para, @androidx.annotation.Nullable String extraIdentifier) {
        Realm realm = Realm.getDefaultInstance();
        Number maxId = realm.where(Client.class).max("id");
        this.id = maxId == null ? 1 : maxId.intValue() + 1;
        // check if name and fathersName unique together
        RealmResults<Client> clients = realm.where(Client.class).equalTo("name", name).equalTo("fathersName", fathersName).findAll();
        if (clients.size() > 0) {
            throw new RuntimeException("Client with same name and father's name already exists");
        }
        this.name = name;
        this.fathersName = fathersName;
        // check if phone unique
        clients = realm.where(Client.class).equalTo("phone", phone).findAll();
        if (clients.size() > 0) {
            throw new RuntimeException("Client with same phone already exists");
        }
        this.phone = phone;
        this.union = union;
        this.village = village;
        // para, name and extraIdentifier are not unique
        clients = realm.where(Client.class).equalTo("para", para).equalTo("name", name).equalTo("extraIdentifier", extraIdentifier).findAll();
        if (clients.size() > 0) {
            throw new RuntimeException("Client with same para, name and extraIdentifier already exists");
        }
        this.para = para;
        this.extraIdentifier = extraIdentifier;
        this.records = null;
        this.payments = null;
        this.save();
    }

    void validate() {

    }
}
