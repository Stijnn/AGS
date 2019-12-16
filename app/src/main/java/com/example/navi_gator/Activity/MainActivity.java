package com.example.navi_gator.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.navi_gator.Logic.DatabaseManager;
import com.example.navi_gator.Models.API.Route;
import com.example.navi_gator.R;

public class MainActivity extends AppCompatActivity {

    /**
     * De mainactivity is er eigenlijk alleen voor het opstarten van de app.
     * Dit wordt op het scherm weergegeven als de app nog iets aan het laden is.
     */

    private DatabaseManager databaseManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseManager = new DatabaseManager(this.getBaseContext());

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
