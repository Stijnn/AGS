package com.example.navi_gator.Models.API;

import com.google.android.gms.maps.model.LatLng;

public class Waypoint {

    private int number;
    private LatLng latlong;
    private String name;
    private String comment;

    public static final String TABLE_NAME = "tbl_Waypoints";
    private boolean visited;
    private String id, description, image;

    private double lon, lat;

    // Current Setup for the RouteManager
    public Waypoint(int number, LatLng latlong, String name, String comment) {
        this.number = number;
        this.latlong = latlong;
        this.name = name;
        this.comment = comment;
    }

    // Setup for the DatabaseManager
    public Waypoint(boolean visited, String id, String description, String image) {
        this.visited = visited;
        this.id = id;
        this.description = description;
        this.image = image;
        this.lon = getLatlong().latitude;
        this.lat = getLatlong().longitude;
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
