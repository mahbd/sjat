package com.sajjadsjat.model;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;

public class Client extends RealmObject {
    @PrimaryKey
    private long id;
    private Address address;
    private double due;
    @Nullable
    private String extra;
    private String name;
    @Nullable
    private String fathersName;
    private String phone;

    public static String formatName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "";
        }
        name = name.trim();
        name = name.toLowerCase();
        String[] words = name.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append(word.substring(0, 1).toUpperCase()).append(word.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    public long getId() {
        return id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        if (address == null) {
            throw new RuntimeException("Address is null");
        }
        if (address.equals(this.address)) {
            return;
        }
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        this.address = address;
        realm.commitTransaction();
    }

    public double getDue() {
        return due;
    }

    public void setDue(double due) {
        if (this.due == due) {
            return;
        }
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        this.due = due;
        realm.commitTransaction();
    }

    public Client() {
    }

    @Nullable
    public String getExtra() {
        if (extra == null) {
            return "";
        }
        return extra;
    }

    public void setExtra(@Nullable String extra) {
        if (extra == null) {
            extra = "";
        }
        if (extra.equals(this.extra)) {
            return;
        }
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        this.extra = extra;
        realm.commitTransaction();
    }

    @Nullable
    public String getFathersName() {
        return fathersName;
    }

    public void setFathersName(@Nullable String fathersName) {
        fathersName = formatName(fathersName);
        if (validateNameFatherName(name, fathersName) != null) {
            throw new RuntimeException(validateNameFatherName(name, fathersName));
        }
        if (fathersName.equals(this.fathersName)) {
            return;
        }
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        this.fathersName = fathersName;
        realm.commitTransaction();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = formatName(name);
        if (validateNameFatherName(name, fathersName) != null) {
            throw new RuntimeException(validateNameFatherName(name, fathersName));
        }
        if (name.equals(this.name)) {
            return;
        }
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        this.name = name;
        realm.commitTransaction();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (validatePhone(phone) != null) {
            throw new RuntimeException(validatePhone(phone));
        }
        if (phone.equals(this.phone)) {
            return;
        }
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        this.phone = phone;
        realm.commitTransaction();
    }

    public Client(String name, @androidx.annotation.Nullable String fathersName, String phone, Address address, @androidx.annotation.Nullable String extra) {
        Realm realm = Realm.getDefaultInstance();
        Number maxId = realm.where(Client.class).max("id");
        this.id = maxId == null ? 1 : maxId.intValue() + 1;
        if (validateNameFatherName(formatName(name), formatName(fathersName)) != null) {
            throw new RuntimeException(validateNameFatherName(formatName(name), formatName(fathersName)));
        }
        this.name = formatName(name);
        this.fathersName = formatName(fathersName);
        if (validatePhone(phone) != null) {
            throw new RuntimeException(validatePhone(phone));
        }
        this.phone = phone;
        if (address == null) {
            throw new RuntimeException("Address is null");
        }
        this.address = address;
        this.extra = extra;
        this.save();
    }

    public static String validatePhone(@androidx.annotation.Nullable String phone) {
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

    void validate() {
        if (validateNameFatherName(name, fathersName) != null) {
            throw new RuntimeException(validateNameFatherName(name, fathersName));
        }
        if (validatePhone(phone) != null) {
            throw new RuntimeException(validatePhone(phone));
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

    public static RealmResults<Client> getAll() {
        return Realm.getDefaultInstance().where(Client.class).findAll().sort("due", Sort.DESCENDING);
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
