package com.sajjadsjat.model;
import java.util.Random;

public class Client {
    public int id;
    public String name;
    public String fathersName;
    public String phone;
    public String union;
    public String village;
    public String para;
    public String extraIdentifier;
    public Record[] records;
    public Payment[] payments;

    public Client(int id, String name, String fathersName, String phone, String union, String village, String para, String extraIdentifier, Record[] records, Payment[] payments) {
        this.id = id;
        this.name = name;
        this.fathersName = fathersName;
        this.phone = phone;
        this.union = union;
        this.village = village;
        this.para = para;
        this.extraIdentifier = extraIdentifier;
        this.records = records;
        this.payments = payments;
    }

    public Client() {
        this.id = new Random().nextInt(1000000);
        this.name = "";
        this.fathersName = "";
        this.phone = "";
        this.union = "";
        this.village = "";
        this.para = "";
        this.extraIdentifier = "";
        this.records = new Record[0];
        this.payments = new Payment[0];
    }
}
