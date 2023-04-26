package com.sajjadsjat.model;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;

public class Item {
//    @PrimaryKey
//    private String name;
//    @LinkingObjects("item")
//    public final RealmResults<Record> records;
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//        this.save();
//    }
//
//    public Item() {
//        this.records = null;
//    }
//
//    public Item(String name) {
//        if (validateName(name) != null) {
//            throw new IllegalArgumentException(validateName(name));
//        }
//        this.name = name;
//        this.records = null;
//        this.save();
//    }
//
//    public String validateName(@Nullable String name) {
//        if (name == null) {
//            return "Name cannot be null";
//        }
//        Realm realm = Realm.getDefaultInstance();
//        RealmResults<Item> items = realm.where(Item.class).equalTo("name", name).findAll();
//        if (items.size() > 0) {
//            return "Item with same name already exists";
//        }
//        return null;
//    }
//
//    private void save() {
//        validateName(this.name);
//        Realm realm = Realm.getDefaultInstance();
//        realm.beginTransaction();
//        realm.copyToRealmOrUpdate(this);
//        realm.commitTransaction();
//    }
//
//    public static Item get(String name) {
//        Realm realm = Realm.getDefaultInstance();
//        RealmResults<Item> items = realm.where(Item.class).equalTo("name", name).findAll();
//        if (items.size() > 0) {
//            return items.get(0);
//        }
//        return null;
//    }
//
//    public static List<Item> get(HashMap<String, String> filters) {
//        Realm realm = Realm.getDefaultInstance();
//        RealmResults<Item> items = realm.where(Item.class).findAll();
//        for (String key : filters.keySet()) {
//            items = items.where().equalTo(key, filters.get(key)).findAll();
//        }
//        return items;
//    }
//
//    public static void delete(String name) {
//        Realm realm = Realm.getDefaultInstance();
//        realm.beginTransaction();
//        Item item = Item.get(name);
//        if (item != null) {
//            item.deleteFromRealm();
//        }
//        realm.commitTransaction();
//    }
}
