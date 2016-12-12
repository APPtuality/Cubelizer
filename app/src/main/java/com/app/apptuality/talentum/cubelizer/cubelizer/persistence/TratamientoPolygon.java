package com.app.apptuality.talentum.cubelizer.cubelizer.persistence;

import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by Adri on 01/12/2016.
 */

public class TratamientoPolygon {

    private ArrayList<Polygon> polygons = new ArrayList<Polygon>();
    //String sector = "Name: Entrance Polygon: [[0,0],[559,0],[559,41],[346,188],[209,198],[97,203],[0,145]]";
    //I/System.out: Name: Left Polygon: [[0,145],[97,203],[209,198],[224,332],[234,434],[0,461]]
    //I/System.out: Name: Upper-right Polygon: [[209,198],[346,188],[559,41],[559,237],[378,304],[224,332]]
    //I/System.out: Name: Lower-right  Polygon: [[224,332],[378,304],[559,237],[559,464],[309,426],[234,434]]
    //I/System.out: Name: Chairs Polygon: [[0,461],[234,434],[309,426],[559,464],[559,559],[0,559]]


    public void convertirAPolygon(String sector) {
        Polygon polygon = new Polygon();
        int iPolygon = sector.indexOf("Polygon");
        String name = sector.substring(6, iPolygon - 1);
        polygon.setName(name);
        int longitud = sector.length();

        String sPolygon = sector.substring(iPolygon+10,longitud-1);
        ArrayList<Point> aPolygon = new ArrayList<Point>();
        String[] aPunto = sPolygon.split("]");
        int i = 0;
        for(String sPunto : aPunto){
            if (i == 0) {
                String sPunto2 = sPunto.substring(1);
                String[] aPunto2 = sPunto2.split(",");
                int x = Integer.parseInt(aPunto2[0]);
                int y = Integer.parseInt(aPunto2[1]);
                Point punto = new Point(x,y);
                aPolygon.add(punto);
                i = i + 1;
                //System.out.println(i);
            } else {
                String sPunto2 = sPunto.substring(2);
                String[] aPunto2 = sPunto2.split(",");
                int x = Integer.parseInt(aPunto2[0]);
                int y = Integer.parseInt(aPunto2[1]);
                Point punto = new Point(x,y);
                aPolygon.add(punto);
            }
        }
        polygon.setPolygon(aPolygon);
        polygons.add(polygon);
    }

    public ArrayList<Polygon> getPolygons() {
        return polygons;
    }

    public void setPolygons(ArrayList<Polygon> polygons) {
        this.polygons = polygons;
    }

}
