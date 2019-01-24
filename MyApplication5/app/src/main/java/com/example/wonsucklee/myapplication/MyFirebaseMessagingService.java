package com.example.wonsucklee.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.Policy;


public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";

    String sender_name = null;
    String sender_major = null;
    String message = null;


    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //추가한것
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "data: " + remoteMessage.getData());

        JSONObject jsonObject = new JSONObject(remoteMessage.getData());
        try {
            sender_name = jsonObject.getString("sender_name");
            sender_major = jsonObject.getString("sender_major");
            message = jsonObject.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendNotification(message);
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, Frag_MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.jsea);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String msg_to_show = "";

        if(message.length() >= 10) {
            msg_to_show = message.substring(0, 10) + "...";
        } else {
            msg_to_show = message;
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.jsea))
                .setSmallIcon(R.drawable.jsea)
                .setContentTitle("F니까청춘이다")
                .setContentText(messageBody) //메세지
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle(notificationBuilder);
        style.bigText(msg_to_show).setBigContentTitle(sender_name + "(" + sender_major + ")");
        /**
         * setBigContentTitle : 보낸이
         * style.bingTest : 메시지내용
         */

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}

