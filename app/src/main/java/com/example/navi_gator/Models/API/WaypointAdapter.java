package com.example.navi_gator.Models.API;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.navi_gator.R;

import java.util.ArrayList;

public class WaypointAdapter extends RecyclerView.Adapter<WaypointAdapter.ViewHolder> {
    private ArrayList<Waypoint> waypoints;
    private Context context;

    public WaypointAdapter(ArrayList<Waypoint> waypoints,Context context) {
        this.context = context;
        this.waypoints = waypoints;
    }
    public class ViewHolder extends  RecyclerView.ViewHolder{
        TextView titel;
        TextView details;
        ImageView image;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View view){
            super(view);
            titel = view.findViewById(R.id.waypoint_name);
            details = view.findViewById(R.id.waypoint_details);
            image = view.findViewById(R.id.waypoint_image);
            parentLayout = view.findViewById(R.id.parent_layout);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.waypoint_fragment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i){
        final Waypoint waypoint = waypoints.get(i);
        holder.titel.setText(waypoint.getName());
        if (waypoint.getDescription()!= null){
            holder.details.setText(waypoint.getDescription());
        }

    }


    @Override
    public int getItemCount() {
        return waypoints.size();
    }
}
