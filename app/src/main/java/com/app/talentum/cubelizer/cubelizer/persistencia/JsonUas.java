package com.app.talentum.cubelizer.cubelizer.persistencia;
import android.graphics.*;
        import android.graphics.Point;

import com.app.talentum.cubelizer.cubelizer.persistencia.Polygon;

import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Iterator;
        import java.util.Map;



public class JsonUas {

    //String json;
    private static String actualKey = null;
    private Polygon polygon = new Polygon();
    private ArrayList<Polygon> polygons = new ArrayList<Polygon>();

    public void devolucionArray(String json) {
        JSONObject object = null;
        try {
            object = new JSONObject(json);

            JSONObject info = object.getJSONObject("result");

            Map<String, String> out = new HashMap<String, String>();

            parse(info, out);

            int jar = info.length();

            for (int i = 1; i <= jar; i++) {

                String name = out.get("name_0" + i);

                String polygon = out.get("polygon_0" + i);

                System.out.println("Name: " + name + " Polygon: " + polygon);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> parse(JSONObject json, Map<String, String> outJ) throws JSONException {

        Iterator<String> keys = json.keys();

        while (keys.hasNext()) {
            String key = keys.next();

            System.out.println("String key =>  " + key);
            // polygon.setName(key);


            if (!key.equalsIgnoreCase("name: ") && !key.equalsIgnoreCase("polygon: ")) {
                actualKey = key;
                System.out.println("String actual_key del if =>  " + actualKey);

            }
            String val = null;
            try {
                JSONObject value = json.getJSONObject(key);
                parse(value, outJ);
                System.out.println("parse =>  " + value+", "+outJ);
            } catch (Exception e) {
                val = json.getString(key);
                System.out.println("val que se obtiene del json =>  " + val);
            }
            if (val != null) {
                //  outJ.put(key + "_" + actualKey, val);
                outJ.put(key, val);
                System.out.println("String put_key key y valor =>  " + key+", "+val);
            }
        }

        System.out.println("IMPRESION DEL MAP parse: "+outJ);

        convertirAPolygon(outJ);
        return outJ;
    }

    public void convertirAPolygon(Map<String, String>mapPolygon){
        String sName = mapPolygon.get("name");
        System.out.println("MetodoConvertir sName: "+sName);
        String sPolygon = mapPolygon.get("polygon");
        System.out.println("MetodoConvertir sPolygon: "+sPolygon);
        int longitud = sPolygon.length();
        System.out.println("Longitud: "+ longitud);

        ArrayList<android.graphics.Point> aPolygon = new ArrayList<android.graphics.Point>();

        String name = sPolygon.substring(1, longitud - 1);
        System.out.println("name subString: "+ name);
        String[] aPunto = name.split("]");

        System.out.println("aPunto: "+ aPunto.toString());
        int i = 0;
        for(String sPunto : aPunto){
            if (i == 0) {
                String sPunto2 = sPunto.substring(1);
                System.out.println("sPunto2: "+ sPunto2);

                String [] aPunto2 = sPunto2.split(",");
                int x = Integer.parseInt(aPunto2[0]);
                int y = Integer.parseInt(aPunto2[1]);
                System.out.println("aPunto2 de 0: "+ aPunto2[0].toString());
                System.out.println("aPunto2 de 1: "+ aPunto2[1].toString());
                android.graphics.Point punto = new android.graphics.Point(x,y);
                System.out.println("Punto: "+ punto);
                aPolygon.add(punto);
                i = i + 1;
                System.out.println("aPolygon: "+ aPolygon);
            } else {
                String sPunto2 = sPunto.substring(2);
                String [] aPunto2 = sPunto2.split(",");

                int x = Integer.parseInt(aPunto2[0]);
                int y = Integer.parseInt(aPunto2[1]);
                System.out.println("else aPunto2 de 0: "+ aPunto2[0].toString());
                System.out.println("else aPunto2 de 1: "+ aPunto2[1].toString());
                android.graphics.Point punto = new Point(x,y);
                System.out.println("else Punto: "+ punto);
                aPolygon.add(punto);
            }
        }
        polygon.setName(sName);
        System.out.println("Poligono final: "+ polygon);
        polygon.setPolygon(aPolygon);
        polygons.add(polygon);
        System.out.println("Polygons: "+ polygons.toString());

    }

    public ArrayList<Polygon> getPolygons() {
        return polygons;
    }

    public void setPolygons(ArrayList<Polygon> polygons) {
        this.polygons = polygons;
    }



}
