package com.app.apptuality.talentum.cubelizer.cubelizer.persistence;

import android.graphics.*;

import android.graphics.Point;



import com.google.gson.annotations.Expose;

import com.google.gson.annotations.SerializedName;



import java.util.ArrayList;

import java.util.List;



/**

 * Created by Astrid on 29/11/2016.

 */


public class Polygon {

    private String name;
    private ArrayList<android.graphics.Point> polygon = new ArrayList<android.graphics.Point>();


    public String getName() {
        return name;
    }

    public void setName(String name) {

        this.name = name;

    }


    public ArrayList<android.graphics.Point> getPolygon() {
        return polygon;
    }

    public void setPolygon(ArrayList<Point> polygon) {
        this.polygon = polygon;

    }

    @Override

    public String toString() {

        return "Polygon{" +

                "name='" + name + '\'' +

                ", polygon=" + polygon +

                '}';

    }

}

