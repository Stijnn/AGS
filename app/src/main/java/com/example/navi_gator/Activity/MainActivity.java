package com.example.navi_gator.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.navi_gator.Logic.DatabaseManager;
import com.example.navi_gator.Logic.RouteReader;
import com.example.navi_gator.Models.API.Route;
import com.example.navi_gator.Models.API.Waypoint;
import com.example.navi_gator.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /**
     * De mainactivity is er eigenlijk alleen voor het opstarten van de app.
     * Dit wordt op het scherm weergegeven als de app nog iets aan het laden is.
     */

    private DatabaseManager databaseManager;
    private ImageView ags_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseManager = new DatabaseManager(this.getBaseContext());

        SharedPreferences preferences = this.getSharedPreferences("COOKIES", MODE_PRIVATE);
        if (!preferences.contains("INITIALIZED")) {
            RouteReader reader = new RouteReader(getResources().openRawResource(R.raw.historischekm));
            Route routeObject = reader.getRoute();
            databaseManager.addRoute(routeObject);
            ArrayList<Waypoint> waypoints = new ArrayList<>(routeObject.getRouteWaypoints());
            for (int i = 0; i < waypoints.size(); i++) {
                databaseManager.addWaypoint(waypoints.get(i));
                databaseManager.addRouteWaypoint(routeObject, waypoints.get(i), false);
            }
            preferences.edit().putBoolean("INITIALIZED", true).apply();
        }


        ags_logo = findViewById(R.id.ags_logo_display);
        ags_logo.setImageResource(R.drawable.ags_logo);
        initButton();
        animateText();
    }

    private void initButton() {
        Button btn = (Button) findViewById(R.id.btnStartMaps);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });
    }

    private void setText(final CharSequence text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.animatedText)).setText(text);
            }
        });
    }

    private void animateText() {
        final boolean timeIsTicking = true;

        Thread animatedText = new Thread() {
            public void run() {
                try {
                    int time = 0;
                    while(timeIsTicking) {
                        sleep(100);
                        String textFull = "Navi-gator";
                        int rest = time % (textFull.length() * 100) + 100;
                        setText(textFull.substring(0, rest != 0 ? (rest / 100) : 0));
                        time += 100;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    finish();
                }
            }
        };
        animatedText.start();
    }
}
