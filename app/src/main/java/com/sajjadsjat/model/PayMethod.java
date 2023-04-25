package com.sajjadsjat.model;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;

public class PayMethod extends RealmObject {
    private String name;
    @LinkingObjects("payMethod")
    public final RealmResults<Payment> payments;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.save();
    }

    public PayMethod() {
        this.payments = null;
    }

    public PayMethod(String name) {
        this.name = name;
        this.payments = null;
        this.save();
    }

    private void save() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(this);
        realm.commitTransaction();
    }

    public static PayMethod get(String name) {
        Realm realm = Realm.getDefaultInstance();
        PayMethod payMethod = realm.where(PayMethod.class).equalTo("name", name).findFirst();
        if (payMethod == null) {
            payMethod = new PayMethod(name);
        }
        return payMethod;
    }
}
