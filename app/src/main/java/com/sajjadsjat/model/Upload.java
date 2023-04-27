package com.sajjadsjat.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Upload extends RealmObject {
    @PrimaryKey
    private long key;
    private String name;
    private long valueId;
    private boolean isUploaded;

    public Upload() {
        this.key = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }

    public Upload(String name, long valueId) {
        this.key = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        this.name = name;
        this.valueId = valueId;
        this.isUploaded = false;
        Realm.getDefaultInstance().executeTransaction(r -> r.insertOrUpdate(this));
    }
}
