package com.sajjadsjat.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import com.sajjadsjat.R;
import com.sajjadsjat.model.Client;
import com.sajjadsjat.model.Record;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class H {
    public static final String ITEM_DEPOSIT = "Deposit";
    public static final String ITEM_DISCOUNT = "Discount";
    public static final String ITEM_PREVIOUS = "Previous";

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

    public static void sendMessage(Context context, Client client, boolean isPaid) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String phoneNumber = client.getPhone();
        StringBuilder lastFewTransaction = new StringBuilder();
        RealmResults<Record> records = Realm.getDefaultInstance().where(Record.class).equalTo("client.id", client.getId()).sort("createdAt", Sort.DESCENDING).limit(3).findAll();
        if (records.size() > 0) {
            lastFewTransaction = new StringBuilder("Last 3 transactions:\n");
            for (Record record : records) {
                if (record.getItem().equals(ITEM_DEPOSIT)) {
                    lastFewTransaction.append(record.getDateTimeShort()).append(" deposit ").append(record.getDiscount()).append("tk\n");
                } else if (record.getItem().equals(ITEM_DISCOUNT)) {
                    lastFewTransaction.append(record.getDateTimeShort()).append(" all due PAID\n");
                } else if (record.getItem().equals(ITEM_PREVIOUS)) {
                    lastFewTransaction.append("Previous due ").append(record.getTotal()).append("tk\n");
                } else {
                    lastFewTransaction.append(record.getDateTimeShort()).append(" ").append(record.getQuantity()).append(" ").append(record.getUnit()).append(" ").append(record.getItem()).append(" ").append(record.getTotal()).append("tk\n");
                }
            }
        }
        String message = "Dear " + client.getName() + ",\n" +
                lastFewTransaction +
                "Your current due is " + client.getDue() + "tk.\n" +
                "-\nIbrahim Khalil";
        if (isPaid) {
            message = "Dear " + client.getName() + ",\n" +
                    "I am happy to inform you that all dues have been paid. Thank you for your cooperation.\n" +
                    "Best regards,\n" +
                    "Ibrahim Khalil";
        }
        if (message.length() > 160) {
            message = message.substring(0, 160);
        }
        new SMSHandler().sendSMS(context, phoneNumber, message);
    }

    public static void sendMessage(Context context, Client client) {
        sendMessage(context, client, false);
    }

    public static void sendEmail(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String emailUsername = prefs.getString("email_username", "");
        String emailPassword = prefs.getString("email_password", "");
        if (emailUsername.isEmpty() || emailPassword.isEmpty()) {
            Toast.makeText(context, "Please set email username and password in settings", Toast.LENGTH_LONG).show();
            return;
        }

        runBackground(() -> {
            new EmailSender(emailUsername, emailPassword).sendEmail("mahmudula2000@gmail.com", "Test", "Test");
        });
    }

    public static void sendEmail(Context context, String to, String subject, String message, File file) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String emailUsername = prefs.getString("email_username", "");
        String emailPassword = prefs.getString("email_password", "");
        if (emailUsername.isEmpty() || emailPassword.isEmpty()) {
            Toast.makeText(context, "Please set email username and password in settings", Toast.LENGTH_LONG).show();
            return;
        }

        runBackground(() -> {
            new EmailSender(emailUsername, emailPassword).sendEmail(to, subject, message, file);
        });
    }

    public static interface AlertCallback {
        public void onOk();

        public void onCancel();
    }

    public static interface SimpleAlertCallback {
        public void onOk();
    }

    public static void showAlert(Context context, String title, String message, AlertCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", (dialog, which) -> {
            callback.onOk();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            callback.onCancel();
        });
        builder.show();
    }

    public static void showAlert(Context context, String title, String message, SimpleAlertCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", (dialog, which) -> {
            callback.onOk();
        });
        builder.show();
    }

    public static interface BackgroundRun {
        public void run();
    }

    public static void runBackground(BackgroundRun backgroundRun) {
        Executor executor = Executors.newSingleThreadExecutor();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                backgroundRun.run();
            }
        });
    }

    public static interface AfterTextChanged {
        void afterTextChanged(Editable s);
    }

    public static TextWatcher createAfterTextChanged(AfterTextChanged afterTextChanged) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                afterTextChanged.afterTextChanged(s);
            }
        };
    }
}
