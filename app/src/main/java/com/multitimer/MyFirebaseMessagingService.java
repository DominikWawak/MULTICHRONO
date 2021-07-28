package com.multitimer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull @org.jetbrains.annotations.NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        String title = remoteMessage.getNotification().getTitle();
        String body =  remoteMessage.getNotification().getBody();

        Map<String,String> extraData = remoteMessage.getData();

        String startTime = extraData.get("startTime");



        //etc,,,,,,,,,,,

        NotificationCompat.Builder notificationBuilder= new NotificationCompat.Builder(this,"CAT")
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_launcher_background);

        // Can add if statements based on the data to direct users to different intents.
        //https://www.youtube.com/watch?v=c_WyIUhcRCE&list=PL2sOBQWr1QlOgTPmwEtQ3SASLEial0wZ8&index=4

        Intent intent = new Intent(this,RecieveNotificationActivity.class);
        //Intent intent = new Intent(this,MainActivity.class);

        intent.putExtra("startTime",startTime);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }

        //notificationBuilder.setContentIntent(pendingIntent);


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        int id = (int) System.currentTimeMillis();

        if(Build.VERSION.SDK_INT >- Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("CAT","demo",NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);

        }

        notificationManager.notify(id, notificationBuilder.build());








    }
}
