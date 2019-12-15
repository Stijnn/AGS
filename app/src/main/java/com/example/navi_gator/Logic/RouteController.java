package com.example.navi_gator.Logic;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.navi_gator.Models.GPS.GPSManager;
import com.example.navi_gator.Models.GPS.IUserNavigatorUpdater;
import com.example.navi_gator.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.example.navi_gator.Models.GPS.GPSManager.PERMISSION_STRING;

public class RouteController {

    /**
     * De route controller laat de gebruiker de route een beetje besturen.
     * De gebruiker kan de route hiermee bijvoorbeeld even pauzeren.
     * Of als de gebruiker het echt wilt zelfs zeggen dat hij niet naar een bepaalde waypoint wilt.
     *
     * Dit zal weergegeven worden op de mapactivity.
     */

    private LocationManager locationManager;
    private LocationListener locationListener;

    private Context context;

    private GPSManager gpsManager;

    private Activity mapActivity;

    public RouteController (Context context, IUserNavigatorUpdater listener, Activity mapActivity) {
        gpsManager = new GPSManager(context, listener);
        this.context = context;
        this.mapActivity = mapActivity;
    }

    public GPSManager getGpsManager() {
        return gpsManager;
    }

    public void setGpsManager(GPSManager gpsManager) {
        this.gpsManager = gpsManager;
    }

    public void prepareAndUpdateUserNavigatorPosition(Marker userNavigator, GoogleMap mMap, Location location) {
        try {

            userNavigator.setPosition(new LatLng(location.getLatitude(),location.getLongitude()));

        } catch (NullPointerException ex) {
            // this gets called after the user clicks the cancel button or the marker isn't initialized yet, to make sure the system doesn't crash
            // this also makes sure to update the location if the GPS is disabled mid-way and re-enabled.

            // creates the userNavigator marker, to show the current position on the map.
            MarkerOptions position = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .title("UserNavigator")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.user_navigator_marker));
            userNavigator = mMap.addMarker(position);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            mMap.setMinZoomPreference(10);

            //TODO make notification
        }
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
                5000,
                10, locationListener);
    }
}
