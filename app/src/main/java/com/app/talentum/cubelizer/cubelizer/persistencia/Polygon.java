package com.app.talentum.cubelizer.cubelizer.persistencia;

import android.graphics.Point;


import java.util.ArrayList;
import java.util.List;



public class Polygon {

    private String name;
    private List<android.graphics.Point> polygon = new ArrayList<android.graphics.Point>();


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public List<android.graphics.Point> getPolygon() {
        return polygon;
    }

    public void setPolygon(List<Point> polygon) {
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