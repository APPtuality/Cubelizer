package com.app.apptuality.talentum.cubelizer.cubelizer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
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
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import com.app.apptuality.talentum.cubelizer.cubelizer.persistence.PinchZoomImageView;
import com.app.apptuality.talentum.cubelizer.cubelizer.persistence.Polygon;
import com.app.apptuality.talentum.cubelizer.cubelizer.persistence.Recycler_View_Adapter;
import com.app.apptuality.talentum.cubelizer.cubelizer.persistence.TratamientoPolygon;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static ArrayList<Polygon> aPolygons;

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

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            user = (String)extras.get("user");
            password = (String)extras.get("password");
        }
        nodata = BitmapFactory.decodeResource(getResources(), R.drawable.nodata);
        ivImagen = (ImageView)findViewById(R.id.ivImagen);
        new ConnectionMap().execute();


        /*TRATAMIENTO DEL CALENDARIO*/
        dia = (DatePicker) findViewById(R.id.datePicker);
        calendar = (Calendar) findViewById(R.id.listener_calendar);
        calendar.setDayViewOnClickListener(new Calendar.DayViewOnClickListener() {
            @Override
            public String onDaySelected(int dia) {
                View parentLayout = findViewById(android.R.id.content);
                diaPulsado = String.valueOf(dia);//.getYear() + "-" + dia.getMonth() + "-" + day);

                Snackbar.make(parentLayout, "Seleted Day: " + diaPulsado, Snackbar.LENGTH_SHORT).show();
                diaPulsado = "2016-09-10";
                //2016-09-10
                //2016-11-25
                //2016-01-10
                return diaPulsado;
            }
        });

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
                    new ConnectionUAsData().execute();
                    //pinchZoomImageView.draw(canvas);
                    //new ConnectionUAs().execute();
                    //new ConnectionBackground().execute();
                    //Display FAB menu
                    FAB_Status = true;
                } else {
                    //Close FAB menu
                    hideFAB();
                    FAB_Status = false;
                }
            }
        });

        /*Dándole funcionalidad al botón flotante -> Plano + Imagen*/
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //planoActividad();
            }
        });

        /*Dándole funcionalidad al botón flotante -> Plano + Actividad*/
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mostrarFlow();
            }
        });

        /*Dándole funcionalidad al botón flotante -> MostrarTodo*/
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mostrarFlow();
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

        if (id == R.id.nav_login) {
            startActivity(new Intent(this, LogoutActivity.class));

        } else if (id == R.id.nav_plano) {
            //Floorplan + background image
            new ConnectionBackground().execute();

        } else if (id == R.id.nav_foto) {
            //Floorplan + background image + activity map
            new ConnectionActivity().execute();

        } else if (id == R.id.nav_calor) {
            //Floorplan + background image + UA polygons + UA flows


        } else if (id == R.id.nav_todo) {
            //Floorplan + background image + activity map + UA polygons + UA flows

        } else if (id == R.id.nav_calendar) {
            //Calendario
            dia.setVisibility(View.VISIBLE);


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
            Log.d("uasData", uasData);
            JsonUAs jsonUAs = new JsonUAs();
            jsonUAs.devolucionArray(uasData);
            aPolygons =  jsonUAs.getPolygons();

            Log.d("movida ==>",aPolygons.toString() );
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            //DescargarActivity(urlActivity);
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
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            //DescargarActivity(urlActivity);
            if(uasData != ""){
                //DescargarBackground(uasData);
            }else{
                ivImagen.setImageBitmap(nodata);
            }
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
                runOnUiThread(new Runnable() {
                    public void run() {
                        FloorPlan(bitmapFloorPlan);
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
                    runOnUiThread(new Runnable() {
                        public void run() {
                            FloorPlanAndBackground(bitMapBackground);
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
                    runOnUiThread(new Runnable() {
                        public void run() {
                            FloorPlanAndBackgroundAndActivity(bitMapActivity);
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

    public void FloorPlan(Bitmap bitmapFloorPlan){
        Bitmap bM = Bitmap.createBitmap(bitmapFloorPlan.getWidth(),bitmapFloorPlan.getHeight(),bitmapFloorPlan.getConfig());
        Canvas canvas = new Canvas(bM);
        //Paint p = new Paint();
        //p.setAlpha(229);
        canvas.drawBitmap(bitmapFloorPlan, new Matrix(), null);
        //canvas.drawBitmap(bitMapBackground, new Matrix(),p);
        ivImagen.setImageBitmap(bM);
        //tvDimensiones.setText(Integer.toString(bitmapFloorPlan.getWidth()) + " x " + Integer.toString(bitmapFloorPlan.getHeight()));
        //mPinchZoomImageView.setImageBitmap(bMapPlano);
    }

    public void FloorPlanAndBackground(Bitmap bitMapBackgroun) {
        bitMapBackground = bitMapBackgroun;
        Bitmap bM = Bitmap.createBitmap(bitMapBackground.getWidth(),bitMapBackground.getHeight(),bitMapBackground.getConfig());
        Canvas canvas = new Canvas();
        canvas.setBitmap(bM);
        Paint p = new Paint();
        p.setAlpha(229);
        //canvas.drawBitmap(bitmapFloorPlan, new Matrix(), null);
        canvas.drawBitmap(bitMapBackground, new Matrix(),null);
        ivImagen.setImageBitmap(bM);
        //tvDimensiones.setText(Integer.toString(bitmapFloorPlan.getWidth()) + " x " + Integer.toString(bitmapFloorPlan.getHeight()));
    }

    public void FloorPlanAndBackgroundAndActivity(Bitmap bitMapActivity) {
        Bitmap bM = Bitmap.createBitmap(bitMapActivity.getWidth(),bitMapActivity.getHeight(),bitMapActivity.getConfig());
        Canvas canvas = new Canvas();
        canvas.setBitmap(bM);
        Paint p = new Paint();
        p.setAlpha(229);
        //canvas.drawBitmap(bitmapFloorPlan, new Matrix(), null);
        //canvas.drawBitmap(bitMapBackground, new Matrix(),null);
        canvas.drawBitmap(bitMapActivity, new Matrix(),null);
        canvas.drawBitmap(bitMapBackground, new Matrix(),p);
        ivImagen.setImageBitmap(bM);
        //tvDimensiones.setText(Integer.toString(bitmapFloorPlan.getWidth()) + " x " + Integer.toString(bitmapFloorPlan.getHeight()));
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
}

