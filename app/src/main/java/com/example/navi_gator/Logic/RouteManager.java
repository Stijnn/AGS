package com.example.navi_gator.Logic;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.navi_gator.Models.API.Route;
import com.example.navi_gator.Models.API.Waypoint;
import com.example.navi_gator.Models.GPS.GPSManager;
import com.example.navi_gator.Models.GPS.IUserNavigatorUpdater;
import com.example.navi_gator.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import static com.example.navi_gator.Models.GPS.GPSManager.PERMISSION_STRING;

public class RouteManager implements IUserNavigatorUpdater, IRouteLeavingCallback {

    /**
     * De route controller laat de gebruiker de route een beetje besturen.
     * De gebruiker kan de route hiermee bijvoorbeeld even pauzeren.
     * Of als de gebruiker het echt wilt zelfs zeggen dat hij niet naar een bepaalde waypoint wilt.
     *
     * Dit zal weergegeven worden op de mapactivity.
     */

    // GPS attributes
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Marker userNavigator;
    private Context context;
    private GPSManager gpsManager;
    private Activity mapActivity;
    private GoogleMap mMap;
    // GPS constant attributes
    private final int UPDATE_TIME_INTERVAL = 500;
    private final int UPDATE_MIN_DISTANCE = 0; // set to 0 to only update when time UPDATE_TIME_INTERVAL has passed

    // RouteManager attributes
    private Route route;
    private RouteReader routeReader;
    private InputStream routeStream;

    // DirectionsAPI
    private List<List<Waypoint>> divideWaypoints;
    private DirectionsAPI directionsAPI;
    private boolean onRouteCreationSuccess = true;

    private LatLng currentGPSPos;

    // Route progression
    public int nextWaypoint = 1;
    private HashMap<Waypoint, Marker> markers;

