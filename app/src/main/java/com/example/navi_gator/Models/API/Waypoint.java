package com.example.navi_gator.Models.API;

import com.example.navi_gator.Models.Media.Media;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Waypoint {

    private int number;
    private LatLng latlong;
    private String name;
    private String comment;
    private boolean visited;
    private ArrayList<Media> mediaFiles;

    public static final String TABLE_NAME = "tbl_Waypoints";
    private String id, description, image;

    private double lon, lat;

    // Current Setup for the RouteManager
    public Waypoint(int number, LatLng latlong, String name, String comment) {
        this.number = number;
        this.latlong = latlong;
        this.name = name;
        this.comment = comment;
        this.visited = false;
        this.mediaFiles = new ArrayList<>();
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

    public void addMediaFile(Media media){
        this.mediaFiles.add(media);
    }

    public ArrayList<Media> getMediaFiles(){
        return this.mediaFiles;
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
