package com.example.navi_gator.Models.GPS;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class GPSManager extends Service {

    private Context context;

    public static final String PERMISSION_STRING
            = android.Manifest.permission.ACCESS_FINE_LOCATION;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private IUserNavigatorUpdater listener;

    public GPSManager(Context context, IUserNavigatorUpdater listener) {
        this.context = context;
        this.listener = listener;
    }

    public LocationListener getLocationListener() {
        return locationListener;
    }

    public void isLocationEnabled() {
        // check to see whether the user gave permission to use location services, if not prompts the user for permission

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Enable Location");
            alertDialog.setMessage("Your locations settings are disabled. \nPlease enabled it in settings menu or from the top-down draggable shortcut window");
            alertDialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    try {

                    context.startActivity(intent);

                    } catch (NullPointerException ex) {
                        //TODO make notification
                    }
                }
            });
            alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                    // TODO make notification

                    // Placeholder
                    Toast.makeText(context, "You can still continue using the map, \nwhile location is disabled.\nTo use location you can toggle it on from the settings menu", Toast.LENGTH_LONG).show();
                }
            });
            AlertDialog alert = alertDialog.create();
            alert.show();
        }
        // not necessary

//            AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);
//            alertDialog.setTitle("Confirm Location");
//            alertDialog.setMessage("Your Location is enabled, please enjoy");
//            alertDialog.setNegativeButton("Back to interface",new DialogInterface.OnClickListener(){
//                public void onClick(DialogInterface dialog, int which){
//                    dialog.cancel();
//                }
//            });
//            AlertDialog alert=alertDialog.create();
//            alert.show();
    }

    public void setupLocationServices(LocationListener locationListener, LocationManager locationManager) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    listener.updateUserNavigatorLocation(location);
                    Toast.makeText(context, "lat: " + location.getLatitude()+ "\nlong: "  + location.getLongitude(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                //TODO make notification

                Toast.makeText(context, "Location Service altered", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderEnabled(String provider) {
                //TODO make notification

                Toast.makeText(context, "Location Service enabled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderDisabled(String provider) {
                //TODO make notification

                Toast.makeText(context, "Location Service disabled", Toast.LENGTH_SHORT).show();
            }
        };
        this.locationManager = locationManager;
        this.locationListener = locationListener;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
