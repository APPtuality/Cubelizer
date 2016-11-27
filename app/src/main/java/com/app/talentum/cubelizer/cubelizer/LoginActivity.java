package com.app.talentum.cubelizer.cubelizer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;


    //@InjectView(R.id.input_email) EditText _emailText;
    //@InjectView(R.id.input_password) EditText _passwordText;
    //@InjectView(R.id.btn_login) Button _loginButton;
    EditText userText;
    EditText passwordText;
    Button loginButton;;
    String pass;
    String user;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        userText = (EditText)findViewById(R.id.input_user);
        passwordText = (EditText)findViewById(R.id.input_password);
        //ButterKnife.inject(this);
        loginButton = (Button)findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
    }


    public void login() {
        Log.d(TAG, "Login");

        user = userText.getText().toString();
        pass = passwordText.getText().toString();
        if(validarString(user)&& validarString(pass)){

            PreferenciasFragment.setString(context, PreferenciasFragment.getKeyUserEd(),user);
            PreferenciasFragment.setString(context, PreferenciasFragment.getKeyPassEd(),user);

            //PreferenciasFragment.showUserSettings(context);


        }else{
            Toast.makeText(getApplicationContext(),"Invalid User", Toast.LENGTH_SHORT).show();
        }


        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();



        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the Main2Activity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String user = userText.getText().toString();
        String password = passwordText.getText().toString();

        if (user.isEmpty() || !Patterns.DOMAIN_NAME.matcher(user).matches()) {
            userText.setError("enter a name correct");
            valid = false;
        } else {
            userText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }


    /*
    * ENRIQUE*/
    public void comprobarPreferencias(){

        if (PreferenciasFragment.getBoolean(context, PreferenciasFragment.getKeyUserCheck(), false))
            userText.setText(PreferenciasFragment.getString(context,PreferenciasFragment.getKeyUserEd()));

        if (PreferenciasFragment.getBoolean(context,PreferenciasFragment.getKeyPassCheck(),false))
            passwordText.setText(PreferenciasFragment.getString(context,PreferenciasFragment.getKeyPassEd()));
    }


    Boolean validarString(String text){
        return text!=null && text.trim().length()>0;
    }
}