    public RouteManager(Context context, Activity mapActivity, GoogleMap mMap, InputStream route) {
        this.context = context;
        this.mapActivity = mapActivity;
        this.mMap = mMap;
        this.routeStream = route;
        this.markers = new HashMap<>();

        try {
            routeReader = new RouteReader(this.routeStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.route = new DatabaseManager(context).getRoute("DEBUG");

        gpsManager = new GPSManager(context, this);

        setupDirectionsAPI();
    }

    public GPSManager getGpsManager() {
        return gpsManager;
    }

    public void setGpsManager(GPSManager gpsManager) {
        this.gpsManager = gpsManager;
    }

    public HashMap<Waypoint, Marker> getMarkers() {
        return markers;
    }

    public String generateRouteStartURL() {
        String output = "";
        List<Waypoint> waypoints = this.divideWaypoints.get(0);
        output = this.directionsAPI.getDirectionsUrl(
                waypoints.get(0).getLatlong(),
                waypoints.get(waypoints.size() - 1).getLatlong(),
                this.directionsAPI.generateExtraWaypointsStringFromList(waypoints));
        Log.wtf("TESTING GEN ALL URL", "Testing item: String output\n" + output + "\n" +
                "With the route starting with waypoint number: 1" +
               ", And ending with: " + waypoints.get(waypoints.size() - 1).getNumber() + "\n" + "---------------------------------------------------------------");
        return output;
    }

    public String generateRouteRestURL(int segment) {
        String output = "";
        List<Waypoint> waypointsList = this.divideWaypoints.get(segment);
        Waypoint lastWaypoint = this.divideWaypoints.get(segment - 1).get(this.divideWaypoints.get(segment - 1).size() - 1);

        output = this.directionsAPI.getDirectionsUrl(
                lastWaypoint.getLatlong(),
                waypointsList.get(waypointsList.size() - 1).getLatlong(),
                this.directionsAPI.generateExtraWaypointsStringFromList(waypointsList));
        Log.wtf("TESTING GEN ALL URL", "Testing item: String output\n" + output  + "\n" +
                "With the route starting with waypoint number: " + lastWaypoint.getNumber() +
                ", And ending with: " + waypointsList.get(waypointsList.size() - 1).getNumber() + "\n" + "---------------------------------------------------------------");

        return output;
    }

    public void TestRouteStringGeneration() {
        for (int i = 0; i < this.divideWaypoints.size(); i++) {
            if (i == 0) {
                generateRouteStartURL();
            } else {
                generateRouteRestURL(i);
            }
        }
    }

    public void createRoutePolyLinesOnMap() {
        for (int i = 0; i < this.divideWaypoints.size(); i++) {
            if (i == 0) {
                String routeStartURL = generateRouteStartURL();

            } else {
                String routeRestURL = generateRouteRestURL(i);
            }
        }
    }

    private List<List<Waypoint>> divideWaypoints() {
        List<List<Waypoint>> waypointListDivided = new ArrayList<>();

        boolean newListRequired;
        List<Waypoint> waypointsOfRoute = this.route.getRouteWaypoints();
        List<Waypoint> dividedList = new ArrayList<>();

        for (int i = 1; i <= this.route.getRouteWaypoints().size(); i++) {
            newListRequired = i % 9 == 1 && i != 1;
            if (newListRequired) {
                waypointListDivided.add(dividedList);
                dividedList = new ArrayList<>();
            }
            dividedList.add(waypointsOfRoute.get(i-1));
        }
        waypointListDivided.add(dividedList);
        return waypointListDivided;
    }

    private void initializeRouteWaypoints() {
        try {
            for (Waypoint routeWaypoint : this.route.getRouteWaypoints()) {
                Marker marker;
                MarkerOptions routeWaypointMarker;

                if (routeWaypoint.getNumber() == nextWaypoint) {
                    routeWaypointMarker = new MarkerOptions().position(routeWaypoint.getLatlong())
                            .title(routeWaypoint.getNumber() + ". " + routeWaypoint.getName())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.next_marker))
                            .visible(!routeWaypoint.isVisited());
                    marker = this.mMap.addMarker(routeWaypointMarker);

                } else {
                    routeWaypointMarker = new MarkerOptions().position(routeWaypoint.getLatlong())
                            .title(routeWaypoint.getNumber() + ". " + routeWaypoint.getName())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.waypoint_marker))
                            .visible(!routeWaypoint.isVisited());
                    marker = this.mMap.addMarker(routeWaypointMarker);
                }
                this.markers.put(routeWaypoint, marker);
            }

        } catch (NullPointerException ex) {
            Log.e("CreateRouteWayPoints",ex.getLocalizedMessage() + ": Error Occurred");
            onRouteCreationSuccess = false;
        }
    }

    public void updateNextMarker() {
        Waypoint nextWaypoint = getNextWaypoint();
        if (nextWaypoint != null) markers.get(nextWaypoint).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.next_marker));
    }

    public Waypoint getNextWaypoint(){
        for (Waypoint point : route.getRouteWaypoints()){
            if (point.getNumber() == nextWaypoint) return point;
        }
        return null;
    }

    public void prepareLocationService() {
        getGpsManager().setupLocationServices(locationListener, locationManager);
        getGpsManager().isLocationEnabled();
        locationListener = getGpsManager().getLocationListener();

        // initializes the location services and checks if permission is given.
        locationManager = (LocationManager) mapActivity.getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(context, PERMISSION_STRING) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            ActivityCompat.requestPermissions(mapActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                UPDATE_TIME_INTERVAL,
                UPDATE_MIN_DISTANCE, locationListener);
    }

    public void setupDirectionsAPI () {
        this.directionsAPI = new DirectionsAPI(this.route, this.mMap, this, onRouteCreationSuccess); // For now left empty
    }

    @Override
    public void updateUserNavigatorLocation(Location location) {
        try {

            userNavigator.setPosition(new LatLng(location.getLatitude(),location.getLongitude()));
            this.currentGPSPos = new LatLng(location.getLatitude(), location.getLongitude());
            directionsAPI.drawRoutePolyLine(location);
//            initializeRouteWaypoints();

        } catch (NullPointerException ex) {
            // this gets called after the user clicks the cancel button or the marker isn't initialized yet, to make sure the system doesn't crash
            // this also makes sure to update the location if the GPS is disabled mid-way and re-enabled.

            mMap.clear();
            this.directionsAPI.drawPolyLinesOnMap();
            initializeRouteWaypoints();

            // creates the userNavigator marker, to show the current position on the map.
            MarkerOptions position = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .title("UserNavigator")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.user_navigator_marker));
            userNavigator = mMap.addMarker(position);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            mMap.setMinZoomPreference(10);

            this.currentGPSPos = new LatLng(location.getLatitude(), location.getLongitude());

            directionsAPI.drawRoutePolyLine(location);

            //TODO make notification
        }
    }

    public Waypoint getWaypointInRouteFromLatLngAndNextWaypointInt(LatLng markerPos, int nextWaypoint) {
        Vector<Waypoint> waypoints = new Vector<>();
        for(Waypoint point : route.getRouteWaypoints()){
            if (point.getLatlong().latitude == markerPos.latitude && point.getLatlong().longitude == markerPos.longitude){
                waypoints.add(point);
        }
        }
        try {
            if (waypoints.size() == 1) {
                return waypoints.get(0);
            } else if (waypoints.size() > 1) {
                for (Waypoint point : waypoints) {
                    if (point.getNumber() < nextWaypoint) waypoints.remove(point);
                }
                return waypoints.get(0);
            }
        } catch (Exception e){
            return waypoints.get(0);
        }
        return null;
    }

    @Override
    public void onRouteLeave(String status) {
        Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
    }
}
