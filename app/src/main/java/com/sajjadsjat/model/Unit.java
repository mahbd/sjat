package com.sajjadsjat.model;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;

public class Unit extends RealmObject {
    @PrimaryKey
    public String name;
    @LinkingObjects("unit")
    public final RealmResults<Record> records;

    public Unit() {
        this.records = null;
    }

    void save() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(this);
        realm.commitTransaction();
    }

    static Unit get(String name) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Unit> units = realm.where(Unit.class).equalTo("name", name).findAll();
        if (units.size() > 0) {
            return units.get(0);
        }
        return null;
    }

    public Unit(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Unit> units = realm.where(Unit.class).equalTo("name", name).findAll();
        if (units.size() > 0) {
            throw new IllegalArgumentException("Unit with same name already exists");
        }
        this.name = name;
        this.records = null;
        this.save();
    }
}
