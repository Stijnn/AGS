package com.example.navi_gator.Activity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.example.navi_gator.GPSManager;
import com.example.navi_gator.Models.GPS.IUserNavigatorUpdater;
import com.example.navi_gator.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.example.navi_gator.GPSManager.PERMISSION_STRING;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, IUserNavigatorUpdater {

    /**
     * De map activity is waar de gebruiker de meeste tijd zal besteden.
     * Hier is een kaart te zien met de route die is ingeladen.
     * Op de activity staat de navigationFragment en routeController.
     */

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Marker userNavigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        prepareLocationService();

        // Add a marker in Breda and move the camera

        LatLng breda = new LatLng(51.571915, 4.768323);
        mMap.addMarker(new MarkerOptions().position(breda).title("Marker in breda"));
    }

    public void prepareLocationService() {
        GPSManager gpsManager = new GPSManager(this, this);
        gpsManager.setupLocationServices(locationListener, locationManager);
        gpsManager.isLocationEnabled();
        locationListener = gpsManager.getLocationListener();

        // initializes the location services and checks if permission is given.
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, PERMISSION_STRING) != PackageManager.PERMISSION_GRANTED ) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 123);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                2000,
                10, locationListener);

        // creates the userNavigator marker, to show the current position on the map.
        Location startLoc = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (startLoc != null) {
            MarkerOptions position = new MarkerOptions().position(new LatLng(startLoc.getLatitude(), startLoc.getLongitude()))
                    .title("UserNavigator")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.user_navigator_marker));
            this.userNavigator = this.mMap.addMarker(position);
        }
    }

    @Override
    public void updateUserNavigatorLocation(Location location) {
        this.userNavigator.setPosition(new LatLng(location.getLatitude(),location.getLongitude()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude())));
    }
}
