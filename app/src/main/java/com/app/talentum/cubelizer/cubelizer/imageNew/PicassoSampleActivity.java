package com.app.talentum.cubelizer.cubelizer.imageNew;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.app.talentum.cubelizer.cubelizer.R;

import com.squareup.picasso.Callback;

import com.squareup.picasso.Picasso;

public class PicassoSampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_simple);

        PhotoView photoView = (PhotoView) findViewById(R.id.iv_photo);
        final PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);
        Picasso.with(this).load("https://s3.eu-central-1.amazonaws.com/cubelizer-empleodigital/ep_layout.jpg").into(photoView, new Callback() {
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
