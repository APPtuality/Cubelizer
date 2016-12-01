package com.app.talentum.cubelizer.cubelizer.imageNew;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;

import com.app.talentum.cubelizer.cubelizer.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class PicassoSampleActivity extends AppCompatActivity {
    String sMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            sMap =(String)extras.get("map");
            Log.e("hola------------->",sMap);
        }

        //Escondemos la barra superior de navegación para mostrar la pantalla de Login
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);


        PhotoView photoView = (PhotoView) findViewById(R.id.iv_photo);
        final PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);
        Picasso.with(this).load(sMap).into(photoView, new Callback() {
            @Override
            public void onSuccess() {
                attacher.update();
            }
            @Override
            public void onError() {
            }

        });
    }
}
