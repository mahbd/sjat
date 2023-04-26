package com.sajjadsjat.model;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;

public class Address extends RealmObject {
    @PrimaryKey
    private long id;
    private String para;
    private String village;
    private String union;
    @LinkingObjects("address")
    public final RealmResults<Client> clients;

    public long getId() {
        return id;
    }

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        this.para = format(para);
        realm.commitTransaction();
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        this.village = format(village);
        realm.commitTransaction();
    }

    public String getUnion() {
        return union;
    }

    public void setUnion(String union) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        this.union = format(union);
        realm.commitTransaction();
    }

    public String toString() {
        return para + ", " + village + ", " + union;
    }

    public static String format(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        str = str.toLowerCase().trim();
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public Address() {
        this.clients = null;
    }

    public Address(String para, String village, String union) {
        Number maxId = Realm.getDefaultInstance().where(Address.class).max("id");
        this.id = maxId == null ? 1 : maxId.longValue() + 1;
        this.para = format(para);
        this.village = format(village);
        this.union = format(union);
        this.clients = null;
        Realm.getDefaultInstance().executeTransaction(r -> r.copyToRealm(this));
    }

    public static boolean doesExist(String para, String village, String union) {
        return Realm.getDefaultInstance().where(Address.class)
                .equalTo("para", format(para))
                .equalTo("village", format(village))
                .equalTo("union", format(union))
                .count() > 0;
    }

    public static Address get(long id) {
        return Realm.getDefaultInstance().where(Address.class).equalTo("id", id).findFirst();
    }

    public static List<Address> getAll() {
        return Realm.getDefaultInstance().where(Address.class).findAll();
    }

    public static void delete(long id) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(r -> {
            Address address = r.where(Address.class).equalTo("id", id).findFirst();
            if (address != null) {
                address.deleteFromRealm();
            }
        });
    }
}
