package com.app.apptuality.talentum.cubelizer.cubelizer.persistence;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import com.app.apptuality.talentum.cubelizer.cubelizer.MainActivity;
import com.app.apptuality.talentum.cubelizer.cubelizer.persistence.Affluence;
import com.app.apptuality.talentum.cubelizer.cubelizer.persistence.Polygon;
import com.app.apptuality.talentum.cubelizer.cubelizer.persistence.TratamientoPolygon;

import java.util.ArrayList;

/**
 * Created by Veronica on 27/11/2016.
 */

public class PinchZoomImageView extends ImageView {

    private Bitmap mBitmap;
    private int mImageWidth;
    private int mImageHeight;
    private final static float mMinZoom = 1.f;
    private final static float mMaxZoom = 3.f;
    private float mScaleFactor = 1.f;
    private ScaleGestureDetector mScaleGestureDetector;
    private final static int NONE = 0;
    private final static int PAN = 1;
    private final static int ZOOM = 2;
    private int mEventState;
    private float mStartX = 0;
    private float mStartY = 0;
    private float mTranslateX = 0;
    private float mTranslateY = 0;
    private float mPreviousTranslateX = 0;
    private float mPreviousTranslateY = 0;
    private float canvasScale;


    private Affluence affluence;
    private TratamientoPolygon tratamientoPolygon;

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(mMinZoom, Math.min(mMaxZoom, mScaleFactor));
            // invalidate();
            // requestLayout();
            return super.onScale(detector);
        }
    }

    public PinchZoomImageView(Context context) {
        super(context);
    }

    /*
        public PinchZoomImageView(Context context, AttributeSet attrs) {
            super(context, attrs);

            mScaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
        }
    */

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //desde aqui es lo relacionado con dibujar las zonas y flujos
        Paint pZona = new Paint();
        pZona.setColor(Color.BLUE);
        pZona.setStrokeWidth(5);
        pZona.setStyle(Paint.Style.STROKE);

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
        pLineaFlecha.setColor(Color.CYAN);
        pLineaFlecha.setStyle(Paint.Style.STROKE);
        pLineaFlecha.setAlpha(50);

        Paint pPuntaFlecha = new Paint();
        pPuntaFlecha.setColor(Color.CYAN);
        pPuntaFlecha.setStyle(Paint.Style.STROKE);
        pPuntaFlecha.setAlpha(50);

        Path pathPolygons = new Path();
