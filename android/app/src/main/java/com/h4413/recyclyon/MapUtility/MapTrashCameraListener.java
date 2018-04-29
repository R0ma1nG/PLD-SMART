package com.h4413.recyclyon.MapUtility;

import com.google.android.gms.maps.GoogleMap;
import com.h4413.recyclyon.Activities.MapsActivity;

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
            activity.populator.hideMarkers();
        } else {
            activity.populator.displayMarkers();
        }
    }

}
