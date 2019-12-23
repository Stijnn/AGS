package com.example.navi_gator.Logic;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import androidx.annotation.Nullable;

import com.example.navi_gator.Models.API.Route;
import com.example.navi_gator.Models.API.Waypoint;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DirectionsAPI implements IDirectionsAPIHelper {

    // The Google Maps Directions API will only work with a Server key.
    private String MY_API_KEY = "97cb2f24-ffa2-412b-ac7e-deb64176af35\n";

    private GoogleMap mMap;
    private List<PolylineOptions> mPolyLinesOptions;
    private List<Polyline> mPolylines;
    private List<LatLng> passedPoints;
    private List<Polyline> linesPasser;

    private boolean onRouteCreationSuccess;

    private IRouteLeavingCallback routeLeavingCallback;

    // Special prefixes used in the directions url formatting
    // These are used when assigning extra waypoints in the format of LatLng
    public final String pipeDividerCodePrefix = "%7C";
    public final String commaCodePrefix = "%2C";

    private List<List<Waypoint>> divideWaypoints;

    private final boolean backUpKeyRequired = false;

    private LatLng northEast;
    private LatLng southWest;

    private Route route;

    private int index;

    private int requestCount;

    public DirectionsAPI(Route route, GoogleMap map,IRouteLeavingCallback callback, boolean onRouteCreationSuccess) {

        this.onRouteCreationSuccess = onRouteCreationSuccess;
        this.routeLeavingCallback = callback;

        if (backUpKeyRequired) {
            this.MY_API_KEY = "b9b76fa5-992f-4271-bdb3-3a5e2ebc0bf3";
        }

        this.route = route;

        if (onRouteCreationSuccess) {
            this.divideWaypoints = divideWaypoints();
        } else {
            this.divideWaypoints = new ArrayList<>();
        }

        this.mMap = map;
        this.passedPoints = new ArrayList<>();
        this.mPolylines = new ArrayList<>();
        this.mPolyLinesOptions = new ArrayList<>();
        this.linesPasser = new ArrayList<>();
        this.requestCount = this.divideWaypoints.size();

        createRoutePolyLinesOnMap(this);

        mMap.setMaxZoomPreference(20);
        mMap.setMinZoomPreference(15);
    }

    public void TestRouteStringGeneration() {
        for (int i = 0; i < this.divideWaypoints.size(); i++) {
            if (i == 0) {
                generateRouteStartURL();
            } else {
                generateRouteRestURL(i);
            }
        }
    }

    private List<List<Waypoint>> divideWaypoints() {
        List<List<Waypoint>> waypointListDivided = new ArrayList<>();

        boolean newListRequired;
        List<Waypoint> waypointsOfRoute = this.route.getRouteWaypoints();
        List<Waypoint> dividedList = new ArrayList<>();

        for (int i = 1; i <= this.route.getRouteWaypoints().size(); i++) {
            newListRequired = i % 9 == 1 && i != 1;
            if (newListRequired) {
                waypointListDivided.add(dividedList);
                dividedList = new ArrayList<>();
            }
            dividedList.add(waypointsOfRoute.get(i - 1));
        }
        waypointListDivided.add(dividedList);
        return waypointListDivided;
    }

    public void drawPolyLinesOnMap() {
        if (mPolyLinesOptions != null && this.requestCount == 0) {
            for (PolylineOptions mPolyline : mPolyLinesOptions) {
                mMap.addPolyline(mPolyline);
            }
        }
    }

    public void createRoutePolyLinesOnMap(final IDirectionsAPIHelper helper) {
        this.index = 0;

        for (int i = 0; i < this.divideWaypoints.size(); i++) {
            if (i == 0) {
                Thread threadStart = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String routeStartURL = generateRouteStartURL();
                        FetchUrl FetchUrl = new FetchUrl(helper);
                        FetchUrl.execute(routeStartURL);
                        index++;
                    }
                });
                FetchUrl.SERIAL_EXECUTOR.execute(threadStart);

            } else {
                Thread threadRest = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String routeRestURL = generateRouteRestURL(index);
                        FetchUrl FetchUrl = new FetchUrl(helper);
                        FetchUrl.execute(routeRestURL);
                        index++;
                    }
                });
                FetchUrl.SERIAL_EXECUTOR.execute(threadRest);
            }
        }
    }

    public String generateRouteStartURL() {
        String output = "";

        try {
            List<Waypoint> waypoints = this.divideWaypoints.get(0);
            output = getDirectionsUrl(
                    waypoints.get(0).getLatlong(),
                    waypoints.get(waypoints.size() - 1).getLatlong(),
                    generateExtraWaypointsStringFromList(waypoints));
            Log.wtf("TESTING GEN ALL URL", "Testing item: String output\n" + output + "\n" +
                    "With the route starting with waypoint number: 1" +
                    ", And ending with: " + waypoints.get(waypoints.size() - 1).getNumber() + "\n" + "---------------------------------------------------------------");

        } catch (IndexOutOfBoundsException exc) {

        }
        return output;
    }

    public String generateRouteRestURL(int segment) {
        String output = "";
        List<Waypoint> waypointsList = this.divideWaypoints.get(segment);
        Waypoint lastWaypoint = this.divideWaypoints.get(segment - 1).get(this.divideWaypoints.get(segment - 1).size() - 1);

        output = getDirectionsUrl(
                lastWaypoint.getLatlong(),
                waypointsList.get(waypointsList.size() - 1).getLatlong(),
                generateExtraWaypointsStringFromList(waypointsList));
        Log.wtf("TESTING GEN ALL URL", "Testing item: String output\n" + output + "\n" +
                "With the route starting with waypoint number: " + lastWaypoint.getNumber() +
                ", And ending with: " + waypointsList.get(waypointsList.size() - 1).getNumber() + "\n" + "---------------------------------------------------------------");

        return output;
    }

    public String generateExtraWaypointsStringFromList(List<Waypoint> waypointsToConvert) {
        StringBuilder waypointString = new StringBuilder("&waypoints=");

        int length = 0;

        for (int i = 0; i < waypointsToConvert.size() - 1; i++) {
            waypointString.append(waypointsToConvert.get(i).getLatlong().latitude).append(commaCodePrefix).append(waypointsToConvert.get(i).getLatlong().longitude).append(pipeDividerCodePrefix);
        }
        length = waypointString.length();

        return String.valueOf(waypointString).substring(0, length - 3);
    }

    public String getDirectionsUrl(LatLng startingPostion, LatLng destination, @Nullable String extraWaypoints) {

        String str_origin = "origin=" + startingPostion.latitude + "," + startingPostion.longitude;

        // Detination of route
        String str_dest = "destination=" + destination.latitude + "," + destination.longitude;

        String trafficMode = "mode=walking";
//        String trafficMode = "mode=driving";

        if (extraWaypoints == null) {
            extraWaypoints = ""; // Or empty it wont cause problems for the URL generation
        }

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + extraWaypoints + "&" + trafficMode + "&key=" + MY_API_KEY;

        // Output format
        String output = "/json";

        // Building the url to the web service
        //String url = "https://maps.googleapis.com/maps/api/directions" + output + "?" + parameters;
        String url = "http://145.48.6.80:3000/directions" + "?" + parameters; // Via onze eigen quota server

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public void drawRoutePolyLine(Location location) {

        float[] results = new float[1];
        for (Iterator<Polyline> pl = this.mPolylines.iterator(); pl.hasNext(); ) {
            Polyline mPolyLine = pl.next();
            for (Iterator<LatLng> it = mPolyLine.getPoints().iterator(); it.hasNext(); ) {
                LatLng latLng = it.next();
                Location.distanceBetween(location.getLatitude(), location.getLongitude(), latLng.latitude, latLng.longitude, results);
                float distanceInMeters = results[0];
                if (distanceInMeters < 50) {
//                    this.routeLeavingCallback.onRouteLeave("You're leaving the route, go back!");
                }
            }
        }

        this.passedPoints.add(new LatLng(location.getLatitude(), location.getLongitude()));

        PolylineOptions options = new PolylineOptions();
        options.width(5);
        options.color(Color.CYAN);

        for (LatLng passedPoint : this.passedPoints) {
            options.add(passedPoint);
        }
        this.linesPasser.add(mMap.addPolyline(options));
    }

    @Override
    public void onParserResult(List<List<LatLng>> result) {
        PolylineOptions options = new PolylineOptions();

        northEast = result.get(0).get(0);
        southWest = result.get(0).get(1);
        // The first list contained the bounds of the route and is not part of the route:
        result.remove(0);
        options.width(3);
        options.color(Color.GRAY);

        for (List<LatLng> leg : result) {
            options.addAll(leg);
        }
        this.mPolyLinesOptions.add(options);

        Log.d("onPostExecute", "onPostExecute lineoptions decoded");

//             Drawing polyline in the Google Map for the i-th route
        if (mPolyLinesOptions != null) {

            for (PolylineOptions mPolyline : mPolyLinesOptions) {
               this.mPolylines.add(mMap.addPolyline(mPolyline));
            }

//                // zoom to bounding-box of the route:
            LatLngBounds bounds = new LatLngBounds(southWest, northEast);
            int padding = 20;
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
//
        } else {
            Log.d("onPostExecute", "without Polylines drawn");
        }
    }

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        private IDirectionsAPIHelper listener;

        public FetchUrl(IDirectionsAPIHelper helper) {
            this.listener = helper;
        }

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask(listener);

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    // todo - error handling indien de aanroep niet geldig is (bijvoorbeeld limiet bereikt).
    private class ParserTask extends AsyncTask<String, Integer, List<List<LatLng>>> {

        private IDirectionsAPIHelper helper;

        public ParserTask(IDirectionsAPIHelper helper) {
            this.helper = helper;
        }

        // Parsing the data in non-ui thread
        @Override
        protected List<List<LatLng>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<LatLng>> routeData = null;

            try {
                jObject = new JSONObject(jsonData[0]);

                Log.d("ParserTask", jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing routes data
                routeData = parser.parseRoutesInfo(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask-routes: ", routeData.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routeData;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<LatLng>> result) {
            try {
                this.helper.onParserResult(result);
            } catch (NullPointerException ex) {
                Log.e("onParserExecute", ex.getMessage() + ": Error occurred, direction-api not available\n" +
                        "Check the API-key");
            }
        }
    }
}
