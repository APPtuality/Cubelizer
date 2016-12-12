package com.app.apptuality.talentum.cubelizer.cubelizer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.apptuality.talentum.cubelizer.cubelizer.persistence.Connection;
import com.app.apptuality.talentum.cubelizer.cubelizer.persistence.JsonRespon;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class LoginActivity extends AppCompatActivity implements Serializable {

    /*DECLARACIÓN DE VARIABLES*/
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    private EditText userText;
    private EditText passwordText;
    private Button loginButton;
    private String actualKey;
    private String pass;
    private String user;
    private Context context;
    private JsonRespon respuesta;
    Connection connection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        //Escondemos la barra superior de navegación para mostrar la pantalla de Login
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        //getSupportActionBar().hide();

        //Llamamos a la activity que tiene estructurado el Login
        setContentView(R.layout.activity_login);
        //session = new UserSessionManager(getApplicationContext());

        //Declaración de los elementos del Activity Login
        userText = (EditText) findViewById(R.id.input_user);
        passwordText = (EditText) findViewById(R.id.input_password);
        loginButton = (Button) findViewById(R.id.btn_login);


        //Le damos funcionalidad al Botón de Login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    new ConexionRest().execute();
                } else {
                    Log.v("LoginActivity", "No hay conexion disponible");
                }
            }
        });
    }

    /**HILO DE CONEXION*/
    private class ConexionRest extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        @Override
        protected void onPreExecute() {
            user = userText.getText().toString();
            pass = passwordText.getText().toString();
            connection = new Connection(user, pass);
            connection.setUsuario(user);
            connection.setPassword(pass);
            progressDialog.setTitle("Please wait...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            respuesta = connection.doTheConnection();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            if (respuesta.getStatus().equals("ok")) {
                Log.i("Hola estoy aqui","Login correcto");
                saveUser(user, pass);
            }else{
                Log.i("Hola estoy aqui","Login incorrecto");
            }
            }
        }

        private void saveUser(String us, String pass) {
            //session.createUserLoginSession(us, pass);
            String user = userText.getText().toString();
            String pas = passwordText.getText().toString();

        /*Usuario newUs = new Usuario();
        newUs.setUsuario(us);
        newUs.setPassword(pass);
        Log.i("MainActivity",newUs.toString());*/
            Log.i("Login", connection.toString());
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("password",pas);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        /*
        private String prettyfyJSON(String json){
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(json);
            return gson.toJson(element);
        }
        */
    /*
    Función que le da funcionalidad al botón de Login
     */
        public void login() {
            Log.d(TAG, "Login");

/*
            if (!validate()) {
                onLoginFailed();
                return;
            }
*/
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



        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == REQUEST_SIGNUP) {
                if (resultCode == RESULT_OK) {

                    // TODO: Implement successful signup logic here
                    // By default we just finish the Activity and log them in automatically
                    finish();
                }
            }
        }


        public void onBackPressed() {
            // disable going back to the Main2Activity
            moveTaskToBack(true);
        }

        public void onLoginSuccess() {
            loginButton.setEnabled(true);
            finish();
        }
/*
        public void onLoginFailed() {
            String loginFail = this.getString(R.string.login_fail);

            Toast.makeText(getBaseContext(), loginFail, Toast.LENGTH_LONG).show();

            loginButton.setEnabled(true);
        }
*/
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


        Boolean validarString(String text) {
            return text != null && text.trim().length() > 0;
        }
    }





