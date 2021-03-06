package com.example.navi_gator.Logic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.navi_gator.Models.API.Route;
import com.example.navi_gator.Models.API.Waypoint;
import com.example.navi_gator.R;
import com.google.android.gms.maps.model.LatLng;

import java.nio.file.Watchable;
import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper {
    public static final String DB_NAME = "NAVI_GATOR_DB";
    private static final int DB_VERSION = 1;

    public DatabaseManager(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Creates the database and its required tables
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE %s (" +
                "id TEXT," +
                "name TEXT," +
                "description TEXT," +
                "finished INTEGER);", Route.TABLE_NAME));

        db.execSQL(String.format("CREATE TABLE %s (" +
                "id TEXT," +
                "number INTEGER," +
                "description TEXT," +
                "name TEXT," +
                "lon DOUBLE," +
                "lat DOUBLE);", Waypoint.TABLE_NAME));

        db.execSQL(String.format("CREATE TABLE %s (" +
                "route_id TEXT," +
                "waypoint_id TEXT," +
                "id_number INTEGER," +
                "visited INTEGER);", "tbl_RouteWaypoints"));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Retrieve route from the database
     * @param route_id The id for the route
     * @return found route, in case not found returns null
     */
    public Route getRoute(String route_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("SELECT * FROM %s WHERE id == '%s';", Route.TABLE_NAME, route_id), new String[]{});
        if (cursor.moveToFirst())
        {
            Route r = new Route(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3) > 0);
            r.setRouteWaypoints(getWaypoints(r));
            return r;
        }
        else
            return null;
    }

    /**
     * Retrieve waypoints by route from the database
     * @param route route for identification
     * @return List of waypoints
     */
    public ArrayList<Waypoint> getWaypoints(Route route) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("SELECT * FROM %s WHERE route_id == '%s';", "tbl_RouteWaypoints", route.getId()), new String[]{});
        ArrayList<Waypoint> waypoints = new ArrayList<>();
        ArrayList<Integer> waypoint_number = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                waypoint_number.add(cursor.getInt(2));
            }
            while (cursor.moveToNext());
        }

        try {
            for (int i = 0; i < waypoint_number.size(); i++) {
                Cursor waypointCursor = db.rawQuery(String.format("SELECT * FROM %s WHERE number == '%s';", Waypoint.TABLE_NAME, waypoint_number.get(i)), new String[]{});
                cursor = db.rawQuery(String.format("SELECT * FROM %s WHERE id_number == '%s';", "tbl_RouteWaypoints", waypoint_number.get(i)), new String[]{});

                if (waypointCursor.moveToFirst() && cursor.moveToFirst()) {
                    boolean visited = cursor.getInt(3) > 0;

                    waypoints.add(new Waypoint(
                            visited, //Visited
                            cursor.getString(1),         //Id
                            waypointCursor.getInt(1), //Number
                            new LatLng(waypointCursor.getDouble(5), waypointCursor.getDouble(4)),
                            waypointCursor.getString(3), //name
                            waypointCursor.getString(2)) //description
                    );
                }
                waypointCursor.close();
            }
        } catch (IndexOutOfBoundsException ex) {
            // Error caught
        }

        cursor.close();
        return waypoints;
    }

    /**
     * Updates route
     * @param route object to update
     */
    public void updateRoute(Route route) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(String.format("UPDATE %s" +
                "SET finished = %d" +
                "WHERE id == '%s';", Route.TABLE_NAME, route.isFinished() ? 1 : 0, route.getId()));
    }

    /**
     * Updates waypoint
     * @param route identifier
     * @param waypoint waypoint to update in linked table
     */
    public void updateRouteWaypoint(Route route, Waypoint waypoint) {
        SQLiteDatabase db = this.getWritableDatabase();

        Waypoint wp = waypoint;

        ContentValues cv = new ContentValues();
        cv.put("visited", wp.isVisited());

        String whereClause = "route_id = '" + route.getId() + "' AND waypoint_id = '" + wp.getName() + "';";

        db.update("tbl_RouteWaypoints", cv, whereClause, null);
    }

    /**
     * Add route to the database
     * @param route route to add
     */
    public void addRoute(Route route) {
        ContentValues cv = new ContentValues();
        cv.put("id", route.getId());
        cv.put("name", route.getName());
        cv.put("description", route.getDescription());
        cv.put("finished", route.isFinished());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(Route.TABLE_NAME, null, cv);
    }

    /**
     * Add waypoint to the database
     * @param waypoint waypoint to add
     */
    public void addWaypoint(Waypoint waypoint) {
        ContentValues cv = new ContentValues();
        cv.put("id", waypoint.getId());
        cv.put("number", waypoint.getNumber());
        cv.put("description", waypoint.getDescription());
        cv.put("name", waypoint.getName());
        cv.put("lon", waypoint.getLatlong().longitude);
        cv.put("lat", waypoint.getLatlong().latitude);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(Waypoint.TABLE_NAME, null, cv);
    }

    /**
     * Add link between route and waypoint
     * @param route route id
     * @param waypoint waypoint id
     * @param visited is the waypoint visited?
     */
    public void addRouteWaypoint(Route route, Waypoint waypoint, boolean visited) {
        ContentValues cv = new ContentValues();
        cv.put("route_id", route.getId());
        cv.put("waypoint_id", waypoint.getId());
        cv.put("id_number", waypoint.getNumber());
        cv.put("visited", visited);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("tbl_RouteWaypoints", null, cv);
    }
}
