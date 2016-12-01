package com.app.talentum.cubelizer.cubelizer;

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

import com.app.talentum.cubelizer.cubelizer.entidades.Usuario;
import com.app.talentum.cubelizer.cubelizer.imageNew.PicassoSampleActivity;
import com.app.talentum.cubelizer.cubelizer.map.GetMapActivity;
import com.app.talentum.cubelizer.cubelizer.persistencia.HttpGetWithEntity;
import com.app.talentum.cubelizer.cubelizer.persistencia.JsonRespon;
import com.app.talentum.cubelizer.cubelizer.persistencia.UserSessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;


public class LoginActivity extends AppCompatActivity implements Serializable{

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    public static final String SERVER_URL = "http://52.59.217.121/v0/ping";
    EditText userText;
    EditText passwordText;
    Button loginButton;;
    String pass;
    String user;
    Context context;
    HttpClient client;
    Usuario usuario;
    int marcador;
    HttpGetWithEntity httpGetWithEntity;
    JsonRespon jsonRespon;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        //Escondemos la barra superior de navegación para mostrar la pantalla de Login
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();

        //Llamamos a la activity que tiene estructurado el Login
        setContentView(R.layout.activity_login);
        session = new UserSessionManager(getApplicationContext());

        //Declaración de los elementos del Activity Login
        userText = (EditText)findViewById(R.id.input_user);
        passwordText = (EditText)findViewById(R.id.input_password);
        loginButton = (Button)findViewById(R.id.btn_login);
        Toast.makeText(getApplicationContext(),"USer Login status" + session.isUserLoggedIn(), Toast.LENGTH_LONG).show();

        //Le damos funcionalidad al Botón de Login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if(networkInfo != null && networkInfo.isConnected()){
                    new ConexionRest().execute(SERVER_URL);
                    Log.v("LoginActivity",SERVER_URL);
                }else{
                    Log.v("LoginActivity","No hay conexion disponible");
                }
            }
        });
    }
    /*
    * Hilo de conexión
    **/
    private class ConexionRest extends AsyncTask<String, Void, Void>{
        final HttpClient httpClient = new DefaultHttpClient();
        String respuesta;
        String error;
        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        String data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            user = userText.getText().toString();
            pass = passwordText.getText().toString();
            progressDialog.setTitle("Please wait...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(String... params) {
            Reader reader = null;
            Gson gson = new Gson();
            try {
                client = new DefaultHttpClient();
                httpGetWithEntity = new HttpGetWithEntity(SERVER_URL);

                //Creamos el objeto Json
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("user", user);
                jsonObject.put("password", pass);
                Log.d("LoginActivity", jsonObject.toString());

                StringEntity stringEntity = new StringEntity(jsonObject.toString(), "UTF8");
                stringEntity.setContentType("application/json");
                httpGetWithEntity.setEntity(stringEntity);
                HttpResponse response = client.execute(httpGetWithEntity);
                Log.d("LoginActivity",String.valueOf(response.getStatusLine().getStatusCode()));

                if(response!=null){
                    InputStream ips = response.getEntity().getContent();
                    reader = new InputStreamReader(ips);
                    jsonRespon = gson.fromJson(reader, JsonRespon.class);
                    Log.e("RESPUESTA", jsonRespon.toString());
                }

            }catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();

            if(jsonRespon.getStatus().equals("ok")){
                Log.i("LoginActivity","Login Correcto");
                saveUser(user, pass);

            } else{
                String mens = jsonRespon.getMessage();
                Toast.makeText(getApplicationContext(),mens,Toast.LENGTH_SHORT).show();
                Log.i("LoginActivity","Error en el login");
            }
            Log.i("MainActivity",jsonRespon.toString());
        }
    }
    private void saveUser(String us, String pass){
        session.createUserLoginSession(us);
        Usuario newUs = new Usuario();
        newUs.setUsuario(us);
        newUs.setPassword(pass);
        Log.i("MainActivity",newUs.toString());

        Intent intent = new Intent(getApplicationContext(), GetMapActivity.class);
        intent.putExtra("user", us);
        intent.putExtra("password",pass);
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
