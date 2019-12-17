package com.example.navi_gator.Activity;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.example.navi_gator.Logic.RouteManager;
import com.example.navi_gator.Models.API.Waypoint;
import com.example.navi_gator.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    /**
     * De map activity is waar de gebruiker de meeste tijd zal besteden.
     * Hier is een kaart te zien met de route die is ingeladen.
     * Op de activity staat de navigationFragment en routeController.
     */

    private GoogleMap mMap;
    private RouteManager controller;

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

        this.controller = new RouteManager(this, this, mMap, getResources().openRawResource(R.raw.historischekm));
        controller.prepareLocationService();
        // Add a marker in Breda and move the camera

        LatLng breda = new LatLng(51.571915, 4.768323);
        mMap.addMarker(new MarkerOptions().position(breda).title("Marker in breda"));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Waypoint point = controller.getWaypointInRouteFromLatLng(marker.getPosition());
                point.setVisited(!point.isVisited());

                if(point.isVisited()){
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.checked_marker));
                } else {
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.waypoint_marker));
                }

                return false;
            }
        });
    }



}
