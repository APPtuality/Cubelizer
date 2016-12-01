package com.app.talentum.cubelizer.cubelizer.persistencia;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class Result {

    @SerializedName("map")
    @Expose
    private String map;


    @SerializedName("activity_map")
    @Expose
    private String activityMap;
    @SerializedName("background")
    @Expose
    private String background;
    @SerializedName("flow_mag_map")
    @Expose
    private String flowMagMap;
    @SerializedName("flow_angle_map")
    @Expose
    private String flowAngleMap;



    @SerializedName("UAs_flow")
    @Expose
    private String uAsFlow;
    //private List<List<Integer>> uAsFlow = new ArrayList<List<Integer>>();


    @SerializedName("01")
    @Expose
    private Polygon polygon;

    public String getMap() {
        if (map.isEmpty()){
            map = "nullMap";
        }
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getActivityMap() {
        if (activityMap == ""){
            activityMap = "nullActivityMap";
        }
        return activityMap;
    }

    public void setActivityMap(String activityMap) {
        this.activityMap = activityMap;
    }

    public String getBackground() {
        if (background == ""){
            background = "nullBackground";
        }
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getFlowMagMap() {
        if (flowMagMap == ""){
            flowMagMap = "nullFlowMagMap";
        }
        return flowMagMap;
    }

    public void setFlowMagMap(String flowMagMap) {
        this.flowMagMap = flowMagMap;
    }

    public String getFlowAngleMap() {
        if (flowAngleMap == ""){
            flowAngleMap = "nullFlowAngleMap";
        }
        return flowAngleMap;
    }

    public void setFlowAngleMap(String flowAngleMap) {
        this.flowAngleMap = flowAngleMap;
    }

    public String getuAsFlow() {
        return uAsFlow;
    }

    public void setuAsFlow(String uAsFlow) {
        this.uAsFlow = uAsFlow;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    @Override
    public String toString() {
        return "Result{" +
                "map='" + map + '\'' +
                ", activityMap='" + activityMap + '\'' +
                ", background='" + background + '\'' +
                ", flowMagMap='" + flowMagMap + '\'' +
                ", flowAngleMap='" + flowAngleMap + '\'' +
                ", uAsFlow=" + uAsFlow +
                ", polygon=" + polygon +
                '}';
    }
}
