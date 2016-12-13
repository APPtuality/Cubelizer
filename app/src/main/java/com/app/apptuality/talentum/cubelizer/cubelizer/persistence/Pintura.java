package com.app.apptuality.talentum.cubelizer.cubelizer.persistence;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by Daboom on 12/12/2016.
 */

public class Pintura {

    public Pintura() {
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
