package com.sajjadsjat.model;

import java.time.LocalDateTime;

import io.realm.Realm;
import io.realm.RealmResults;

public class UserRecordData {
    public static void init(Realm realm) {
        RealmResults<Item> items = realm.where(Item.class).equalTo("name", "Cement").findAll();
        if (items.size() == 0) {
            new Item("Cement");
        }
        items = realm.where(Item.class).equalTo("name", "Sand").findAll();
        if (items.size() == 0) {
            new Item("Sand");
        }

        RealmResults<Unit> units = realm.where(Unit.class).equalTo("name", "Bag").findAll();
        if (units.size() == 0) {
            new Unit("BAG").save();
        }
        units = realm.where(Unit.class).equalTo("name", "CFT").findAll();
        if (units.size() == 0) {
            new Unit("CFT").save();
        }
    }
//    public static ClientRecord instance1 = new ClientRecord(new Record(LocalDateTime.now(), Item.get("Cement"), 1, Unit.get("Bag"), 560, 0, "Kausar"));
//    public static ClientRecord instance2 = new ClientRecord(new Record(LocalDateTime.now(), Item.get("Sand"), 1, Unit.get("CFT"), 30, 0, "Rofiqul"));
//    public static ClientRecord instance3 = new ClientRecord(new Record(LocalDateTime.now(), Item.get("Cement"), 1, Unit.get("Bag"), 560, 0, "Dollar"));
//    public static ClientRecord instance4 = new ClientRecord(new Record(LocalDateTime.now(), Item.get("Sand"), 1, Unit.get("CFT"), 30, 0, "Rofiqul"));
//    public static ClientRecord instance5 = new ClientRecord(new Record(LocalDateTime.now(), Item.get("Cement"), 1, Unit.get("Bag"), 560, 0, "Dollar"));
//    public static ClientRecord instance6 = new ClientRecord(new Record(LocalDateTime.now(), Item.get("Sand"), 1, Unit.get("CFT"), 30, 0, "Rofiqul"));
}
