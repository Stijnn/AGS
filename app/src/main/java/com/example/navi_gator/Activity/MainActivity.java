package com.example.navi_gator.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

                        int rest = time % 1100;

                        switch(rest) {
                            case 0:
                                setText("N");
                                break;
                            case 100:
                                setText("Na");
                                break;
                            case 200:
                                setText("Nav");
                                break;
                            case 300:
                                setText("Navi");
                                break;
                            case 400:
                                setText("Navi-");
                                break;
                            case 500:
                                setText("Navi-g");
                                break;
                            case 600:
                                setText("Navi-ga");
                                break;
                            case 700:
                                setText("Navi-gat");
                                break;
                            case 800:
                                setText("Navi-gato");
                                break;
                            case 900:
                                setText("Navi-gator");
                                break;
                            default:
                                break;
                        }

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
