package com.h4413.recyclyon.MapUtility;

import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.VisibleRegion;
import com.h4413.recyclyon.MapsActivity;
import com.h4413.recyclyon.Model.Bin;

import java.util.ArrayList;
import java.util.List;

public class MapTrashCameraListener implements GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener {

    private GoogleMap mMap;
    private MapsActivity activity;

    public MapTrashCameraListener(GoogleMap map, MapsActivity mapsActivity){
        mMap = map;
        activity = mapsActivity;
    }


    @Override
    public void onCameraMove() {

    }

    @Override
    public void onCameraIdle() {
        if(mMap.getCameraPosition().zoom < 15){
            mMap.clear();
        } else {
            activity.populator.displayMarkers();
        }
    }

}
