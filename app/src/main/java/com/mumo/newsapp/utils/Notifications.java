package com.mumo.newsapp.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.mumo.newsapp.HomeActivity;
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

        Intent intent = new Intent(context, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,PendingIntent.FLAG_IMMUTABLE);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/*");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Mumo is inviting you to join NewsApp");
        shareIntent.putExtra(Intent.EXTRA_TITLE, "Join NewsApp");

        PendingIntent sharePendingIntent = PendingIntent.getActivity(context, 0, shareIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_explore)
                .setContentTitle(title)
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_baseline_share, "Share", sharePendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        return builder;
    }

    public NotificationCompat.Builder bigTextNotification(String title, String text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_explore)
                .setContentTitle(title)
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        return builder;
    }
    public NotificationCompat.Builder bigImageNotification(String title, Bitmap image) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        intent.putExtra(Intent.EXTRA_TEXT, "Random Text");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_explore)
                .setContentTitle(title)
                .setLargeIcon(image)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(image)
                        .bigLargeIcon(null))
                .addAction(R.drawable.ic_baseline_share, "Share", pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        return builder;
    }
}
