package com.sajjadsjat.model;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;

public class Client extends RealmObject {
    @PrimaryKey
    private long id;
    private String name;

    @Nullable
    private String fathersName;
    private String phone;
    private String union;
    private String village;
    private String para;
    @Nullable
    private String extraIdentifier;
    @LinkingObjects("client")
    public final RealmResults<Record> records;
    @LinkingObjects("client")
    public final RealmResults<Payment> payments;


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.save();
    }

    @Nullable
    public String getFathersName() {
        return fathersName;
    }

    public void setFathersName(@Nullable String fathersName) {
        this.fathersName = fathersName;
        this.save();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        this.save();
    }

    public String getUnion() {
        return union;
    }

    public void setUnion(String union) {
        this.union = union;
        this.save();
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
        this.save();
    }

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
        this.save();
    }

    @Nullable
    public String getExtraIdentifier() {
        return extraIdentifier;
    }

    public void setExtraIdentifier(@Nullable String extraIdentifier) {
        this.extraIdentifier = extraIdentifier;
        this.save();
    }

    public Client() {
        this.records = null;
        this.payments = null;
    }

    public Client(String name, @androidx.annotation.Nullable String fathersName, String phone, String union, String village, String para, @androidx.annotation.Nullable String extraIdentifier) {
        Realm realm = Realm.getDefaultInstance();
        Number maxId = realm.where(Client.class).max("id");
        this.id = maxId == null ? 1 : maxId.intValue() + 1;
        if (validateNameFatherName(name, fathersName) != null) {
            throw new RuntimeException(validateNameFatherName(name, fathersName));
        }
        this.name = name;
        this.fathersName = fathersName;
        if (validatePhone(phone) != null) {
            throw new RuntimeException(validatePhone(phone));
        }
        this.phone = phone;
        this.union = union;
        this.village = village;
        if (validateNameParaExtra(name, para, extraIdentifier) != null) {
            throw new RuntimeException(validateNameParaExtra(name, para, extraIdentifier));
        }
        this.para = para;
        this.extraIdentifier = extraIdentifier;
        this.records = null;
        this.payments = null;
        this.save();
    }

    static String validatePhone(@androidx.annotation.Nullable String phone) {
        if (phone == null) {
            return "Phone cannot be null";
        }
        if (phone.length() != 11) {
            return "Phone must be 11 digits";
        }
        if (!phone.startsWith("01")) {
            return "Phone must start with 01";
        }
        try {
            Long.parseLong(phone);
        } catch (NumberFormatException e) {
            return "Phone must be a number";
        }
        RealmResults<Client> clients = Realm.getDefaultInstance().where(Client.class).equalTo("phone", phone).findAll();
        if (clients.size() > 0) {
            return "Client with same phone already exists";
        }
        return null;
    }

    static String validateNameFatherName(@androidx.annotation.Nullable String name, @androidx.annotation.Nullable String fathersName) {
        if (name == null) {
            return "Name cannot be null";
        }
        if (fathersName == null) {
            return "Father's name cannot be null";
        }
        RealmResults<Client> clients = Realm.getDefaultInstance().where(Client.class).equalTo("name", name).equalTo("fathersName", fathersName).findAll();
        if (clients.size() > 0) {
            return "Client with same name and father's name already exists";
        }
        return null;
    }

    static String validateNameParaExtra(@androidx.annotation.Nullable String name, @androidx.annotation.Nullable String para, @androidx.annotation.Nullable String extraIdentifier) {
        if (name == null) {
            return "Name cannot be null";
        }
        if (para == null) {
            return "Para cannot be null";
        }
        if (extraIdentifier == null) {
            return "Extra identifier cannot be null";
        }
        RealmResults<Client> clients = Realm.getDefaultInstance().where(Client.class).equalTo("name", name).equalTo("para", para).equalTo("extraIdentifier", extraIdentifier).findAll();
        if (clients.size() > 0) {
            return "Client with same name, para and extra identifier already exists";
        }
        return null;
    }

    void validate() {
        if (validateNameFatherName(name, fathersName) != null) {
            throw new RuntimeException(validateNameFatherName(name, fathersName));
        }
        if (validatePhone(phone) != null) {
            throw new RuntimeException(validatePhone(phone));
        }
        if (validateNameParaExtra(name, para, extraIdentifier) != null) {
            throw new RuntimeException(validateNameParaExtra(name, para, extraIdentifier));
        }
    }

    private void save() {
        this.validate();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(this);
        realm.commitTransaction();
    }

    public static Client get(long id) {
        return Realm.getDefaultInstance().where(Client.class).equalTo("id", id).findFirst();
    }

    public static List<Client> get(HashMap<String, String> filters) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Client> clients = realm.where(Client.class).findAll();
        for (String key : filters.keySet()) {
            clients = clients.where().equalTo(key, filters.get(key)).findAll();
        }
        return Realm.getDefaultInstance().where(Client.class).findAll();
    }

    public static void delete(long id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Client client = realm.where(Client.class).equalTo("id", id).findFirst();
        if (client != null) {
            client.deleteFromRealm();
        }
        realm.commitTransaction();
    }
}
