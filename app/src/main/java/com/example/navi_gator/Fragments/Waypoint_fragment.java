package com.example.navi_gator.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.navi_gator.Interface.OnFragmentInteractionListener;
import com.example.navi_gator.Models.API.Waypoint;
import com.example.navi_gator.R;
import com.google.android.gms.maps.model.Marker;


public class Waypoint_fragment extends Fragment {


    private Waypoint mwaypoint;

    private OnFragmentInteractionListener mListener;

    private TextView title;
    private TextView details;
    private ImageView image;

    public Waypoint_fragment() {
        // Required empty public constructor
    }

    public static Waypoint_fragment newInstance(Waypoint waypoint) {
        Waypoint_fragment fragment = new Waypoint_fragment();
        Bundle args = new Bundle();
        args.putSerializable("waypoint", waypoint);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mwaypoint = (Waypoint)getArguments().getSerializable("waypoint");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.waypoint_fragment, container, false);
        this.title = view.findViewById(R.id.waypoint_title);
        this.details = view.findViewById(R.id.waypoint_details);
        this.image = view.findViewById(R.id.waypoint_image);

        this.title.setText(mwaypoint.getName());
        this.details.setText(mwaypoint.getComment());
        //TODO image
        //this.image.setImageResource(R.id.);

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
}
