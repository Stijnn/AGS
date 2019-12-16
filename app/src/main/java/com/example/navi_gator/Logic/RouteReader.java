package com.example.navi_gator.Logic;

import android.os.FileUtils;
import android.util.Log;

import com.google.android.gms.common.util.IOUtils;
import com.google.android.gms.common.util.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class RouteReader {



    public RouteReader(InputStream is){
        try {
            JSONObject json = new JSONObject(inputStreamToJsonString(is));
            Log.d("JSON", json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private String inputStreamToJsonString(InputStream is){
        String json = null;
        try {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

}


