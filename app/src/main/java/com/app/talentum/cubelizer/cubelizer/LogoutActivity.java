package com.app.talentum.cubelizer.cubelizer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.talentum.cubelizer.cubelizer.persistencia.UserSessionManager;

import java.util.HashMap;

public class LogoutActivity extends AppCompatActivity {
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        session = new UserSessionManager(getApplicationContext());
        TextView tvUsuario = (TextView)findViewById(R.id.tvUsuario);
        Button btnLogout = (Button)findViewById(R.id.btnLogout);

        if(session.checkLogin()){
            finish();
        }
        HashMap<String,String>user = session.getUserDetails();
        String name =  user.get(UserSessionManager.KEY_USER);

        tvUsuario.setText(name);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
            }
        });

    }
}
