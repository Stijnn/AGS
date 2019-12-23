package com.example.navi_gator.Models.API;

import com.example.navi_gator.Models.Media.Media;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

public class Waypoint implements Serializable {
    private int number;
    private LatLng latlong;
    private String name;
    private String comment;
    private boolean visited;

    public static final String TABLE_NAME = "tbl_Waypoints";
    private String id, description, image;

    private double lon, lat;

    // Current Setup for the RouteManager
    public Waypoint(boolean visited, String id, int number, LatLng latlong, String name, String comment) {
        this.number = number;
        this.latlong = latlong;
        this.id = id;
        this.name = name;
        this.comment = comment;
        this.description = comment;
        this.visited = visited;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public LatLng getLatlong() {
        return latlong;
    }

    public void setLatlong(LatLng latlong) {
        this.latlong = latlong;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "Waypoint{" +
                "number=" + number +
                ", latlong=" + latlong +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
