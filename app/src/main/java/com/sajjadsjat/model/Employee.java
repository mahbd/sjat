package com.sajjadsjat.model;

import androidx.annotation.NonNull;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

public class Employee extends RealmObject {
    @PrimaryKey
    private long id;
    private String name;
    private String job;
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

    @NonNull
    public String toString() {
        return name + " (" + job + ")" + " - " + phone;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = formatName(name);
        if (this.name.equals(name)) {
            return;
        }
        if (validateName(name) != null) {
            throw new RuntimeException(validateName(name));
        }
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        this.name = name;
        realm.commitTransaction();
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        job = formatName(job);
        if (this.job.equals(job)) {
            return;
        }
        if (job.isEmpty()) {
            throw new RuntimeException("Job is empty");
        }
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        this.job = job;
        realm.commitTransaction();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (this.phone.equals(phone)) {
            return;
        }
        if (validatePhone(phone) != null) {
            throw new RuntimeException(validatePhone(phone));
        }
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        this.phone = phone;
        realm.commitTransaction();
    }

    public Employee() {
    }

    public Employee(String name, String job, String phone) {
        name = formatName(name);
        job = formatName(job);
        if (validateName(name) != null) {
            throw new RuntimeException(validateName(name));
        }
        this.name = name;
        if (job.isEmpty()) {
            throw new RuntimeException("Job is empty");
        }
        this.job = job;
        if (validatePhone(phone) != null) {
            throw new RuntimeException(validatePhone(phone));
        }
        this.phone = phone;
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Number maxId = realm.where(Employee.class).max("id");
        this.id = maxId == null ? 1 : maxId.longValue() + 1;
        realm.copyToRealmOrUpdate(this);
        realm.commitTransaction();
    }

    public static String validateName(String name, boolean checkExistence) {
        name = formatName(name);
        if (name.isEmpty()) {
            return "Name is empty";
        }
        if (checkExistence && Realm.getDefaultInstance().where(Employee.class).equalTo("name", name).count() > 0) {
            return "Name already exists";
        }
        return null;
    }

    public static String validateName(String name) {
        return validateName(name, true);
    }

    public static String validatePhone(@androidx.annotation.Nullable String phone, boolean checkExistence) {
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
        RealmResults<Employee> clients = Realm.getDefaultInstance().where(Employee.class).equalTo("phone", phone).findAll();
        if (checkExistence && clients.size() > 0) {
            return "Client with same phone already exists";
        }
        return null;
    }
    public static String validatePhone(String phone) {
        return validatePhone(phone, true);
    }

    public static boolean doesExist(String name, String job, String phone) {
        name = formatName(name);
        job = formatName(job);
        return Realm.getDefaultInstance().where(Employee.class).equalTo("name", name).equalTo("job", job).equalTo("phone", phone).count() > 0;
    }

    public static Employee get(long id) {
        return Realm.getDefaultInstance().where(Employee.class).equalTo("id", id).findFirst();
    }

    public static RealmResults<Employee> getAll() {
        return Realm.getDefaultInstance().where(Employee.class).findAll();
    }
}
