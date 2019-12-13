package com.example.navi_gator.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.navi_gator.R;

public class MainActivity extends AppCompatActivity {

    /**
     * De mainactivity is er eigenlijk alleen voor het opstarten van de app.
     * Dit wordt op het scherm weergegeven als de app nog iets aan het laden is.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
