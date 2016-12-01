package com.app.talentum.cubelizer.cubelizer.persistencia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.app.talentum.cubelizer.cubelizer.LoginActivity;

import java.util.HashMap;


public class UserSessionManager {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;
    private static final String PREFER_NAME = "AndroidExample";
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    public static final String KEY_USER = "name";
    public static final String KEY_PASS = "pass";

    //Constructor
    public UserSessionManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFER_NAME,PRIVATE_MODE);
        editor = sharedPreferences.edit();


    }
    public void createUserLoginSession(String name, String pass){
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_USER, name);
        editor.putString(KEY_PASS, pass);
        editor.commit();
    }
    public boolean checkLogin(){
        if(!this.isUserLoggedIn()){
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            return true;
        }
        return false;
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String,String>user = new HashMap<>();
        user.put(KEY_USER, sharedPreferences.getString(KEY_USER,null));
        user.put(KEY_PASS, sharedPreferences.getString(KEY_PASS,null));
        return user;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent i = new Intent (context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
    public boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean(IS_USER_LOGIN, false);
    }
}
