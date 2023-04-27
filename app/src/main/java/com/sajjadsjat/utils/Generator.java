package com.sajjadsjat.utils;

import android.content.Context;

import com.sajjadsjat.R;
import com.sajjadsjat.model.Address;
import com.sajjadsjat.model.Client;
import com.sajjadsjat.model.Payment;
import com.sajjadsjat.model.Record;

import java.time.LocalDateTime;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;

public class Generator {
    public static String generateRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder builder = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            builder.append(characters.charAt(index));
        }

        return builder.toString();
    }

    public static String generateRandomPhone() {
        String[] prefixes = {"017", "018", "019", "015", "016"};
        String prefix = prefixes[generateRandomInt(0, prefixes.length - 1)];
        String characters = "1234567890";
        StringBuilder builder = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(characters.length());
            builder.append(characters.charAt(index));
        }

        String number = builder.toString();
        return prefix + number;
    }

    public static int generateRandomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public static double generateRandomDouble(double min, double max) {
        Random random = new Random();
        return min + (max - min) * random.nextDouble();
    }

    public static Address generateRandomAddress() {
        String para = generateRandomString(Generator.generateRandomInt(5, 15));
        String village = generateRandomString(Generator.generateRandomInt(5, 15));
        String union = generateRandomString(Generator.generateRandomInt(5, 15));
        return new Address(para, village, union);
    }

    public static Address getRandomAddress() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Address> address = realm.where(Address.class).findAll();
        int total = address.size();
        if (total == 0) {
            return generateRandomAddress();
        }
        int random = (int) (Math.random() * total);
        return address.get(random);
    }

    public static Client generateRandomClient() {
        Realm realm = Realm.getDefaultInstance();
        Address address = getRandomAddress();
        double due = generateRandomDouble(0, 100000);
        String extra = generateRandomString(Generator.generateRandomInt(0, 20));
        String name = generateRandomString(Generator.generateRandomInt(5, 20));
        String fathersName = generateRandomString(Generator.generateRandomInt(5, 20));
        String phone = generateRandomPhone();
        Client client = new Client(name, fathersName, phone, address, extra);
        client.setDue(due);
        return client;
    }

    public static Client getRandomClient() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Client> clients = realm.where(Client.class).findAll();
        int total = clients.size();
        if (total == 0) {
            return generateRandomClient();
        }
        int random = (int) (Math.random() * total);
        return clients.get(random);
    }

    public static Payment generateRandomPayment(Context context) {
        double amount = generateRandomDouble(0, 100000);
        Client client = getRandomClient();
        LocalDateTime date = LocalDateTime.of(generateRandomInt(2020, 2023), generateRandomInt(1, 12), generateRandomInt(1, 28), generateRandomInt(0, 23), generateRandomInt(0, 59));
        long createdAt = H.datetimeToTimestamp(date);
        boolean isDue = generateRandomInt(0, 1) == 1;
        String payMethod = "Cash";
        String[] employees = context.getResources().getStringArray(R.array.arrays_employees);
        String receiver = employees[generateRandomInt(0, employees.length - 1)];
        return new Payment(amount, client, createdAt, isDue, payMethod, receiver);
    }

    public static Record generateRandomRecord(Context context) {
        Client client = getRandomClient();
        LocalDateTime date = LocalDateTime.of(generateRandomInt(2020, 2023), generateRandomInt(1, 12), generateRandomInt(1, 28), generateRandomInt(0, 23), generateRandomInt(0, 59));
        long createdAt = H.datetimeToTimestamp(date);
        double discount = generateRandomDouble(0, 1000);
        String[] items = context.getResources().getStringArray(R.array.arrays_items);
        String item = items[generateRandomInt(0, items.length - 1)];
        double quantity = generateRandomDouble(0, 100);
        String[] employees = context.getResources().getStringArray(R.array.arrays_employees);
        String seller = employees[generateRandomInt(0, employees.length - 1)];
        String[] units = context.getResources().getStringArray(R.array.arrays_units);
        String unit = units[generateRandomInt(0, units.length - 1)];
        double unitPrice = generateRandomDouble(0, 1000);
        return new Record(client, createdAt, discount, item, quantity, seller, unit, unitPrice);
    }
}
