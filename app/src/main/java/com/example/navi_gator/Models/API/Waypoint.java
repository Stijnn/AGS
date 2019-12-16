package com.example.navi_gator.Models.API;

import android.location.Location;

public class Waypoint {
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

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
