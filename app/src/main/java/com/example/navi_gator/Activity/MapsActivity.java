package com.example.navi_gator.Activity;

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
import com.example.navi_gator.Interface.OnFragmentInteractionListener;
import com.example.navi_gator.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback , OnFragmentInteractionListener {

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        help_btn = findViewById(R.id.help_btn);
        burger_btn = findViewById(R.id.burgermenu_btn);
        helpLayout = findViewById(R.id.help_fragment_container);
        burgerLayout = findViewById(R.id.burger_fragment_container);
        dummyLayout = findViewById(R.id.dummy_container);

        help_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               openHelpFragment();
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

    public void openHelpFragment(){
        Help_fragment fragment = Help_fragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_left);
        transaction.addToBackStack(null);
        transaction.replace(R.id.map_container,fragment,"help_fragment").commit();
    }

    public void openBurgerFragment(){
        Burger_fragment fragment = Burger_fragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.exit_to_right,R.anim.enter_from_right,R.anim.exit_to_right,R.anim.enter_from_right);
        transaction.addToBackStack(null);
        transaction.replace(R.id.dummy_container,fragment,"burger_fragment").commit();
    }

    public void openLoadingFragment(){
        Loading_fragment fragment = Loading_fragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_bottom,R.anim.exit_to_bottom,R.anim.enter_from_bottom,R.anim.exit_to_bottom);
        transaction.addToBackStack(null);
        transaction.replace(R.id.map_container,fragment,"loading_fragment").commit();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        LatLng breda = new LatLng(51.571915, 4.768323);
        mMap.addMarker(new MarkerOptions().position(breda).title("Marker in breda"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(breda));
    }

    @Override
    public void onFragmentInteraction() {
       onBackPressed();
    }
}
