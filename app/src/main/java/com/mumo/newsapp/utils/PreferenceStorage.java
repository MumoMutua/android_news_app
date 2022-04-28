package com.mumo.newsapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.mumo.newsapp.Networking.pojos.UserResponse;

public class PreferenceStorage {

    SharedPreferences sharedPreferences;
    private Context context;

    private final String USER_NAME = "com.mumo.newsapp.utils.USER_NAME";
    private final String USER_EMAIL = "com.mumo.newsapp.utils.USER_EMAIL";
    private final String USER_TOKEN = "com.mumo.newsapp.utils.USER_TOKEN";
    private final String USER_NUMBER = "com.mumo.newsapp.utils.USER_NUMBER";
    private final String SHARED_PREF_NAME = "com.mumo.newsapp.utils.SHARED_PREF_NAME";
    private final String IS_AUTHENTICATED = "com.mumo.newsapp.utils.IS_AUTHENTICATED";
    private static final String USER_ID = "com.mumo.newsapp.utils.USER_ID";

    public PreferenceStorage(Context context) {
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        this.context = context;
    }

    public void setUserData(UserResponse userResponse){

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USER_NAME, userResponse.getUsername());
        editor.putString(USER_EMAIL, userResponse.getEmail());
        editor.putString(USER_NUMBER, userResponse.getPhoneNumber());
        editor.putString(USER_TOKEN, userResponse.getToken());
        editor.putInt(USER_ID, userResponse.getId());
        editor.apply();
    }

    public String getUserToken(){
        return sharedPreferences.getString(USER_TOKEN, "");
    }
    public String getUserName(){
        return sharedPreferences.getString(USER_NAME, "");
    }
    public int getUserId(){
        return sharedPreferences.getInt(USER_ID, 0);
    }

    public boolean isAuthenticated(){
        return sharedPreferences.getBoolean(IS_AUTHENTICATED, false);
    }
    public void setAuthStatus(Boolean status){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_AUTHENTICATED, status);
        editor.apply();
    }

    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_AUTHENTICATED, false);
        editor.apply();
    }
}
