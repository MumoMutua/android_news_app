package com.mumo.newsapp.utils;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.mumo.newsapp.Networking.ChatServiceGenerator;
import com.mumo.newsapp.Networking.ServiceGenerator;
import com.mumo.newsapp.Networking.pojos.ChatResponse;
import com.mumo.newsapp.ObjectBox;
import com.mumo.newsapp.R;
import com.mumo.newsapp.models.Chat;

import java.util.List;

import io.objectbox.Box;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyServices extends Service {

    MediaPlayer mediaPlayer;
    private Box<Chat> chatBox = ObjectBox.get().boxFor(Chat.class);
    private NotificationManager notificationManager;
    private static final int CHAT_SYNC_NOTIFICATION_ID = 132;

    @Override
    public void onCreate() {
        super.onCreate();

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.song);
//        mediaPlayer.start();

        syncChats(new PreferenceStorage(getApplicationContext()).getUserToken());

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mediaPlayer.release();
//        mediaPlayer = null;
    }

    public void syncChats(String token){

        notificationManager.notify(CHAT_SYNC_NOTIFICATION_ID, new Notifications(getApplicationContext()).chatSyncNotification().build());

        Call<List<ChatResponse>> call = ChatServiceGenerator.getInstance()
                .getApiConnector().syncChats("Token "+ token, chatBox.getAll().size());

        call.enqueue(new Callback<List<ChatResponse>>() {
            @Override
            public void onResponse(Call<List<ChatResponse>> call, Response<List<ChatResponse>> response) {

               if (response.code()==200 && response.body() !=null){
                   if (response.body().size() > chatBox.getAll().size()){
                       List<ChatResponse> chats = response.body();
                       chatBox.removeAll();
                       for (int i=0; i<chats.size();i++){
                           chatBox.put(
                                   new Chat(
                                           chats.get(i).getUser_Id(),
                                           chats.get(i).getUserFrom(),
                                           chats.get(i).getUserFromName(),
                                           chats.get(i).getUserTo(),
                                           chats.get(i).getUserToName(),
                                           chats.get(i).getMessage(),
                                           chats.get(i).getDateCreated()
                                   )
                           );
                       }
                   }
               }
               else {
                   Log.d("TESTS::", "onResponse: "+response.message()+response.code());
               }
//               notificationManager.cancel(CHAT_SYNC_NOTIFICATION_ID);
            }

            @Override
            public void onFailure(Call<List<ChatResponse>> call, Throwable t) {

                Log.d("TESTS::", "onFailure: "+t.getMessage());
            }
        });
    }
}
