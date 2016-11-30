package com.app.talentum.cubelizer.cubelizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.app.talentum.cubelizer.cubelizer.persistencia.UserSessionManager;

/**
 * Created by Verónica on 26/11/2016.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);/*
        Context context= this;
        String user = PreferenciasFragment.getString(context,"KEY_USER_ED");
        String pass = PreferenciasFragment.getKeyPassEd();
        Log.i(user,"Dato guardo en preferencias");
        Log.i(pass,"Dato guardo en preferencias");*/
        UserSessionManager session  = new UserSessionManager(getApplicationContext());

        if(session.checkLogin()){
            Intent intent = new Intent(this,MainActivity.class );
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}