/*
        tratamientoPolygon = new TratamientoPolygon();

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
*/
        ArrayList<Polygon> aPolygons = MainActivity.aPolygons;
        //Log.i("POLIGONOS",aPolygons.toString());
        //JsonUAs jsonUAs = new JsonUAs();
        //ArrayList<Polygon> aPolygons = tratamientoPolygon.convertirAPolygon(jsonUAs.toString());
        dibujarPoligonos (aPolygons, pathPolygons, canvas, pZona);



        Path pathCirculosOut = new Path();
        Path pathCirculosIn = new Path();
        affluence = new Affluence();

        String uas = "[[0, 741, 142, 0, 0, 173], [772, 0, 0, 435, 0, 0], [0, 293, 0, 0, 0, 284], [0, 173, 0, 0, 426, 0], [0, 0, 9, 164, 0, 284], [284, 0, 426, 0, 31, 0]]";
        affluence.getArrayFlow(uas);

        int[][] listaUasFlow_a = affluence.getListaUasFlow_a();

        dibujarCirculos (aPolygons, listaUasFlow_a, pathCirculosOut, pathCirculosIn, canvas, pCirculoIn, pCirculoOut);

        Path pathLineaFlecha = new Path();
        Path pathPuntaFlecha = new Path();

        dibujarFlechas (aPolygons,listaUasFlow_a,pathLineaFlecha, canvas, pLineaFlecha,pathPuntaFlecha,pPuntaFlecha);

        canvas.restore();
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

    /*public ArrayList<Point> obtenerCentros(ArrayList<Polygon> aPolygons){
        //con esta funcion obtenemos un array con todos los centros
        ArrayList<Point> centros = new ArrayList<Point>();
        //sacamos el numero de poligonos total
        //int nPoligonos = aPolygons.size();
        ArrayList<Point> puntos2 = new ArrayList<Point>();
        //nos recorremos el array de poligonos
        for (Polygon polygon : aPolygons) {
            //a cada poligono le saco su List de puntos
            //y recorremos este array para guardarlo en
            //un array de Puntos
            for(Point punto : polygon.getPolygon()) {
                puntos2.add(punto);
            }
            centros.add(encontrarCentro(puntos2, puntos2.size()));
        }
        return centros;
    }*/

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
                            //dibujamos linea de flecha
                            //pathLineaFlecha.addArc((float)(centros.get(k-1).x), (float)(centros.get(k-1).y),(float)((centros.get(i-1).x)+0), (float)((centros.get(i-1).y)+0), 0,360);
                            //pathLineaFlecha.lineTo((float)((centros.get(i-1).x)+0), (float)((centros.get(i-1).y)+0));
                            //pLineaFlecha.setStrokeWidth(grosorFlecha(numeroMayor, flujo));

                            //final RectF arrowOval = new RectF();
                            //arrowOval.set((float)((centros.get(k-1).x)), (float)((centros.get(k-1).y)+0),
                               //     (float)((centros.get(i-1).x)+0), (float)((centros.get(i-1).y)+0));

                            //add the oval to path
                           // pathLineaFlecha.addArc(arrowOval,-180,180;

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

    /*public void dibujarFlechas (ArrayList<Polygon> aPolygons, int[][] listaUasFlow_a, Path pathLineaFlecha, Canvas canvas, Paint pLineaFlecha, Path pathPuntaFlecha,Paint pPuntaFlecha) {
        int numeroMayor = flujoMayor(listaUasFlow_a);
        ArrayList<Point> centros = new ArrayList<Point>();
        centros = obtenerCentros(aPolygons);
        int i = 0;
        for (int[] flujosZona : listaUasFlow_a) {
            //ii representa a "y" (salida)

            //si i(x)!=0 son los flujos de zonas
            if (i != 0) {
                int ii = 0;
                for (int flujo : flujosZona) {
                    //si ii(y)!=0 son los flujos entre zonas
                    if(ii!=0){
                        if(flujo!=0){
                            //dibujamos linea de flecha
                            pathLineaFlecha.moveTo(centros.get(ii-1).x, centros.get(ii-1).y);
                            pathLineaFlecha.lineTo(centros.get(i-1).x+15, centros.get(i-1).y+15);
                            pLineaFlecha.setStrokeWidth(grosorFlecha(numeroMayor, flujo));
                            canvas.drawPath(pathLineaFlecha, pLineaFlecha);
                            //dibujamos punta de flecha
                            puntaFlecha(pathPuntaFlecha, centros.get(ii-1).x, centros.get(ii-1).y, centros.get(i-1).x+15, centros.get(i-1).y+15, grosorPuntaFlecha(numeroMayor, flujo));
                            canvas.drawPath(pathPuntaFlecha, pPuntaFlecha);
                        }
                    }
                    ii=ii+1;
                }
            }
            i = i + 1;
        }
    }*/


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

    /*function canvas_arrow(context, fromx, fromy, tox, toy){
        var headlen = 10;   // length of head in pixels
        var angle = Math.atan2(toy-fromy,tox-fromx);
        context.moveTo(fromx, fromy);
        context.lineTo(tox, toy);
        context.lineTo(tox-headlen*Math.cos(angle-Math.PI/6),toy-headlen*Math.sin(angle-Math.PI/6));
        context.moveTo(tox, toy);
        context.lineTo(tox-headlen*Math.cos(angle+Math.PI/6),toy-headlen*Math.sin(angle+Math.PI/6));
    }*/

}
