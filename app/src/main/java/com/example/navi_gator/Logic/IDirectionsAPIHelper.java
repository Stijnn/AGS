package com.example.navi_gator.Logic;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface IDirectionsAPIHelper {
    void onParserResult(List<List<LatLng>> result);
}
