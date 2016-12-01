package com.app.talentum.cubelizer.cubelizer.persistencia;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;



public class Polygon {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("polygon")
    @Expose
    private List<Point> polygon = new ArrayList<Point>();

        /**
         *
         * @return
         * The name
         */
        public String getName() {
            return name;
        }

        /**
         *
         * @param name
         * The name
         */
        public void setName(String name) {
            this.name = name;
        }

    public List<Point> getPolygon() {
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
