package com.mumo.newsapp.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.mumo.newsapp.R;

public class Notifications {

    public static final String CHANNEL_ID = "com.mumo.newsapp.register_notification";
    private Context context;

    /**
     * This function registers our notification with the Android System
     * Without Notification & Notification Channel, we cannot display notifications from our app
    */
    public void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            /*
             This will only apply to Android 8 (API 26) and above
             Android 7 and below registers notifications in a different way
             */

            CharSequence name = "NewsApp Notifications";
            String description = "Registration notification from newsapp";

            /*
            This Variable will let the android system know how to display the notification
            Notifications with higher importance will be able to interrupt user activity
            Those with less importance will be displayed silently
             */

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    public Notifications(Context context) {
        this.context = context;
    }

    public NotificationCompat.Builder registerNotification (String title, String text){
        //This method will be used to build and add the necessary details

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_explore)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        return builder;
    }
}
