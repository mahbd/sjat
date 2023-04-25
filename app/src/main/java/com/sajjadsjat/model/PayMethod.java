package com.sajjadsjat.model;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;

public class PayMethod extends RealmObject {
    public String name;
    @LinkingObjects("payMethod")
    public final RealmResults<Payment> payments;

    public PayMethod() {
        this.payments = null;
    }
}
