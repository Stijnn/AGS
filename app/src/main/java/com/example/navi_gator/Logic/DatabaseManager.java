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

import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper {
    private static final String DB_NAME = "NAVI_GATOR_DB";
    private static final int DB_VERSION = 1;

    public DatabaseManager(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE %s (" +
                "id TEXT," +
                "name TEXT," +
                "description TEXT," +
                "finished INTEGER);", Route.TABLE_NAME));

        db.execSQL(String.format("CREATE TABLE %s (" +
                "id TEXT," +
                "description TEXT," +
                "image TEXT," +
                "lon DOUBLE," +
                "lat DOUBLE);", Waypoint.TABLE_NAME));

        db.execSQL(String.format("CREATE TABLE %s (" +
                "route_id TEXT," +
                "waypoint_id TEXT," +
                "visited INTEGER);", "tbl_RouteWaypoints"));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Route getRoute(String route_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("SELECT * FROM %s WHERE id == %s;", Route.TABLE_NAME, route_id), new String[]{});
        if (cursor.moveToFirst())
            return new Route(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3) > 0);
        else
            return null;
    }

    public ArrayList<Waypoint> getWaypoints(Route route) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("SELECT * FROM %s WHERE route_id == %s;", "tbl_RouteWaypoints", route.getId()), new String[]{});
        ArrayList<Waypoint> waypoints = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Cursor waypointCursor = db.rawQuery(String.format("SELECT * FROM %s WHERE id == %s;", Waypoint.TABLE_NAME, cursor.getString(1)), new String[]{});

                if (waypointCursor.moveToFirst()) {
                    waypoints.add(new Waypoint(
                            cursor.getInt(2) > 0,
                            cursor.getString(1),
                            waypointCursor.getString(1),
                            waypointCursor.getString(2),
                            waypointCursor.getDouble(3),
                            waypointCursor.getDouble(4))
                    );
                }
            }
            while (cursor.moveToNext());
        }
        return waypoints;
    }

    public void updateRoute(Route route) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(String.format("UPDATE %s" +
                "SET finished = %d" +
                "WHERE id == %s;", Route.TABLE_NAME, route.isFinished() ? 1 : 0, route.getId()));
    }

    public void updateRouteWaypoint(Route route, Waypoint waypoint) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(String.format("UPDATE %s" +
                "SET visited = %d" +
                "WHERE route_id == %s AND waypoint_id == %s;", "tbl_RouteWaypoints", waypoint.isVisited() ? 1 : 0, route.getId(), waypoint.getId()));
    }

    public void addRoute(Route route) {
        ContentValues cv = new ContentValues();
        cv.put("id", route.getId());
        cv.put("name", route.getName());
        cv.put("description", route.getDescription());
        cv.put("finished", route.isFinished());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(Route.TABLE_NAME, null, cv);
    }

    public void addWaypoint(Waypoint waypoint) {
        ContentValues cv = new ContentValues();
        cv.put("id", waypoint.getId());
        cv.put("description", waypoint.getDescription());
        cv.put("image", waypoint.getImage());
        cv.put("lon", waypoint.getLon());
        cv.put("lat", waypoint.getLat());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(Waypoint.TABLE_NAME, null, cv);
    }

    public void addRouteWaypoint(Route route, Waypoint waypoint, boolean visited) {
        ContentValues cv = new ContentValues();
        cv.put("route_id", route.getId());
        cv.put("waypoint_id", waypoint.getId());
        cv.put("visited", visited);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("tbl_RouteWaypoints", null, cv);
    }
}
