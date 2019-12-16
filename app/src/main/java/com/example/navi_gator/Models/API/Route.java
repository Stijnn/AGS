package com.example.navi_gator.Models.API;

import com.example.navi_gator.Logic.RouteReader;
import com.example.navi_gator.R;

import java.util.ArrayList;
import java.util.List;

public class Route {
    private List<Waypoint> routeWaypoints;

    public Route(List<Waypoint> waypoints) {
        this.routeWaypoints = waypoints;
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


