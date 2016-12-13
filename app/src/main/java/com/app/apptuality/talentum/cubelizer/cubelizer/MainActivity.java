package com.app.apptuality.talentum.cubelizer.cubelizer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.CoordinatorLayout;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.apptuality.talentum.cubelizer.cubelizer.persistence.Affluence;
import com.app.apptuality.talentum.cubelizer.cubelizer.persistence.Calendar;
import com.app.apptuality.talentum.cubelizer.cubelizer.persistence.Connection;
import com.app.apptuality.talentum.cubelizer.cubelizer.persistence.JsonUAs;
import com.app.apptuality.talentum.cubelizer.cubelizer.persistence.Pintura;
import com.app.apptuality.talentum.cubelizer.cubelizer.persistence.Polygon;
import com.app.apptuality.talentum.cubelizer.cubelizer.persistence.Recycler_View_Adapter;
import com.app.apptuality.talentum.cubelizer.cubelizer.persistence.TratamientoPolygon;

import org.joda.time.DateTime;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static ArrayList<Polygon> aPolygons;
    public static int[][] listaUasFlow_a;

    //private String imageHttpAddress = "";
    /*Elementos del layout content_main*/
    private ImageView ivImagen;
    private TextView tvDimensiones;
    private Bitmap bitmapFloorPlan;
    private Bitmap bitMapBackground;
    private Bitmap bitMapActivity;
    private Bitmap bMapActual;
    private Bitmap nodata;
    private float canvasScale;
    private Display display;
    private Point size;
    //private float dWidth;
    float canvasScaleWidth;
    float canvasScaleHeight;
    float aspecRatio;
    float aWidth,sWidth,aHeight,sHeight;
    RectF imageRectF;
    RectF viewRectF;
    Pintura pintura;
    Canvas canvasGeneral;




    private Bitmap descargab1;
    private Bitmap descargab2;
    private Bitmap descargab3;
    //float scale =1f;
    //PinchZoomImageView mPinchZoomImageView;

    /*Declaración de los botones flotantes*/
    FloatingActionButton fab;
    FloatingActionButton fab1;
    FloatingActionButton fab2;
    FloatingActionButton fab3;
    CoordinatorLayout rootLayout;

    /*Estado de los botones FAB: false -> fab = escondido; true -> fab = visto.*/
    private boolean FAB_Status = false;

    /*Inicializamos las Animaciones de los botones flotantes*/
    Animation show_fab_1;
    Animation hide_fab_1;
    Animation show_fab_2;
    Animation hide_fab_2;
    Animation show_fab_3;
    Animation hide_fab_3;
    String user;
    String password;
    String urlMapa;
    String urlBackground;
    String urlActivity;
    String uasData;
    String uas;
    Connection connection;
    String diaPulsado;
    DatePicker dia;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*Date date = new Date();
        int year = date.getYear() + 1900;
        int month = date.getMonth() + 1;
        diaPulsado = year + "-" + month + "-" + date.getDate();
        */

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            user = (String)extras.get("user");
            password = (String)extras.get("password");
        }
        nodata = BitmapFactory.decodeResource(getResources(), R.drawable.nodata);


        ivImagen = (ImageView)findViewById(R.id.ivImagen);

        new ConnectionMap().execute();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        display = getWindowManager().getDefaultDisplay();
        /*
        if(orientacionDispositivoVertical()){
            dWidth = (float)displayMetrics.widthPixels;

        }else{
            dWidth = (float)displayMetrics.heightPixels;
        }
        */


        /*TRATAMIENTO DEL CALENDARIO*/
        dia = (DatePicker) findViewById(R.id.datePicker);
        /*dia.getYear();
        int g = dia.getYear();
        System.out.println("año :" + g);
        int d = dia.getMonth();
        System.out.println("mes: " + d);
        int s = dia.getDayOfMonth();
        System.out.println("dia: " + s);

        checkFeatures();*/

        calendar = (Calendar) findViewById(R.id.listener_calendar);
        calendar.setDayViewOnClickListener(new Calendar.DayViewOnClickListener() {
            @Override
            public String onDaySelected(int dia) {
                View parentLayout = findViewById(android.R.id.content);
                diaPulsado = String.valueOf(dia);//.getYear() + "-" + datepickerDia.getMonth() + "-" + day);
                Log.e("ListenerMAin","Estoy en daySelected");
                Log.e("ListenerMAin",diaPulsado);
                Snackbar.make(parentLayout, "Seleted Day: " + diaPulsado, Snackbar.LENGTH_SHORT).show();
                diaPulsado = calendar.fechaSelect;
                new ConnectionBackground().execute();
                new ConnectionActivity().execute();
                Log.e("DiaCompletoPulsado",diaPulsado);
                // "2016-09-10";
                //2016-09-10
                //2016-11-25
                //2016-01-10
                return diaPulsado;
            }

        });
        /*
        calendar = (Calendar) findViewById(R.id.listener_calendar);
        calendar.setDayViewOnClickListener(new Calendar.DayViewOnClickListener() {
            @Override
            public String onDaySelected(int day) {
                View parentLayout = findViewById(android.R.id.content);
                diaPulsado = String.valueOf(dia.getYear() + "-" + dia.getMonth() + "-" + day);

                Snackbar.make(parentLayout, "Seleted Day: " + diaPulsado, Snackbar.LENGTH_SHORT).show();
                diaPulsado = "2016-09-10";
                new ConnectionBackground().execute();
                new ConnectionActivity().execute();
                //2016-09-10
                //2016-11-25
                //2016-01-10
                return diaPulsado;
            }

        });
*/
        /*MENU HAMBURGUESA*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*FUNCION QUE GESTIONA LOS BOTONES FLOTANTES*/
        floatingButton();
        rootLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        PhotoViewAttacher photoView = new PhotoViewAttacher(ivImagen);
        photoView.update();
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


    public void checkFeatures() {
        DateTime today = new DateTime();
        DateTime.Property day = today.dayOfMonth();
        DateTime.Property month = today.monthOfYear();
        DateTime.Property year = today.year();
        System.out.println("day: " + day.getAsShortText());
        System.out.println("mes: " + month.getAsShortText());
        System.out.println("año: " + year.getAsShortText());
        //System.out.println("Mes: " + month.getAsText());
        //System.out.println("Mes: " + month.getAsText(Locale.ENGLISH));

        DateTime.Property week = today.weekOfWeekyear();
        System.out.println("Semana: " + week.getAsText());
    }
    private void floatingButton() {
        //Enlazamos las animaciones de los botones flotantes
        show_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_show);
        hide_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_hide);
        show_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_show);
        hide_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_hide);
        show_fab_3 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab3_show);
        hide_fab_3 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab3_hide);

        //Unimos los botones flotantes con sus ids
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab_1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab_2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab_3);


        /*Dándole funcionalidad al botón flotante -> Plano*/
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if (FAB_Status == false) {
                    expandFAB();
                    //Canvas canvas = new Canvas();
                    //PinchZoomImageView pinchZoomImageView = new PinchZoomImageView(MainActivity.this);
                    floorPlanAndBackground();
                    //pinchZoomImageView.draw(canvas);
                    //new ConnectionUAs().execute();
                    //new ConnectionBackground().execute();
                    //Display FAB menu
                    FAB_Status = true;
                } else {
                    //Close FAB menu
                    hideFAB();
                    floorPlanAndBackground();
                    FAB_Status = false;
                }
            }
        });

        /*Dándole funcionalidad al botón flotante -> Plano + Imagen*/
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floorPlanAndBackgroundAndActivity();

            }
        });

        /*Dándole funcionalidad al botón flotante -> Plano + Imagen + Actividad*/
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ConnectionUAs().execute();
                //floorPlanAndBackgroundAndActivityPoligon();

                //floorPlanAndBackgroundAndActivity();
            }
        });

        /*Dándole funcionalidad al botón flotante -> MostrarTodo --> Plano + Imagen + Actividad + Poligonos*/
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new ConnectionUAsData().execute();
                //floorPlanAndBackgroundAndActivityFlechas();
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




    /*OPCIONES DEL MENU*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // VISTAS DE NAVEGACION
        int id = item.getItemId();

        /*if (id == R.id.nav_login) {
            startActivity(new Intent(this, LogoutActivity.class));

        } else */
        if (id == R.id.nav_plano) {
            //Floorplan + background image
            floorPlanAndBackground();

        } else if (id == R.id.nav_foto) {
            //Floorplan + background image + activity map
            floorPlanAndBackgroundAndActivity();

        } else if (id == R.id.nav_calor) {
            //Floorplan + background image + UA polygons + UA flows
            new ConnectionUAs().execute();

        } else if (id == R.id.nav_todo) {
            //Floorplan + background image + activity map + UA polygons + UA flows
            new ConnectionUAsData().execute();

        }else if(id == R.id.nav_salir){
            finish();

       /* } else if (id == R.id.nav_calendar) {
            //Calendario
            dia.setVisibility(View.VISIBLE);
       */
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Potencia el rendimiento de tu empresa con Cubelizer");
            startActivity(Intent.createChooser(intent, "Share with"));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*TRATAMIENTO DEL ZOOM*/
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



    /*EXTRACCION DE LA URL QUE CONTIENE LA IMAGEN DEL PLANO DE LA TIENDA*/
    private class ConnectionMap extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        @Override
        protected void onPreExecute() {
            connection = new Connection(user, password);
            progressDialog.setTitle("Please wait...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            Log.d("Salto", connection.getMap());
            urlMapa = connection.getMap();
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            DescargarImagen(urlMapa);
        }
    }

    /*EXTRACCION DE LA URL QUE CONTIENE LA IMAGEN REAL DE LA TIENDA*/
    private class ConnectionBackground extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        @Override
        protected void onPreExecute() {
            connection = new Connection(user, password, diaPulsado);
            progressDialog.setTitle("Please wait...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            //Log.d("Salto", connection.getDay(diaPulsado));
            urlBackground = connection.getDay(diaPulsado);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            if(urlBackground != ""){
                DescargarBackground(urlBackground);
            }else{
                ivImagen.setImageBitmap(nodata);
            }

        }
    }

    /*EXTRACCION DE LA URL QUE CONTIENE LA IMAGEN DE ACTIVIDAD DE LA TIENDA*/
    private class ConnectionActivity extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        @Override
        protected void onPreExecute() {
            connection = new Connection(user, password);
            progressDialog.setTitle("Please wait...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            Log.d("Salto", connection.getDay2(diaPulsado));
            urlActivity = connection.getDay2(diaPulsado);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            DescargarActivity(urlActivity);
        }
    }


    /*EXTRACCION DE LA URL QUE CONTIENE LA IMAGEN DE ACTIVIDAD DE LA TIENDA*/
    private class ConnectionUAs extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        @Override
        protected void onPreExecute() {
            connection = new Connection(user, password);
            progressDialog.setTitle("Please wait...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            //Log.d("Salto", connection.getUas());
            uas = connection.getUas();
            Log.d("uas", uas);
            JsonUAs jsonUAs = new JsonUAs();
            jsonUAs.devolucionArray(uas);
            aPolygons =  jsonUAs.getPolygons();
            System.out.println("1er "+ aPolygons);
            Log.d("movida ==>",aPolygons.toString() );
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            floorPlanAndBackgroundAndActivityPoligon();
        }
    }

    /*EXTRACCION DE LA URL QUE CONTIENE LA IMAGEN DE ACTIVIDAD DE LA TIENDA*/
    private class ConnectionUAsData extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        @Override
        protected void onPreExecute() {
            connection = new Connection(user, password);
            progressDialog.setTitle("Please wait...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            Log.d("Salto", connection.getDayUasData(diaPulsado));
            uasData = connection.getDayUasData(diaPulsado);
            Affluence affluence = new Affluence();
            affluence.getArrayFlow(uasData);
            listaUasFlow_a = affluence.getListaUasFlow_a();
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();

            floorPlanAndBackgroundAndActivityFlechas();
        }
    }

    private static final Handler handler = new Handler();
    private void DescargarImagen(final String imageHttpAddress) {
        Thread networkThread = new Thread() {
            @Override
            public void run(){
                try {
                    URL imageUrl = null;
                    imageUrl = new URL(imageHttpAddress);
                    HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                    conn.connect();

                    bitmapFloorPlan = BitmapFactory.decodeStream(conn.getInputStream());
                    //System.out.println("****bmap width1" +  bitmapFloorPlan.getWidth());
                    aWidth = (float)bitmapFloorPlan.getWidth();
                    aHeight = (float)bitmapFloorPlan.getHeight();
                    aspecRatio = aHeight / aWidth;

                    if(orientacionDispositivoVertical()){
                        bitmapFloorPlan = Bitmap.createScaledBitmap(bitmapFloorPlan,ivImagen.getWidth(),(int)(ivImagen.getWidth()*aspecRatio),true);
                        //System.out.println("****bmap width2" +  bitmapFloorPlan.getWidth());
                        sWidth = (float)bitmapFloorPlan.getWidth()/aWidth;
                    }else{
                        bitmapFloorPlan = Bitmap.createScaledBitmap(bitmapFloorPlan,(int)(ivImagen.getHeight()/aspecRatio),(ivImagen.getHeight()),true);
                        sHeight = (float)bitmapFloorPlan.getHeight()/aHeight;
                    }
                    //bitmapFloorPlan = Bitmap.createScaledBitmap(bitmapFloorPlan,(int)ivImagen.getWidth(),(int)(ivImagen.getWidth()*aspecRatio),true);
                    //Drawable d = ivImagen.getDrawable();
                    // TODO: check that d isn't null

                    //imageRectF = new RectF(0, 0, bitmapFloorPlan.getWidth(), bitmapFloorPlan.getHeight());
                    //viewRectF = new RectF(0, 0, ivImagen.getWidth(), ivImagen.getHeight());
                    runOnUiThread(new Runnable() {
                        public void run() {
                            descargab1 = bitmapFloorPlan;
                            floorPlan(descargab1);
                            //ivImagen.setImageBitmap(bitmapFloorPlan);
                        }
                    });
                } catch (Exception e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Error ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        };
        networkThread.start();
    }

    private void DescargarBackground(final String imageHttpAddress) {
        Thread networkThread = new Thread() {
            @Override
            public void run(){
                try {
                    URL imageUrl = null;
                    imageUrl = new URL(imageHttpAddress);
                    HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                    conn.connect();
                    bitMapBackground = BitmapFactory.decodeStream(conn.getInputStream());
                    bitMapBackground = Bitmap.createScaledBitmap(bitMapBackground, descargab1.getWidth(), descargab1.getHeight(), true);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            descargab2 = bitMapBackground;
                            //FloorPlanAndBackground(bitMapBackground);
                            //ivImagen.setImageBitmap(bitmapFloorPlan);
                        }
                    });
                } catch (Exception e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Error ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        };
        networkThread.start();
    }

    private void DescargarActivity(final String imageHttpAddress) {
        Thread networkThread = new Thread() {
            @Override
            public void run(){
                try {
                    URL imageUrl = null;
                    imageUrl = new URL(imageHttpAddress);
                    HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                    conn.connect();
                    bitMapActivity = BitmapFactory.decodeStream(conn.getInputStream());
                    bitMapActivity = Bitmap.createScaledBitmap(bitMapActivity, descargab1.getWidth(), descargab1.getHeight(), true);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            descargab3 = bitMapActivity;
                            //FloorPlanAndBackgroundAndActivity(bitMapActivity);
                            //ivImagen.setImageBitmap(bitmapFloorPlan);
                        }
                    });
                } catch (Exception e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Error ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        };
        networkThread.start();
    }

    public void floorPlan(Bitmap bitmapFloorPlan){
        Bitmap bM = Bitmap.createBitmap(bitmapFloorPlan.getWidth(),bitmapFloorPlan.getHeight(),bitmapFloorPlan.getConfig());
        Canvas canvas = new Canvas(bM);
        //Paint p = new Paint();
        //p.setAlpha(229);

        //Matrix matrix = new Matrix();

        //matrix.setRectToRect(imageRectF, imageRectF, Matrix.ScaleToFit.CENTER);

        canvas.drawBitmap(bitmapFloorPlan, new Matrix(), null);
        //canvas.drawBitmap(bitMapBackground, new Matrix(),p);

        ivImagen.setImageBitmap(bM);

        //ivImagen.setImageMatrix(matrix);

        //tvDimensiones.setText(Integer.toString(bitmapFloorPlan.getWidth()) + " x " + Integer.toString(bitmapFloorPlan.getHeight()));
        //mPinchZoomImageView.setImageBitmap(bMapPlano);
    }

    public void floorPlanAndBackground() {
        Bitmap bM = Bitmap.createBitmap(descargab1.getWidth(),descargab1.getHeight(),descargab1.getConfig());
        Canvas canvas = new Canvas(bM);
        //canvas.restore();
        //canvas.setBitmap(bM);
        Paint p = new Paint();
        p.setAlpha(229);
        canvas.drawBitmap(bitmapFloorPlan, new Matrix(), null);
        canvas.drawBitmap(bitMapBackground, new Matrix(),p);
        ivImagen.setImageBitmap(bM);
        //tvDimensiones.setText(Integer.toString(bitmapFloorPlan.getWidth()) + " x " + Integer.toString(bitmapFloorPlan.getHeight()));
    }

    public void floorPlanAndBackgroundAndActivity() {
        Bitmap bM = Bitmap.createBitmap(descargab1.getWidth(),descargab1.getHeight(),descargab1.getConfig());
        Canvas canvas = new Canvas(bM);
        //canvas.restore();
        //canvas.setBitmap(bM);
        Paint p = new Paint();
        p.setAlpha(178);
        canvas.drawBitmap(bitmapFloorPlan, new Matrix(), null);
        canvas.drawBitmap(bitMapBackground, new Matrix(),p);
        canvas.drawBitmap(bitMapActivity, new Matrix(),p);
        ivImagen.setImageBitmap(bM);
        // canvasGeneral = canvas;
        //tvDimensiones.setText(Integer.toString(bitmapFloorPlan.getWidth()) + " x " + Integer.toString(bitmapFloorPlan.getHeight()));

    }

    private void floorPlanAndBackgroundAndActivityPoligon() {
        Bitmap bM = Bitmap.createBitmap(bitmapFloorPlan.getWidth(),bitmapFloorPlan.getHeight(),bitmapFloorPlan.getConfig());
        Canvas canvas = new Canvas();
        //canvas.restore();
        canvas.setBitmap(bM);
        Paint p = new Paint();
        p.setAlpha(178);
        canvas.drawBitmap(bitmapFloorPlan, new Matrix(), null);
        canvas.drawBitmap(bitMapBackground, new Matrix(),p);
        canvas.drawBitmap(bitMapActivity, new Matrix(),p);
        ivImagen.setImageBitmap(bM);

        if(orientacionDispositivoVertical()){
            canvasScaleWidth = sWidth;
            canvasScaleHeight = sWidth*aspecRatio;
        }else{
            canvasScaleHeight = sHeight;
            canvasScaleWidth = sHeight/aspecRatio;
        }


        //canvasScaleWidth = (float)bMapImagen.getWidth()/(float) aWidth;
        // canvasScaleHeight = canvasScaleWidth*aspecRatio;
        canvas.scale(canvasScaleWidth,canvasScaleHeight);
        System.out.println("****canvas width" + canvas.getMaximumBitmapWidth() + "canvas density" + canvas.getDensity() + "bitmap width" + bM.getWidth());
        // canvasGeneral = canvas;
        //tvDimensiones.setText(Integer.toString(bitmapFloorPlan.getWidth()) + " x " + Integer.toString(bitmapFloorPlan.getHeight()));
        pintarPoligonos(canvas);
        //canvasScaleWidth = (float)ivImagen.getWidth()/(float)canvas.getWidth();

        //canvasScaleHeight = canvasScaleWidth*aspecRatio;
        //canvas.scale(canvasScaleWidth,canvasScaleHeight);
    }
    private void floorPlanAndBackgroundAndActivityFlechas() {
        Bitmap bM = Bitmap.createBitmap(bitmapFloorPlan.getWidth(),bitmapFloorPlan.getHeight(),bitmapFloorPlan.getConfig());
        Canvas canvas = new Canvas();
        //canvas.restore();
        canvas.setBitmap(bM);
        Paint p = new Paint();
        p.setAlpha(178);
        canvas.drawBitmap(bitmapFloorPlan, new Matrix(), null);
        canvas.drawBitmap(bitMapBackground, new Matrix(),p);
        canvas.drawBitmap(bitMapActivity, new Matrix(),p);
        ivImagen.setImageBitmap(bM);
        // canvasGeneral = canvas;
        //tvDimensiones.setText(Integer.toString(bitmapFloorPlan.getWidth()) + " x " + Integer.toString(bitmapFloorPlan.getHeight()));
        //pintarPoligonos(canvas);
        if(orientacionDispositivoVertical()){
            canvasScaleWidth = sWidth;
            canvasScaleHeight = sWidth*aspecRatio;
        }else{
            canvasScaleHeight = sHeight;
            canvasScaleWidth = sHeight/aspecRatio;
        }
        //canvasScaleWidth = (float)bMapImagen.getWidth()/(float) aWidth;
        // canvasScaleHeight = canvasScaleWidth*aspecRatio;
        canvas.scale(canvasScaleWidth,canvasScaleHeight);
        pintarUAs(canvas);

    }
    public void pintarPoligonos(Canvas canvas){
        //canvas.restore();


        //float aspecRatio = (float) bMapPlano.getHeight() / (float) bMapPlano.getWidth();


        //float canvasScaleHeight = (float)displayMetrics.heightPixels/(float) bMapPlano.getHeight();
        //canvas.scale(canvasScaleWidth,canvasScaleHeight);
        Paint pZona = new Paint();
        pZona.setColor(Color.CYAN);
        pZona.setStrokeWidth(5);
        pZona.setStyle(Paint.Style.STROKE);

        Path pathPolygons = new Path();
/*
        TratamientoPolygon tratamientoPolygon = new TratamientoPolygon();

      String sector1 = "Name: Entrance Polygon: [[0,0],[559,0],[559,41],[346,188],[209,198],[97,203],[0,145]]";
        tratamientoPolygon.convertirAPolygon(sector1);
        String sector2 = "Name: Left Polygon: [[0,145],[97,203],[209,198],[224,332],[234,434],[0,461]]";
        tratamientoPolygon.convertirAPolygon(sector2);
        String sector3 = "Name: Upper-right Polygon: [[209,198],[346,188],[559,41],[559,237],[378,304],[224,332]]";
        tratamientoPolygon.convertirAPolygon(sector3);
        String sector4 = "Lower-right  Polygon: [[224,332],[378,304],[559,237],[559,464],[309,426],[234,434]]";
        tratamientoPolygon.convertirAPolygon(sector4);
        String sector5 = "Name: Chairs Polygon: [[0,461],[234,434],[309,426],[559,464],[559,559],[0,559]]";
        tratamientoPolygon.convertirAPolygon(sector5);

        ArrayList<Polygon> aPolygons = tratamientoPolygon.getPolygons();
*/
        dibujarPoligonos (aPolygons, pathPolygons, canvas, pZona);
        System.out.println("2do "+ aPolygons);


    }
    public void dibujarPoligonos(ArrayList<Polygon> aPolygons, Path pathPolygons, Canvas canvas, Paint pZona){
        for (Polygon polygon : aPolygons){
            int i=0;
            ArrayList<Point> arrayPolygon = (ArrayList<Point>) polygon.getPolygon();
            for(Point punto : arrayPolygon){
                if(i==0){
                    pathPolygons.moveTo(punto.x, punto.y);
                    pathPolygons.lineTo(punto.x, punto.y);
                    i=i+1;
                }else{
                    pathPolygons.lineTo(punto.x, punto.y);
                }
            }
            pathPolygons.lineTo(arrayPolygon.get(0).x, arrayPolygon.get(0).y);
        }
        canvas.drawPath(pathPolygons, pZona);
    }

    public void pintarUAs(Canvas canvas){
        Paint pZona = new Paint();
        pZona.setColor(Color.CYAN);
        pZona.setStrokeWidth(5);
        pZona.setStyle(Paint.Style.STROKE);

        Path pathPolygons = new Path();
        dibujarPoligonos (aPolygons, pathPolygons, canvas, pZona);


        Paint pCirculoIn = new Paint();
        pCirculoIn.setStyle(Paint.Style.STROKE);
        pCirculoIn.setAntiAlias(true);
        pCirculoIn.setStrokeWidth(3);
        pCirculoIn.setColor(Color.GREEN);

        Paint pCirculoOut = new Paint();
        pCirculoOut.setStyle(Paint.Style.FILL);
        pCirculoOut.setAntiAlias(true);
        pCirculoOut.setStrokeWidth(3);
        pCirculoOut.setColor(Color.RED);

        Paint pLineaFlecha = new Paint();
        pLineaFlecha.setColor(Color.WHITE);
        pLineaFlecha.setStyle(Paint.Style.STROKE);
        pLineaFlecha.setAlpha(100);

        Paint pPuntaFlecha = new Paint();
        pPuntaFlecha.setColor(Color.WHITE);
        pPuntaFlecha.setStyle(Paint.Style.STROKE);
        pPuntaFlecha.setAlpha(100);

        Path pathCirculosOut = new Path();
        Path pathCirculosIn = new Path();

        dibujarCirculos (aPolygons, listaUasFlow_a, pathCirculosOut, pathCirculosIn, canvas, pCirculoIn, pCirculoOut);

        Path pathLineaFlecha = new Path();
        Path pathPuntaFlecha = new Path();

        dibujarFlechas (aPolygons,listaUasFlow_a,pathLineaFlecha, canvas, pLineaFlecha,pathPuntaFlecha,pPuntaFlecha);


    }




    /*
   TRATAMIENTO DE CAPTURAS DE PANTALLA
    */
    /*
    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }
*/
    public ArrayList<Point> obtenerCentros(ArrayList<Polygon> aPolygons){
        //con esta funcion obtenemos un array con todos los centros
        ArrayList<Point> centros = new ArrayList<Point>();
        int i = 0;
        //recorremos el array de objetos poligonos para sacar cada polígono
        for (Polygon polygon : aPolygons){
            //vertemos en la variable "iVertices" el tamaño del array de puntos de cada polígono
            int iVertices = polygon.getPolygon().size();
            //añadimos al array de puntos "centros" el centro de cada polígono
            //para ello invocamos a la función encontrarCentro pasándole el array de puntos de cada
            //Polígono y el número total de puntos "iVertices"
            centros.add(encontrarCentro(polygon.getPolygon(), iVertices));
            System.out.println("****ARRAY_CENTROS: Polígono " + (i+1) + " = (" + centros.get(i).x + "," + centros.get(i).y + ")");
            i=i+1;
        }
        return centros;
    }

    public void dibujarCirculos (ArrayList<Polygon> aPolygons, int[][] listaUasFlow_a, Path pathCirculosOut, Path pathCirculosIn, Canvas canvas, Paint pCirculoIn, Paint pCirculoOut){
        //sacamos el flujo mayor para escalar lo que dibujemos
        int numeroMayor = flujoMayor(listaUasFlow_a);
        System.out.println("****FLUJO MAYOR: " + numeroMayor);
        ArrayList<Point> centros = obtenerCentros(aPolygons);
        //for (Polygon polygon : aPolygons) {
        // List<Point> arrayPolygon = polygon.getPolygon();
        //i representa a "x" (destino)
        int i = 0;
        for (int[] flujosZona : listaUasFlow_a) {
            //k representa a "y" (salida)
            int k = 0;
            //si i(x)=0 son los flujos hasta el exterior
            //hasta el exterior (circulo rojo)
            for (int flujo : flujosZona) {
                System.out.println("****ARRAY_FLOW int2: [x]= " + i + "[y]= " + k + ", " + listaUasFlow_a[i][k]);
                System.out.println("****ARRAY_FLOW int3: [x]= " + i + "[y]= " + k + ", " + flujo);
                if (i == 0) {
                    if (flujo != 0) {
                        pathCirculosOut.addCircle((float)(centros.get(k-1).x), (float)(centros.get(k-1).y), diametroCirculo(numeroMayor, flujo), Path.Direction.CW);
                        canvas.drawPath(pathCirculosOut, pCirculoOut);
                        pathCirculosOut.reset();
                    }
                    k = k + 1;
                } else {
                    //desde el exterior(circulo verde)
                    if (k == 0) {
                        if (flujo != 0) {
                            pathCirculosIn.addCircle((float)(centros.get(i-1).x), (float)(centros.get(i-1).y), diametroCirculo(numeroMayor, flujo), Path.Direction.CW);
                            canvas.drawPath(pathCirculosIn, pCirculoIn);
                            pathCirculosIn.reset();
                        }
                    }
                    k = k + 1;
                }
            }
            i = i + 1;
        }
        //}
    }

    // @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void dibujarFlechas (ArrayList<Polygon> aPolygons, int[][] listaUasFlow_a, Path pathLineaFlecha, Canvas canvas, Paint pLineaFlecha, Path pathPuntaFlecha, Paint pPuntaFlecha){
        //sacamos el flujo mayor para escalar lo que dibujemos
        Point puntoControl = new Point();
        int numeroMayor = flujoMayor(listaUasFlow_a);
        //System.out.println("****FLUJO MAYOR: " + numeroMayor);
        ArrayList<Point> centros = obtenerCentros(aPolygons);
        //for (Polygon polygon : aPolygons) {
        // List<Point> arrayPolygon = polygon.getPolygon();
        //i representa a "x" (destino)
        int i = 0;

        for (int[] flujosZona : listaUasFlow_a) {
            //k representa a "y" (salida)
            int k = 0;
            int j = 0;
            //si i(x)=0 son los flujos hasta el exterior
            //hasta el exterior (circulo rojo)
            for (int flujo : flujosZona) {
                //System.out.println("****ARRAY_FLOW int2: [x]= " + i + "[y]= " + k + ", " + listaUasFlow_a[i][k]);
                //System.out.println("****ARRAY_FLOW int3: [x]= " + i + "[y]= " + k + ", " + flujo);
                //si i(x)!=0 son los flujos de zonas
                if (i != 0) {
                    //si ii(y)!=0 son los flujos entre zonas
                    if (k != 0) {
                        if (flujo != 0) {
                            pLineaFlecha.setStrokeWidth(grosorFlecha(numeroMayor, flujo));
                            pathLineaFlecha.moveTo((float)(centros.get(k-1).x), (float)(centros.get(k-1).y));
                            puntoControl = puntoControl((float)(centros.get(k-1).x),(float)(centros.get(k-1).y),(float)(centros.get(i-1).x),(float)(centros.get(i-1).y));
                            pathLineaFlecha.quadTo((float)(puntoControl.x), (float)(puntoControl.y),(float)((centros.get(i-1).x)+j), (float)((centros.get(i-1).y)+j));
                            //pathLineaFlecha.quadTo(((float)((centros.get(k-1).x)+80)), (float)((centros.get(k-1).y)+80),(float)((centros.get(i-1).x)+j), (float)((centros.get(i-1).y)+j));
                            //pathLineaFlecha.lineTo((float)((centros.get(i-1).x)+0), (float)((centros.get(i-1).y)+0));

                            canvas.drawPath(pathLineaFlecha, pLineaFlecha);
                            pathLineaFlecha.reset();
                            //dibujamos punta de flecha
                            pPuntaFlecha.setStrokeWidth(grosorFlecha(numeroMayor, flujo));
                            puntaFlecha(pathPuntaFlecha, (float)(puntoControl.x), (float)(puntoControl.y), (float)((centros.get(i-1).x)+j), (float)((centros.get(i-1).y)+j), grosorPuntaFlecha(numeroMayor, flujo));
                            //puntaFlecha(pathPuntaFlecha, (float)(centros.get(k-1).x), (float)(centros.get(k-1).y), (float)((centros.get(i-1).x)+j), (float)((centros.get(i-1).y)+j), grosorPuntaFlecha(numeroMayor, flujo));
                            canvas.drawPath(pathPuntaFlecha, pPuntaFlecha);
                            pathPuntaFlecha.reset();
                            //j=j+25;
                            System.out.println("****FLECHA: de Poligono: " + k + " a Poligono: " + i + ", con flujo " + flujo + " y grosor " + grosorFlecha(numeroMayor, flujo));
                            System.out.println("****PUNTA FLECHA: de Poligono: " + k + " a Poligono: " + i + ", con flujo " + flujo + " y grosor " + grosorFlecha(numeroMayor, flujo));
                        }
                    }
                    k = k + 1;
                }
            }
            i = i + 1;
        }
        //}
    }


    public float grosorPuntaFlecha(int numeroMaximo, int flujo){
        float grosor = 0.f;

        if(flujo != 0){
            grosor=((flujo*35)/numeroMaximo)+15;
        }else{
            grosor=0;
        }

        //System.out.println(grosor);

        return grosor;
    }



    public Point encontrarCentro(ArrayList<Point> puntos, int numeroPuntos){
        Point off = puntos.get(0);
        int twicearea = 0;
        int x = 0;
        int y = 0;
        Point p1, p2, centro;
        int f;
        for (int i = 0, j= numeroPuntos -1;i<numeroPuntos; j= i++){
            p1 = puntos.get(i);
            p2 = puntos.get(j);
            f = (p1.x - off.x) * (p2.y - off.y) - (p2.x - off.x) * (p1.y -off.y);
            twicearea += f;
            x += (p1.x +p2.x - 2 * off.x) * f;
            y += (p1.y + p2.y -2 * off.y) *f;
        }
        f= twicearea *3;

        centro = new Point();
        centro.x= x/f + off.x;
        centro.y = y/f + off.y;

        return centro;
    }

    public float diametroCirculo(int numeroMaximo, int flujo){
        float grosor = 0.f;

        if(flujo != 0){
            grosor=((flujo*20)/numeroMaximo)+5;
        }else{
            grosor=0;
        }

        //System.out.println(grosor);

        return grosor;

    }

    public float grosorFlecha(int numeroMaximo, int flujo){
        float grosor = 0.f;

        if(flujo != 0){
            grosor=((flujo*17)/numeroMaximo)+3;
        }else{
            grosor=0;
        }

        //System.out.println(grosor);

        return grosor;

    }


    public int flujoMayor(int[][]array){
        int numeroMayor = 0;

        for(int[] a : array) {
            for (int b : a) {
                if (b > numeroMayor) {
                    numeroMayor = b;
                }
            }
        }
        return numeroMayor;
    }



    public void puntaFlecha(Path path, float fromx, float fromy, float tox, float toy, float grosor){
        int headlen = (int)grosor;   // length of head in pixels
        double angle = Math.atan2(toy-fromy,tox-fromx);
        path.moveTo(tox, toy);
        path.lineTo((float)(tox-headlen* Math.cos(angle+ Math.PI/6)),(float)(toy-headlen* Math.sin(angle+ Math.PI/6)));
        path.moveTo(tox, toy);
        path.lineTo((float)(tox-headlen* Math.cos(angle- Math.PI/6)),(float)(toy-headlen* Math.sin(angle- Math.PI/6)));
    }

    public Point puntoControl(float fromx, float fromy, float tox, float toy){
        double distancia = Math.hypot(toy-fromy,tox-fromx);
        double longitud = Math.hypot((distancia/2),(distancia/2));   // length of head in pixels
        double angle = Math.atan2(toy-fromy,tox-fromx);
        Point puntoControl = new Point();
        puntoControl.x = (int)(tox-longitud* Math.cos(angle+ Math.PI/6));
        puntoControl.y = (int)(toy-longitud* Math.sin(angle+ Math.PI/6));
        return  puntoControl;
        //path.moveTo(tox, toy);
        //path.lineTo((float)(tox-headlen*Math.cos(angle-Math.PI/6)),(float)(toy-headlen*Math.sin(angle-Math.PI/6)));
    }



}

