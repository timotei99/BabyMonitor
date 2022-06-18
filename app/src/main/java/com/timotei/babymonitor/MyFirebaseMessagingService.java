package com.timotei.babymonitor;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Objects;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String TAG="FIREBASE_MESSAGING";
    private String channelId="1234";

    public MyFirebaseMessagingService() {
    }

    @Override
    public void onNewToken(@NonNull String token) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        //v.vibrate(2000);

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            String id = remoteMessage.getData().get("id");
            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("body");
            Log.d(TAG, "Message data payload: " + id);

            sendLocal(title,message,id);
            //startAlert(payload);

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getNotification().getBody();
            Log.d(TAG, "Message Notification Title: " + title);
            Log.d(TAG, "Message Notification Body: " + message);
        }

        super.onMessageReceived(remoteMessage);
    }

    /*private void startAlert(String payload){
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.putExtra("id",payload);
        intent.setAction(Long.toString(System.currentTimeMillis()));
        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 *//* Request code *//*, intent,
                PendingIntent.FLAG_ONE_SHOT);
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }*/

    // send local notification
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendLocal(String title, String message,String id){
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.putExtra("id",id);
        intent.setAction(Long.toString(System.currentTimeMillis())); // extras are not added if there is no action

        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent pendingIntent=PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        Uri sound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        NotificationChannel notificationChannel= new NotificationChannel(
                    channelId,
                    "NOTIFICATION_CHANNEL",
                    NotificationManager.IMPORTANCE_HIGH
        );
        notificationChannel.setVibrationPattern(new long[]{0,1000,1000,1000,1000,1000});
        notificationChannel.setSound(sound,null);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(notificationChannel);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,channelId)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(message)
                .setSound(sound)
                .setSmallIcon(R.drawable.crying);
        notificationManager.notify(1234,builder.build());
    }

}