package com.app.talentum.cubelizer.cubelizer.map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.app.talentum.cubelizer.cubelizer.LoginActivity;
import com.app.talentum.cubelizer.cubelizer.MainActivity;
import com.app.talentum.cubelizer.cubelizer.R;
import com.app.talentum.cubelizer.cubelizer.entidades.Usuario;
import com.app.talentum.cubelizer.cubelizer.imageNew.PicassoSampleActivity;
import com.app.talentum.cubelizer.cubelizer.persistencia.HttpGetWithEntity;
import com.app.talentum.cubelizer.cubelizer.persistencia.JsonRespon;
import com.app.talentum.cubelizer.cubelizer.persistencia.UserSessionManager;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;


public class GetMapActivity extends AppCompatActivity implements Serializable{

    private static final String TAG = "GetMapActivity";
    private static final int REQUEST_SIGNUP = 0;
    public static final String SERVER_URL_MAP = "http://52.59.217.121/v0/get_map";

    HttpClient client;
    HttpGetWithEntity httpGetWithEntity;
    JsonRespon jsonRespon;
    String mapaPlano;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);
        //Escondemos la barra superior de navegación para mostrar la pantalla de Login
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        isNetworkAvailable();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            new ConexionGetMapRest().execute(SERVER_URL_MAP);
            Log.v("LoginActivity", SERVER_URL_MAP);
        } else {
            Log.v("LoginActivity", "No hay conexion disponible");
        }
        return true;
    }
    /*
    * Hilo de conexión
    **/
    private class ConexionGetMapRest extends AsyncTask<String, Void, Void> {
        final HttpClient httpClient = new DefaultHttpClient();
        ProgressDialog progressDialog = new ProgressDialog(GetMapActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Please wait...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            Reader reader = null;
            Gson gson = new Gson();
            try {
                client = new DefaultHttpClient();
                httpGetWithEntity = new HttpGetWithEntity(SERVER_URL_MAP);

                UserSessionManager session = new UserSessionManager(getApplicationContext());
                HashMap<String,String>user = session.getUserDetails();
                String name =  user.get(UserSessionManager.KEY_USER);
                String pass =  user.get(UserSessionManager.KEY_PASS);
                System.out.println("Estoy aqui"+ name + pass);
                /*
                Creamos el objeto Json
                 */
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("user", name);
                jsonObject.put("password", pass);
                Log.d(TAG, jsonObject.toString());

                StringEntity stringEntity = new StringEntity(jsonObject.toString(), "UTF8");
                stringEntity.setContentType("application/json");
                httpGetWithEntity.setEntity(stringEntity);
                HttpResponse response = client.execute(httpGetWithEntity);
                Log.d(TAG, String.valueOf(response.getStatusLine().getStatusCode()));

                if (response != null) {
                    InputStream ips = response.getEntity().getContent();
                    reader = new InputStreamReader(ips);
                    jsonRespon = gson.fromJson(reader, JsonRespon.class);
                    mapaPlano = jsonRespon.getResult().getMap();
                    Log.e("RESPUESTA", jsonRespon.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            if (jsonRespon.getStatus().equals("ok")) {
                Log.i("GETMAP", "Login Correcto");
                solicitarGetMap();
            } else {
                String mens = jsonRespon.getMessage();
                Log.i("GetMapActivity", mens);
            }
            Log.i("MainActivity", jsonRespon.toString());
        }

        public void solicitarGetMap() {
            String map = mapaPlano;
            Log.d("MAPA_STRING", map);
            Intent i = new Intent (getApplicationContext(), GetDayActivity.class);
            i.putExtra("map", map);
            startActivity(i);
        }
    }
}


