package com.app.talentum.cubelizer.cubelizer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.support.design.widget.CoordinatorLayout;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.talentum.cubelizer.cubelizer.entidades.Usuario;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView ivImagen;
    private TextView tvDimensiones;
    private Bitmap bMapPlano,bMapImagen,bMapActividad;
    private Display display;
    private Point size;
    float scale =1f;
    Usuario usuario;
    //ScaleGestureDetector SGD;
    PinchZoomImageView mPinchZoomImageView;


    FloatingActionButton fab;
    FloatingActionButton fab1;
    FloatingActionButton fab2;
    FloatingActionButton fab3;
    CoordinatorLayout rootLayout;

    //Save the FAB's active status
    //false -> fab = close
    //true -> fab = open
    private boolean FAB_Status = false;

    //Animations
    Animation show_fab_1;
    Animation hide_fab_1;
    Animation show_fab_2;
    Animation hide_fab_2;
    Animation show_fab_3;
    Animation hide_fab_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usuario = new Usuario();
        usuario = (Usuario)getIntent().getExtras().getSerializable("user");
        usuario = (Usuario)getIntent().getExtras().getSerializable("password");

        tvDimensiones = (TextView) findViewById(R.id.tvDimensiones);
        ivImagen = (ImageView) findViewById(R.id.ivImagen);
        mPinchZoomImageView = (PinchZoomImageView) findViewById(R.id.pinchZoomImageView);

        //SGD = new ScaleGestureDetector(this, new ScaleListener());

        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        int dHeight = size.y;
        int dWidth = size.x;
        final int maxSize = 2048;
        bMapPlano = BitmapFactory.decodeResource(getResources(), R.drawable.plano_prueba2);
        int iWidth = bMapPlano.getWidth();
        int iHeight = bMapPlano.getHeight();
        float factor = (float) iWidth/(float) iHeight;

        ivImagen.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Toast.makeText(getApplicationContext(), "ImageView long pressed!", Toast.LENGTH_SHORT).show();
                // zoomImageFromThumb();
                pinchZoomPan();
                return true;
            }
        });

        if (orientacionDispositivoVertical()){
            if ((dHeight * factor) > maxSize) {
                dHeight = (int) (maxSize / factor);
            }
            bMapPlano = Bitmap.createScaledBitmap(bMapPlano, (int)(dHeight*factor), dHeight, true);
            tvDimensiones.setText(Integer.toString(bMapPlano.getWidth()) + " x " +
                    Integer.toString(bMapPlano.getHeight()) + " x " +
                    Integer.toString(size.x) + " x " +
                    Integer.toString(size.y) + " x " +
                    Float.toString(factor) + " x vertical");
        }else{
            if ((dWidth / factor) > maxSize) {
                dWidth = (int) (maxSize * factor);
            }
            bMapPlano = Bitmap.createScaledBitmap(bMapPlano, dWidth, (int) (dWidth / factor), true);
            tvDimensiones.setText(Integer.toString(bMapPlano.getWidth()) + " x " +
                    Integer.toString(bMapPlano.getHeight()) + " x " +
                    Integer.toString(size.y) + " x " +
                    Integer.toString(size.x) + " x " +
                    Float.toString(factor) + " x horizontal");
        }

        bMapImagen = BitmapFactory.decodeResource(getResources(), R.drawable.imagen_prueba);
        bMapImagen = Bitmap.createScaledBitmap(bMapImagen, bMapPlano.getWidth(), bMapPlano.getHeight(), true);
        bMapActividad = BitmapFactory.decodeResource(getResources(), R.drawable.actividad_prueba);
        bMapActividad = Bitmap.createScaledBitmap(bMapActividad, bMapPlano.getWidth(), bMapPlano.getHeight(), true);

        ivImagen.setImageBitmap(bMapPlano);
        mPinchZoomImageView.setImageBitmap(bMapPlano);










        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        rootLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        //Floating Action Buttons
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab_1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab_2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab_3);

        //Animations
        show_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_show);
        hide_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_hide);
        show_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_show);
        hide_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_hide);
        show_fab_3 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab3_show);
        hide_fab_3 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab3_hide);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (FAB_Status == false) {
                    //Display FAB menu
                    expandFAB();
                    FAB_Status = true;
                } else {
                    //Close FAB menu
                    hideFAB();
                    FAB_Status = false;
                }
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "Boton flotante 1 - Mostrar Plano", Toast.LENGTH_SHORT).show();
                tvDimensiones.setText(Integer.toString(bMapPlano.getWidth()) + " x " +
                        Integer.toString(bMapPlano.getHeight()));
                ivImagen.setImageBitmap(bMapPlano);
                mPinchZoomImageView.setImageBitmap(bMapPlano);

            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "Boton flotante 2 - Plano+Imagen", Toast.LENGTH_SHORT).show();
                //mostramos dimensiones despues de escalarlo
                tvDimensiones.setText(Integer.toString(bMapImagen.getWidth()) + " x " +
                        Integer.toString(bMapImagen.getHeight()));
                //creamos un Bitmap de apoyo que será el resultado de la unión de los otros
                Bitmap bMap = Bitmap.createBitmap(bMapPlano.getWidth(), bMapPlano.getHeight(), bMapPlano.getConfig());
                //creamos un objeto Canvas con el nuevo BitMap para suporponer los Bitmaps
                Canvas canvas = new Canvas(bMap);
                //creamos un objeto Paint para modificar la opacidad del Bitmap sobrepuesto
                Paint p = new Paint();
                //cambiamos el valor de alpha al que queramos (0 transparente y 255 opaco)
                //10% de transparencia = 255-(10*2.55)=229.5=229
                p.setAlpha(229);
                //dibujamos sobre nuestro bitMap creado cada bitmap en orden (de debajo a encima)
                canvas.drawBitmap(bMapPlano, new Matrix(), null);
                canvas.drawBitmap(bMapImagen, new Matrix(), p);
                ivImagen.setImageBitmap(bMap);
                mPinchZoomImageView.setImageBitmap(bMap);
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "Boton flotante 3 - Plano+Actividad", Toast.LENGTH_SHORT).show();
                //mostramos dimensiones despues de escalarlo
                tvDimensiones.setText(Integer.toString(bMapActividad.getWidth()) + " x " +
                        Integer.toString(bMapActividad.getHeight()));
                //creamos un Bitmap de apoyo que será el resultado de la unión de los otros
                Bitmap bMap = Bitmap.createBitmap(bMapPlano.getWidth(), bMapPlano.getHeight(), bMapPlano.getConfig());
                //creamos un objeto Canvas con el nuevo BitMap para suporponer los Bitmaps
                Canvas canvas = new Canvas(bMap);
                //creamos un objeto Paint para modificar la opacidad del Bitmap sobrepuest
                Paint p = new Paint();
                //cambiamos el valor de alpha al que queramos (0 transparente y 255 opaco)
                //30% de transparencia = 255-(30*2.55)=178,5=178
                p.setAlpha(178);
                //dibujamos sobre nuestro bitMap creado cada bitmap en orden (de debajo a encima)
                canvas.drawBitmap(bMapPlano, new Matrix(), null);
                canvas.drawBitmap(bMapActividad, new Matrix(), p);
                ivImagen.setImageBitmap(bMap);
                mPinchZoomImageView.setImageBitmap(bMap);
            }
        });


        //Initialize an empty list of 50 elements
        List list = new ArrayList();
        for (int i = 0; i < 50; i++) {
            list.add(new Object());
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        Recycler_View_Adapter adapter = new Recycler_View_Adapter(list, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (FAB_Status) {
                    hideFAB();
                    FAB_Status = false;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /**
         * Boton de settings del main
         */
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, Preferencias.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void expandFAB() {

        //Floating Action Button 1
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab1.getLayoutParams();
        layoutParams.rightMargin += (int) (fab1.getWidth() * 1.7);
        layoutParams.bottomMargin += (int) (fab1.getHeight() * 0.25);
        fab1.setLayoutParams(layoutParams);
        fab1.startAnimation(show_fab_1);
        fab1.setClickable(true);

        //Floating Action Button 2
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fab2.getLayoutParams();
        layoutParams2.rightMargin += (int) (fab2.getWidth() * 1.5);
        layoutParams2.bottomMargin += (int) (fab2.getHeight() * 1.5);
        fab2.setLayoutParams(layoutParams2);
        fab2.startAnimation(show_fab_2);
        fab2.setClickable(true);

        //Floating Action Button 3
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) fab3.getLayoutParams();
        layoutParams3.rightMargin += (int) (fab3.getWidth() * 0.25);
        layoutParams3.bottomMargin += (int) (fab3.getHeight() * 1.7);
        fab3.setLayoutParams(layoutParams3);
        fab3.startAnimation(show_fab_3);
        fab3.setClickable(true);
    }


    private void hideFAB() {

        //Floating Action Button 1
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab1.getLayoutParams();
        layoutParams.rightMargin -= (int) (fab1.getWidth() * 1.7);
        layoutParams.bottomMargin -= (int) (fab1.getHeight() * 0.25);
        fab1.setLayoutParams(layoutParams);
        fab1.startAnimation(hide_fab_1);
        fab1.setClickable(false);

        //Floating Action Button 2
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fab2.getLayoutParams();
        layoutParams2.rightMargin -= (int) (fab2.getWidth() * 1.5);
        layoutParams2.bottomMargin -= (int) (fab2.getHeight() * 1.5);
        fab2.setLayoutParams(layoutParams2);
        fab2.startAnimation(hide_fab_2);
        fab2.setClickable(false);

        //Floating Action Button 3
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) fab3.getLayoutParams();
        layoutParams3.rightMargin -= (int) (fab3.getWidth() * 0.25);
        layoutParams3.bottomMargin -= (int) (fab3.getHeight() * 1.7);
        fab3.setLayoutParams(layoutParams3);
        fab3.startAnimation(hide_fab_3);
        fab3.setClickable(false);
    }



/*
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected (MenuItem item){
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.nav_camera) {
                // Handle the camera action
            } else if (id == R.id.nav_gallery) {

            } else if (id == R.id.nav_slideshow) {

            } else if (id == R.id.nav_manage) {

            } else if (id == R.id.nav_share) {

            } else if (id == R.id.nav_send) {

            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
    }*/

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


/*
    public void mostrarPlano(View v){
        tvDimensiones.setText(Integer.toString(bMapPlano.getWidth()) + " x " +
                Integer.toString(bMapPlano.getHeight()));
        ivImagen.setImageBitmap(bMapPlano);
    }
*/

    /*
    public void mostrarImagen(View v) {
        //mostramos dimensiones despues de escalarlo
        tvDimensiones.setText(Integer.toString(bMapImagen.getWidth()) + " x " +
                Integer.toString(bMapImagen.getHeight()));
        //creamos un Bitmap de apoyo que será el resultado de la unión de los otros
        Bitmap bMap = Bitmap.createBitmap(bMapPlano.getWidth(), bMapPlano.getHeight(), bMapPlano.getConfig());
        //creamos un objeto Canvas con el nuevo BitMap para suporponer los Bitmaps
        Canvas canvas = new Canvas(bMap);
        //creamos un objeto Paint para modificar la opacidad del Bitmap sobrepuesto
        Paint p = new Paint();
        //cambiamos el valor de alpha al que queramos (0 transparente y 255 opaco)
        //10% de transparencia = 255-(10*2.55)=229.5=229
        p.setAlpha(229);
        //dibujamos sobre nuestro bitMap creado cada bitmap en orden (de debajo a encima)
        canvas.drawBitmap(bMapPlano, new Matrix(), null);
        canvas.drawBitmap(bMapImagen, new Matrix(), p);
        ivImagen.setImageBitmap(bMap);
    }
*/

    /*
    public void mostrarActividad(View v){
        //mostramos dimensiones despues de escalarlo
        tvDimensiones.setText(Integer.toString(bMapActividad.getWidth()) + " x " +
                Integer.toString(bMapActividad.getHeight()));
        //creamos un Bitmap de apoyo que será el resultado de la unión de los otros
        Bitmap bMap = Bitmap.createBitmap(bMapPlano.getWidth(), bMapPlano.getHeight(), bMapPlano.getConfig());
        //creamos un objeto Canvas con el nuevo BitMap para suporponer los Bitmaps
        Canvas canvas = new Canvas(bMap);
        //creamos un objeto Paint para modificar la opacidad del Bitmap sobrepuest
        Paint p = new Paint();
        //cambiamos el valor de alpha al que queramos (0 transparente y 255 opaco)
        //30% de transparencia = 255-(30*2.55)=178,5=178
        p.setAlpha(178);
        //dibujamos sobre nuestro bitMap creado cada bitmap en orden (de debajo a encima)
        canvas.drawBitmap(bMapPlano, new Matrix(), null);
        canvas.drawBitmap(bMapActividad, new Matrix(), p);
        ivImagen.setImageBitmap(bMap);
    }
    */

    public void mostrarTodo(View v){
        //mostramos dimensiones despues de escalarlo
        tvDimensiones.setText(Integer.toString(bMapActividad.getWidth()) + " x " +
                Integer.toString(bMapActividad.getHeight()));
        //creamos un Bitmap de apoyo que será el resultado de la unión de los otros
        Bitmap bMap = Bitmap.createBitmap(bMapPlano.getWidth(), bMapPlano.getHeight(), bMapPlano.getConfig());
        //creamos un objeto Canvas con el nuevo BitMap para suporponer los Bitmaps
        Canvas canvas = new Canvas(bMap);
        //creamos un objeto Paint para modificar la opacidad del Bitmap sobrepuest
        Paint p = new Paint();
        //cambiamos el valor de alpha al que queramos (0 transparente y 255 opaco)
        //30% de transparencia = 255-(30*2.55)=178,5=178
        p.setAlpha(178);
        //dibujamos sobre nuestro bitMap creado cada bitmap en orden (de debajo a encima)
        canvas.drawBitmap(bMapPlano, new Matrix(), null);
        canvas.drawBitmap(bMapImagen, new Matrix(), p);
        canvas.drawBitmap(bMapActividad, new Matrix(), p);
        ivImagen.setImageBitmap(bMap);
        mPinchZoomImageView.setImageBitmap(bMap);
    }

    public boolean orientacionDispositivoVertical() {

        int rotacion = display.getRotation();

        if (rotacion== Surface.ROTATION_0 || rotacion == Surface.ROTATION_180) {

            return true;
        }
        else {
            return false;
        }
    }
/*
    public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        public boolean onScale(ScaleGestureDetector detector) {
            scale = scale * detector.getScaleFactor();
            scale = Math.max(0.1f,Math.min(scale,5f));
            Matrix matrix = new Matrix();
            matrix.setScale(scale,scale);
            ivImagen.setImageMatrix(matrix);
            return true;

        }

    }
    */
/*
    public boolean onTouchEvent(MotionEvent event){
        SGD.onTouchEvent(event);
        return true;
    }
*/

    /**
     * Alvaro
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        View decorView = getWindow().getDecorView();
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            );
        }
    }

    private void pinchZoomPan() {
        //mPinchZoomImageView.setImageBitmap(ultimoBMap);
        ivImagen.setAlpha(0.f);
        mPinchZoomImageView.setVisibility(View.VISIBLE);
    }

}
