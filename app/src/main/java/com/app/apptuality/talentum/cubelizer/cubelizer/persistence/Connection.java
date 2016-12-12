package com.app.apptuality.talentum.cubelizer.cubelizer.persistence;

/**
 * Created by Veronica on 07/12/2016.
 */
import android.util.Log;
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
import java.io.UnsupportedEncodingException;
import com.app.apptuality.talentum.cubelizer.cubelizer.persistence.JsonRespon;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Connection {
    private String usuario;
    private String password;

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    private String days;
    private String conexion_token;
    private String basic_url;
    private String floor_plan_view;
    private String background_view;
    private String activity_view;
    private String units_of_analysis;
    private String units_of_analysis_flow;
    JsonRespon jsonRespon;
    boolean respuesta;
    Reader reader;
    JSONObject jsonObject;


    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getConexion_token() {
        return conexion_token;
    }
    public void setConexion_token(String conexion_token) {
        this.conexion_token = conexion_token;
    }

    public String getBasic_url() {
        return basic_url;
    }
    public void  setBasic_url(String basic_url) {
        this.basic_url = basic_url;
    }

    public String getFloor_plan_view() {
        return floor_plan_view;
    }
    public void setFloor_plan_view(String floor_plan_view) {
        this.floor_plan_view = floor_plan_view;
    }

    public String getBackground_view() {
        return background_view;
    }
    public void setBackground_view(String background_view) {
        this.background_view = background_view;
    }

    public String getActivity_view() {
        return activity_view;
    }
    public void setActivity_view(String activity_view) {
        this.activity_view = activity_view;
    }

    public String getUnits_of_analysis() {
        return units_of_analysis;
    }
    public void setUnits_of_analysis(String units_of_analysis) {
        this.units_of_analysis = units_of_analysis;
    }

    public String getUnits_of_analysis_flow() {
        return getUnits_of_analysis();
    }
    public void setUnits_of_analysis_flow(String units_of_analysis_flow) {
        this.units_of_analysis_flow = units_of_analysis_flow;
    }


    public Connection(String usuario, String password) {
        this.setUsuario(usuario);
        this.setPassword(password);
        this.setBasic_url("http://52.59.217.121/v0/");
        this.setConexion_token(usuario + password);
    }
    public Connection(String usuario, String password, String days) {
        this.setUsuario(usuario);
        this.setPassword(password);
        this.setDays(days);
        this.setBasic_url("http://52.59.217.121/v0/");
        this.setConexion_token(usuario + password);
    }


    public JsonRespon doTheConnection() {
        String respuesta;
        Gson gson = new Gson();
        String url = this.getBasic_url() + "ping";
        reader = this.doTheTrick(1,url, "");
        jsonRespon = gson.fromJson(reader, JsonRespon.class);
        //respuesta = jsonRespon.toString();
        return jsonRespon;
    }


    public String getMap() {
        String result = "";
        JsonRespon jsonRespon;
        Gson gson = new Gson();
        String url = this.getBasic_url() + "get_map";
        reader = this.doTheTrick(1,url, "");
        jsonRespon = gson.fromJson(reader, JsonRespon.class);
        //Log.d("JsonRespon =>=> ", jsonRespon.toString());
        //Log.d("aaaaaaa => ", jsonRespon.getResult().toString());
        //Log.d("getFlootPlan =>=> ", jsonRespon.getResult().getMap());
        this.setFloor_plan_view(jsonRespon.getResult().getMap());
        //Log.d("JsonRespon =>=> ", jsonRespon.toString());
        //Log.d("Que es =>", this.getFloor_plan_view());
        result = jsonRespon.getResult().getMap();
        return result;
    }

    public String getDay(String day) {
        String result = "";
        JsonRespon jsonRespon;
        Gson gson = new Gson();
        String url = this.getBasic_url() + "get_day";
        //Log.d("dia", day);
        reader = this.doTheTrick(2,url, day);
        Log.d("Reader =>=> ", reader.toString());
        jsonRespon = gson.fromJson(reader, JsonRespon.class);
        if (jsonRespon.getMessage().isEmpty()) {
            Log.d("JsonRespon antes =>=> ", jsonRespon.toString());
            this.setBackground_view(jsonRespon.getResult().getBackground());
            Log.d("JsonRespon despues=> ", jsonRespon.toString());
            result = jsonRespon.getResult().getBackground();
        } else {
            result = "";
        }
        return result;
    }
    public String getDay2(String day) {
        String result = "";
        JsonRespon jsonRespon;
        Gson gson = new Gson();
        String url = this.getBasic_url() + "get_day";
        reader = this.doTheTrick(2,url, day);
        jsonRespon = gson.fromJson(reader, JsonRespon.class);
        if (jsonRespon.getMessage().isEmpty()) {
            Log.d("JsonRespon antes =>=> ", jsonRespon.toString());
            this.setActivity_view(jsonRespon.getResult().getActivityMap());
            Log.d("JsonRespon despues=> ", jsonRespon.toString());
            result = jsonRespon.getResult().getActivityMap();
        } else {
            result = "";
        }
        return result;
    }

    public String getUas() {
        String result = "";
        String url = this.getBasic_url() + "get_UAs";
        result = this.doTheTrick2(1,url, "");
        return result;
    }


    public String getDayUasData(String day) {
        String result = "";
        JsonRespon jsonRespon;
        Gson gson = new Gson();
        String url = this.getBasic_url() + "get_day_UAs_data";
        reader = this.doTheTrick(2, url, day);
        Log.d("Reader antes =>=> ", reader.toString());
        jsonRespon = gson.fromJson(reader, JsonRespon.class);
        Log.d("JsonRespon antes =>=> ", jsonRespon.toString());

        if (jsonRespon.getMessage() == "") {
            Log.d("JsonRespon antes =>=> ", jsonRespon.toString());
            this.setUnits_of_analysis_flow(jsonRespon.getResult().getuAsFlow());
            Log.d("JsonRespon despues=> ", jsonRespon.toString());
            result = jsonRespon.getResult().getuAsFlow();
        } else {
            result = "";
        }
        //result = jsonRespon.getResult().getuAsFlow();
        return result;
    }

    private Reader doTheTrick(int opc, String url, String day) {
        /*Aqui llamamos a la API */
        String result = "";
        HttpClient client;
        HttpGetWithEntity httpGetWithEntity;
        //JsonRespon jsonRespon;

        Reader reader = null;
        try {
            client = new DefaultHttpClient();
            httpGetWithEntity = new HttpGetWithEntity(url);
            switch (opc){
                case 1:
                    jsonObject = new JSONObject();
                    jsonObject.put("user", this.getUsuario());
                    jsonObject.put("password", this.getPassword());
                    Log.d("JSON", jsonObject.toString());
                    Log.d("URL", url);
                    break;
                case 2:
                    jsonObject = new JSONObject();
                    jsonObject.put("user", this.getUsuario());
                    jsonObject.put("password", this.getPassword());
                    jsonObject.put("day", day);
                    Log.d("JSON else", jsonObject.toString());
                    Log.d("URL else", url);
                    break;
            }
            StringEntity stringEntity = new StringEntity(jsonObject.toString(), "UTF8");
            stringEntity.setContentType("application/json");
            httpGetWithEntity.setEntity(stringEntity);
            HttpResponse response = client.execute(httpGetWithEntity);

            if (response != null) {
                InputStream ips = response.getEntity().getContent();
                reader = new InputStreamReader(ips);
                Log.d("Reader", reader.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reader;
    }

    private String doTheTrick2(int opc, String url, String day){
        BufferedReader reader=null;
        String textoRespuesta = "";
        HttpClient client;
        HttpGetWithEntity httpGetWithEntity;
        try {
            client = new DefaultHttpClient();
            httpGetWithEntity = new HttpGetWithEntity(url);
            switch (opc){
                case 1:
                    jsonObject = new JSONObject();
                    jsonObject.put("user", this.getUsuario());
                    jsonObject.put("password", this.getPassword());
                    Log.d("JSON", jsonObject.toString());
                    Log.d("URL", url);
                    break;
                case 2:
                    jsonObject = new JSONObject();
                    jsonObject.put("user", this.getUsuario());
                    jsonObject.put("password", this.getPassword());
                    jsonObject.put("day", day);
                    Log.d("JSON else", jsonObject.toString());
                    Log.d("URL else", url);
                    break;
            }
            StringEntity stringEntity = new StringEntity(jsonObject.toString(), "UTF8");
            stringEntity.setContentType("application/json");
            httpGetWithEntity.setEntity(stringEntity);
            HttpResponse response = client.execute(httpGetWithEntity);
            Log.d("MainActivity",String.valueOf(response.getStatusLine().getStatusCode()));

            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer();
            String line = null;
            // Leemos la respuesta del servidor
            while((line = reader.readLine()) != null){
                // Añadimos la respuesta a un StringBuffer
                sb.append(line + " ");
            }
            textoRespuesta = sb.toString();
        } catch(Exception ex){
            //error = ex.getMessage();
        } finally {
            try{
                //reader.close();
            } catch(Exception ex) {

            }
        }
        return textoRespuesta;
    }
}


