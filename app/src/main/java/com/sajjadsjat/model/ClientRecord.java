package com.sajjadsjat.model;

public class ClientRecord {
    public Record record;
    public Payment payment;

    public ClientRecord(Record record) {
        this.record = record;
    }

    public ClientRecord(Payment payment) {
        this.payment = payment;
    }
}
