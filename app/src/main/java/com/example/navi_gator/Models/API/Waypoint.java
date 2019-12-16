package com.example.navi_gator.Models.API;

import android.icu.text.Transliterator;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Waypoint implements Serializable {
    public static final String TABLE_NAME = "tbl_Waypoints";

    private boolean visited;
    private String id, description, image;
    private double lon, lat;

    public Waypoint(boolean visited, String id, String description, String image, double lon, double lat) {
        this.visited = visited;
        this.id = id;
        this.description = description;
        this.image = image;
        this.lon = lon;
        this.lat = lat;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    public LatLng getPosition(){
        return new LatLng(this.lat,this.lon);
    }
}
