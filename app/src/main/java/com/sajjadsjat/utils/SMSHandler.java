package com.sajjadsjat.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SMSHandler {
    private static final String SMS_SENT = "SMS_SENT";
    private static final String SMS_DELIVERED = "SMS_DELIVERED";

    public void sendSMS(Context context, String phoneNumber, String message) {
        phoneNumber = "+88" + phoneNumber;
        SmsManager smsManager = SmsManager.getDefault();

        PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0, new Intent(SMS_SENT), PendingIntent.FLAG_IMMUTABLE);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(context, 0, new Intent(SMS_DELIVERED), PendingIntent.FLAG_IMMUTABLE);

        // SMS sent PendingIntent receiver
        BroadcastReceiver sentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic failure", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "No service", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "Null PDU", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Radio off", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        // SMS delivered PendingIntent receiver
        BroadcastReceiver deliveredReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context, "SMS not delivered", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        // Register the sent and delivered receivers
        context.registerReceiver(sentReceiver, new IntentFilter(SMS_SENT));
        context.registerReceiver(deliveredReceiver, new IntentFilter(SMS_DELIVERED));

        // Send the SMS message
        ArrayList<String> messages = smsManager.divideMessage(message);
        ArrayList<PendingIntent> sentIntents = new ArrayList<>(), deliveredIntents = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++) {
            sentIntents.add(sentIntent);
            deliveredIntents.add(deliveredIntent);
        }
        smsManager.sendMultipartTextMessage(phoneNumber, null, messages, sentIntents, deliveredIntents);
    }

}
