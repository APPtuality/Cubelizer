package com.app.talentum.cubelizer.cubelizer.map;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import com.app.talentum.cubelizer.cubelizer.entidades.Usuario;
import com.app.talentum.cubelizer.cubelizer.persistencia.HttpGetWithEntity;
import com.app.talentum.cubelizer.cubelizer.persistencia.JsonUas;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;


public class GetUAsActivity extends AppCompatActivity implements Serializable {

    private static final String TAG = "GetUAsActivity";
    private static final int REQUEST_SIGNUP = 0;
    public static final String SERVER_URL_GETUAS = "http://52.59.217.121/v0/get_UAs";

    Button loginButton;
    ;
    String pass;
    String user;
    Context context;
    HttpClient client;
    Usuario usuario;
    int marcador;
    HttpGetWithEntity httpGetWithEntity;
    String mapaPlano;
    private String textoRespuesta;

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
            new ConexionGetMapRest().execute(SERVER_URL_GETUAS);
            Log.v(TAG, SERVER_URL_GETUAS);
        } else {
            Log.v(TAG, "No hay conexion disponible");
        }
        return true;
    }

    /*
    * Hilo de conexión
    **/
    private class ConexionGetMapRest extends AsyncTask<String, Void, Void> {
        final HttpClient httpClient = new DefaultHttpClient();
        String respuesta;
        String error;
        ProgressDialog progressDialog = new ProgressDialog(GetUAsActivity.this);
        String data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // user = userText.getText().toString();
            // pass = passwordText.getText().toString();

            progressDialog.setTitle("Please wait...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            BufferedReader reader = null;

            try {

                HttpClient client = new DefaultHttpClient();

                String urlMap = SERVER_URL_GETUAS;
                HttpGetWithEntity myGet = new HttpGetWithEntity(urlMap);
                //Create JSONObject here
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("user", "edigital");
                jsonParam.put("password", "edigital2016");
                //jsonParam.put("day", "");
                Log.d("MainActivity", jsonParam.toString());
                //  Log.d("fe",date);


                StringEntity stringEntity = new StringEntity(jsonParam.toString(), "UTF8");
                stringEntity.setContentType("application/json");
                myGet.setEntity(stringEntity);
                HttpResponse response = client.execute(myGet);
                Log.d("MainActivity", String.valueOf(response.getStatusLine().getStatusCode()));

                reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));


                StringBuffer sb = new StringBuffer();
                String line = null;

                // Leemos la respuesta del servidor
                while ((line = reader.readLine()) != null) {
                    // Añadimos la respuesta a un StringBuffer
                    sb.append(line + " ");
                }

                textoRespuesta = sb.toString();
                Log.e("UAs",textoRespuesta);
            } catch (Exception ex) {
                error = ex.getMessage();
            } finally {
                try {
                    reader.close();

                } catch (Exception ex) {
                }
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            if (textoRespuesta != null)
                solicitarGetUAs();
            Log.i("MainActivity", textoRespuesta);
        }
    }

    public void solicitarGetUAs() {
        String respUAs = textoRespuesta;
        JsonUas jsonUAs = new JsonUas();
        jsonUAs.devolucionArray(respUAs);
        Log.d("UAS_STRING", respUAs);

        //Intent intent = new Intent(getApplicationContext(), PinchZoomImageView.class);
        //intent.putExtra("map", map);
        //startActivity(intent);
    }
}