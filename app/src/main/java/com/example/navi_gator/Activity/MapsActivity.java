package com.example.navi_gator.Activity;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.navi_gator.Fragments.Burger_fragment;
import com.example.navi_gator.Fragments.Help_fragment;
import com.example.navi_gator.Fragments.Loading_fragment;
import com.example.navi_gator.Fragments.Waypoint_fragment;
import com.example.navi_gator.Interface.OnFragmentInteractionListener;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnFragmentInteractionListener {

    /**
     * De map activity is waar de gebruiker de meeste tijd zal besteden.
     * Hier is een kaart te zien met de route die is ingeladen.
     * Op de activity staat de navigationFragment en routeController.
     */

    private GoogleMap mMap;
    private Button help_btn;
    private Button burger_btn;
    private FrameLayout helpLayout;
    private FrameLayout burgerLayout;
    private FrameLayout dummyLayout;
    private FrameLayout wayPointLayout;
    private RouteManager controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        help_btn = findViewById(R.id.help_btn);
        burger_btn = findViewById(R.id.burgermenu_btn);
        helpLayout = findViewById(R.id.help_fragment_container);
        burgerLayout = findViewById(R.id.burger_fragment_container);
        dummyLayout = findViewById(R.id.dummy_container);
        wayPointLayout = findViewById(R.id.wayPointLayout);

        help_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //openHelpFragment();
                //openWaypointFragment(new Waypoint(5,new LatLng(200.0,200.0),"kip","ik hou van kip"));
            }
        });



        burger_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBurgerFragment();
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void openHelpFragment() {
        Help_fragment fragment = Help_fragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_left);
        transaction.addToBackStack(null);
        transaction.replace(R.id.map_container, fragment, "help_fragment").commit();
    }

    public void openBurgerFragment() {
        Burger_fragment fragment = Burger_fragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right);
        transaction.addToBackStack(null);
        transaction.replace(R.id.dummy_container, fragment, "burger_fragment").commit();
    }

    public void openLoadingFragment() {
        Loading_fragment fragment = Loading_fragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom, R.anim.enter_from_bottom, R.anim.exit_to_bottom);
        transaction.addToBackStack(null);
        transaction.replace(R.id.map_container, fragment, "loading_fragment").commit();
    }

    public void openWaypointFragment(Waypoint waypoint) {
        Waypoint_fragment fragment = Waypoint_fragment.newInstance(waypoint);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_bottom, R.anim.exit_to_bottom, R.anim.enter_from_bottom, R.anim.exit_to_bottom);
        transaction.addToBackStack(null);
        transaction.replace(R.id.dummy_container, fragment, "waypoint_fragment").commit();
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
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
        // Add a marker in Sydney and move the camera

        //LatLng breda = new LatLng(51.571915, 4.768323);
        //mMap.addMarker(new MarkerOptions().position(breda).title("Marker in breda"));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Waypoint point = controller.getWaypointInRouteFromLatLngAndNextWaypointInt(marker.getPosition(), controller.nextWaypoint);

                if (point.getNumber() == controller.nextWaypoint) {

                    controller.nextWaypoint++;
                    point.setVisited(true);

                    if (point.isVisited()) {
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.checked_marker));
                    }

                    controller.updateNextMarker();

                }
                return false;
            }
        });
    }

    @Override
    public void onFragmentInteraction() {
        onBackPressed();
    }
}
