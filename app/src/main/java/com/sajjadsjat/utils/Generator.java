package com.sajjadsjat.utils;

import android.content.Context;

import com.sajjadsjat.R;
import com.sajjadsjat.model.Address;
import com.sajjadsjat.model.Client;
import com.sajjadsjat.model.Employee;
import com.sajjadsjat.model.Price;
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

    public static Employee generateRandomEmployee() {
        String name = generateRandomString(Generator.generateRandomInt(5, 20));
        String job = generateRandomString(Generator.generateRandomInt(5, 10));
        String phone = generateRandomPhone();
        return new Employee(name, job, phone);
    }

    public static Employee getRandomEmployee() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Employee> employees = realm.where(Employee.class).findAll();
        int total = employees.size();
        if (total == 0) {
            return generateRandomEmployee();
        }
        int random = (int) (Math.random() * total);
        return employees.get(random);
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

    public static Price generateRandomPrice() {
        String item = generateRandomString(Generator.generateRandomInt(5, 20));
        String[] units = {"kg", "gm", "ltr", "ml", "pc", "dozen", "box", "packet", "bottle", "can", "bag", "sack", "bundle", "pair", "set", "roll", "sheet"};
        String unit = units[generateRandomInt(0, units.length - 1)];
        double price = generateRandomDouble(0, 1000);
        return new Price(item, price, unit);
    }

    public static Price getRandomPrice() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Price> prices = realm.where(Price.class).findAll();
        int total = prices.size();
        if (total == 0) {
            return generateRandomPrice();
        }
        int random = (int) (Math.random() * total);
        return prices.get(random);
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

    public static Record generateRandomRecord(Context context) {
        Client client = getRandomClient();
        LocalDateTime date = LocalDateTime.of(generateRandomInt(2020, 2023), generateRandomInt(1, 12), generateRandomInt(1, 28), generateRandomInt(0, 23), generateRandomInt(0, 59));
        long createdAt = H.datetimeToTimestamp(date);
        double discount = generateRandomDouble(0, 1000);
        Price price = getRandomPrice();
        double quantity = generateRandomDouble(0, 100);
        String seller = getRandomEmployee().getName();
        return new Record(client, createdAt, discount, price.getItem(), quantity, seller, price.getUnit(), price.getPrice());
    }
}
