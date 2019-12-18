package com.example.navi_gator.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.navi_gator.Interface.OnFragmentInteractionListener;
import com.example.navi_gator.Models.API.Route;
import com.example.navi_gator.Models.API.Waypoint;
import com.example.navi_gator.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class Burger_fragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button back_btn;
    private Button setting_btn;
    private Button allWaypointsview;
    private Button routeWaypointsview;

    public Burger_fragment() {
        // Required empty public constructor
    }


    public static Burger_fragment newInstance() {
        return new Burger_fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.burger_fragment, container, false);
        this.back_btn = view.findViewById(R.id.burger_back_btn);
        this.setting_btn = view.findViewById(R.id.setting_btn);
        this.allWaypointsview = view.findViewById(R.id.waypoints_btn);
        this.routeWaypointsview = view.findViewById(R.id.route_btn);

        routeWaypointsview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Waypoint> waypoints = new ArrayList<>();
                waypoints.add(new Waypoint(false,"", 1, new LatLng(200.0, 200.0), "", ""));
                openRouteWaypointsFragment(new Route("", "" ,"", false, waypoints));
            }
        });

        allWaypointsview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAllWaypointsFragment();
            }
        });

        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingFragment();
            }
        });

        this.back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBack();
            }
        });
        return view;
    }

    public void sendBack() {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void openSettingFragment() {
        Setting_fragment fragment = Setting_fragment.newInstance();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.addToBackStack(null);
        transaction.replace(R.id.dummy_container, fragment, "setting_fragment").commit();
    }

    public void openAllWaypointsFragment() {
        All_Waypointsview_fragment fragment = All_Waypointsview_fragment.newInstance();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.addToBackStack(null);
        transaction.replace(R.id.help_fragment_container, fragment, "All_Waypointsview_fragment").commit();
    }

    public void openRouteWaypointsFragment(Route route) {
        Route_waypointsview_fragment fragment = Route_waypointsview_fragment.newInstance(route);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.addToBackStack(null);
        transaction.replace(R.id.help_fragment_container, fragment, "Route_Waypointsview_fragment").commit();
    }
}
