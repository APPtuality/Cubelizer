package com.app.talentum.cubelizer.cubelizer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * Created by Veronica on 27/11/2016.
 */

public class PreferenciasFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static String getKeyUserEd() {
        return KEY_USER_ED;
    }

    public static String getKeyPassEd() {
        return KEY_PASS_ED;
    }

    public static String getKeyUserCheck() {
        return KEY_USER_CHECK;
    }

    public static String getKeyPassCheck() {
        return KEY_PASS_CHECK;
    }

    public static String getKeyLanguage() {
        return KEY_LANGUAGE;
    }

    private static final String KEY_USER_ED = "user_text";
    private static final String KEY_PASS_ED = "pass_text";
    private static final String KEY_USER_CHECK = "save_user";
    private static final String KEY_PASS_CHECK = "save_pass";
    private static final String KEY_LANGUAGE = "language_list";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    /**
     * MODIFICAR LAS PREFERENCIAS
     */


    public static String getString(Context context, final String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, "");
    }

    public static void setString(Context context, final String key, final String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();

    }


    public static boolean getBoolean(Context context, final String key, final boolean defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(key, defaultValue);

    }


    public static void setBoolean(Context context, final String key, final boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        switch (key){
            case KEY_USER_ED:
                String valor_user_ed = sharedPreferences.getString(KEY_USER_ED,"NULL");
                Toast.makeText(getActivity().getApplicationContext(),"New user: "+valor_user_ed+" saved.", Toast.LENGTH_LONG).show();
                break;
            case KEY_PASS_ED:
                String valor_pass_ed = sharedPreferences.getString(KEY_PASS_ED,"NULL");
                Toast.makeText(getActivity().getApplicationContext(),"New Password saved",Toast.LENGTH_LONG).show();
                break;
            case KEY_USER_CHECK:
                String valor_user_check =""+ sharedPreferences.getBoolean(KEY_USER_CHECK,false);
                Toast.makeText(getActivity().getApplicationContext(),"New preferences changes",Toast.LENGTH_LONG).show();
                break;
            case KEY_PASS_CHECK:
                String valor_pass_check =""+ sharedPreferences.getBoolean(KEY_PASS_CHECK,false);
                Toast.makeText(getActivity().getApplicationContext(),"New preferences changes",Toast.LENGTH_LONG).show();
                break;
            case KEY_LANGUAGE:
                String valor_language = sharedPreferences.getString(KEY_LANGUAGE, "NULL");
                Toast.makeText(getActivity().getApplicationContext(),"New preferences changes",Toast.LENGTH_LONG).show();
                break;
        }
    }
    @Override
    public void onPause() {
        super.onPause();

        /**
         * ENCENDER EL ESCUCHADOR DE EVENTOS POR SI ALGUNA CLAVE DE PREFERENCIAS CAMBIA
         */
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        /**
         * APAGAR EL ESCUCHADOR DE EVENTOS DE PREFERENCIAS
         */
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);

    }
    }

