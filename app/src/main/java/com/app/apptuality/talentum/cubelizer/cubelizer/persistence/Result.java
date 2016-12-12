package com.app.apptuality.talentum.cubelizer.cubelizer.persistence;

import android.util.Log;

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


    public String getMap() {
        if (map.isEmpty() || map == ""){
            map = "Test";
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
        if (uAsFlow == ""){
            uAsFlow = "nullFlowUAs";
        }
        return uAsFlow;
    }

    public void setuAsFlow(String uAsFlow) {
        this.uAsFlow = uAsFlow;
    }

    @Override
    public String toString() {
        return "Result{" +
                "map='" + map + '\'' +
                ", activityMap='" + activityMap + '\'' +
                ", background='" + background + '\'' +
                ", flowMagMap='" + flowMagMap + '\'' +
                ", flowAngleMap='" + flowAngleMap + '\'' +
                ", uAsFlow='" + uAsFlow + '\'' +
                '}';
    }
}
