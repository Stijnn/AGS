package com.example.navi_gator.Models.API;
import java.io.Serializable;
import java.util.List;

import java.util.ArrayList;

public class Route implements Serializable {
    public static final String TABLE_NAME = "tbl_Routes";

    private String id, name, description;
    private boolean finished;

    // Current route setup
    private List<Waypoint> routeWaypoints;

    public Route(String id, String name, String description, boolean finished, List<Waypoint> waypoints) {
        this(id,name,description,finished);
        this.routeWaypoints = waypoints;
    }

    // DataBaseManager route setup
    public Route(String id, String name, String description, boolean finished) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.finished = finished;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public List<Waypoint> getRouteWaypoints() {
        return routeWaypoints;
    }

    public void setRouteWaypoints(List<Waypoint> routeWaypoints) {
        this.routeWaypoints = routeWaypoints;
    }

    @Override
    public String toString() {
        return "Route{" +
                "routeWaypoints=" + routeWaypoints +
                '}';
    }
}


