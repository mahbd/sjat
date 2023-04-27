package com.sajjadsjat.utils;

import android.telephony.SmsManager;

import com.sajjadsjat.model.Client;
import com.sajjadsjat.model.Record;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class H {
    public static long stringToNumber(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static double stringToDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static long datetimeToTimestamp(LocalDateTime datetime) {
        return datetime.toInstant(ZoneOffset.UTC).toEpochMilli() / 1000;
    }

    public static void sendMessage(Client client, boolean isPaid) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SmsManager smsManager = SmsManager.getDefault();
        String phoneNumber = client.getPhone();
        StringBuilder lastFewTransaction = new StringBuilder();
        RealmResults<Record> records = Realm.getDefaultInstance().where(Record.class).equalTo("client.id", client.getId()).sort("createdAt", Sort.DESCENDING).limit(5).findAll();
        if (records.size() > 0) {
            lastFewTransaction = new StringBuilder("Last few transactions:\n");
            for (Record record : records) {
                if (record.getItem().equals("Payment")) {
                    lastFewTransaction.append(record.getDateTimeShort()).append(" ").append(record.getDiscount()).append(" taka\n");
                } else {
                    lastFewTransaction.append(record.getDateTimeShort()).append(" ").append(record.getQuantity()).append(" ").append(record.getUnit()).append(" ").append(record.getItem()).append(" ").append(record.getTotal()).append(" taka\n");
                }
            }
        }
        String message = "Dear " + client.getName() + ",\n" +
                "Your due is " + client.getDue() + " taka.\n" +
                "Please pay your due as soon as possible.\n" +
                lastFewTransaction +
                "Thank you.";
        if (isPaid) {
            // all due has been paid
            message = "Dear " + client.getName() + ",\n" +
                    "I am happy to inform you that all dues have been paid. Thank you for your cooperation.\n" +
                    "Best regards,\n" +
                    "Md. Ibrahim Khalil";
        }
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }

    public static void sendMessage(Client client) {
        sendMessage(client, false);
    }
}
