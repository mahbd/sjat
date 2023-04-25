package com.sajjadsjat.model;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;

public class Item extends RealmObject {
    @PrimaryKey
    public String name;
    @LinkingObjects("item")
    public final RealmResults<Record> records;

    public Item() {
        this.records = null;
    }

    void save() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(this);
        realm.commitTransaction();
    }

    static Item get(String name) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Item> items = realm.where(Item.class).equalTo("name", name).findAll();
        if (items.size() > 0) {
            return items.get(0);
        }
        return null;
    }

    public Item(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Item> items = realm.where(Item.class).equalTo("name", name).findAll();
        if (items.size() > 0) {
            throw new IllegalArgumentException("Item with same name already exists");
        }
        this.name = name;
        this.records = null;
        this.save();
    }
}
