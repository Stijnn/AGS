package com.example.navi_gator.Logic;

import android.os.FileUtils;
import android.util.Log;

import com.example.navi_gator.Models.API.Route;
import com.example.navi_gator.Models.API.Waypoint;
import com.google.android.gms.common.util.IOUtils;
import com.google.android.gms.common.util.JsonUtils;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RouteReader {

    private Route route;

    public RouteReader(InputStream is){
        try {
            JSONObject json = new JSONObject(Objects.requireNonNull(inputStreamToJsonString(is)));
            this.route = createRouteFromJson(json);
            Log.d("JSON", json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private double convertStringToIntGMS(String gmsCoord, String type) {
        float gmsDegrees = Float.parseFloat(gmsCoord.substring(0, gmsCoord.indexOf("°")));
        float gmsMinutes = Float.parseFloat(gmsCoord.substring(gmsCoord.indexOf("°") + 1, gmsCoord.indexOf("’")));

        float result = gmsDegrees + (gmsMinutes / 60);

        Log.d("GMS CONVERTER",  "Result, " + type + ": " + result);
        return result;
    }

    public LatLng convertGMSCoords(String gmsLat, String gmsLong) {
        return new LatLng(convertStringToIntGMS(gmsLat, "Lat"), convertStringToIntGMS(gmsLong, "Long"));
    }

    private String inputStreamToJsonString(InputStream is) {
        String json = null;
        try {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public Route createRouteFromJson(JSONObject object) {
        Route route = null;
        List<Waypoint> waypoints = new ArrayList<>();

        try {
            JSONArray routeArray = object.getJSONArray("waypoint");

            for (int i = 0; i < routeArray.length(); i++) {
                JSONObject currentObject = routeArray.getJSONObject(i);
                        waypoints.add(new Waypoint(
                                currentObject.getInt("number"),
                                convertGMSCoords(
                                        currentObject.getString("latitude"),
                                        currentObject.getString("longitude")),
                                currentObject.getString("name"),
                                currentObject.getString("comment")
                        ));
            }
            route = new Route(waypoints);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return route;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}


