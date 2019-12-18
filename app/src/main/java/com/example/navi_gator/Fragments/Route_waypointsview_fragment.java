package com.example.navi_gator.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.navi_gator.Interface.OnFragmentInteractionListener;
import com.example.navi_gator.Logic.DatabaseManager;
import com.example.navi_gator.Models.API.Route;
import com.example.navi_gator.Models.API.Waypoint;
import com.example.navi_gator.Models.API.WaypointAdapter;
import com.example.navi_gator.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Route_waypointsview_fragment extends Fragment {

    private DatabaseManager databaseManager;
    private Button back_btn;
    private OnFragmentInteractionListener mListener;
    private Route route;

    public Route_waypointsview_fragment() {
        // Required empty public constructor
    }

    public static Route_waypointsview_fragment newInstance(Route route) {
        Route_waypointsview_fragment fragment = new Route_waypointsview_fragment();
        Bundle bundle =  new Bundle();
        bundle.putSerializable("route",route);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseManager = new DatabaseManager(this.getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.route = (Route)getArguments().getSerializable("route");
        View view = inflater.inflate(R.layout.route_waypointsview, container, false);
        back_btn = view.findViewById(R.id.waypoints_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBack();
            }
        });
        initRecycleviewer(view, (ArrayList<Waypoint>) route.getRouteWaypoints());
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
    private void initRecycleviewer(View view, ArrayList<Waypoint> waypoints) {
        RecyclerView recyclerView = view.findViewById(R.id.Rview);
        waypoints.add(new Waypoint(1, new LatLng(200.0, 200.0), "kip", "ik hou van kip"));
        WaypointAdapter adapter = new WaypointAdapter(waypoints, this.getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }


}
