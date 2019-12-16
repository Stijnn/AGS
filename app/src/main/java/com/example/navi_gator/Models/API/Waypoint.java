package com.example.navi_gator.Models.API;

import com.google.android.gms.maps.model.LatLng;

public class Waypoint {

    private int number;
    private LatLng latlong;
    private String name;
    private String comment;

    public Waypoint(int number, LatLng latlong, String name, String comment) {
        this.number = number;
        this.latlong = latlong;
        this.name = name;
        this.comment = comment;
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
