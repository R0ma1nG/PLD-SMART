package com.h4413.recyclyon.MapUtility;

import android.app.AlertDialog;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.h4413.recyclyon.MapsActivity;
import com.h4413.recyclyon.R;
import com.h4413.recyclyon.Model.Bin;

import java.util.ArrayList;
import java.util.List;

public class MapPopulator implements GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private MapsActivity activity;
    private List<Marker> markers;

    public MapPopulator(GoogleMap m, MapsActivity mActivity){
        mMap = m;
        activity = mActivity;
        markers = new ArrayList<>();
    }

    public void createMarkers(List<Bin> bins){
        Toast.makeText(activity, "start Create", Toast.LENGTH_SHORT);
        for(Bin bin : bins){
            Marker marker;
            LatLng ll = new LatLng(bin.getLat(), bin.getLong());
            if(bin.isFull()){
                marker = mMap.addMarker(new MarkerOptions().position(ll)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bin_full))
                        .visible(false));
            } else {
                marker = mMap.addMarker(new MarkerOptions().position(ll)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bin))
                        .visible(false));
            }
            marker.setTag(bin);
            markers.add(marker);
        }
        mMap.setOnMarkerClickListener(this);
        displayMarkers();
    }

    public void displayMarkers(){
        for(Marker marker : markers){
            if(mMap.getProjection().getVisibleRegion().latLngBounds.contains(marker.getPosition())){
                marker.setVisible(true);
            } else {
                marker.setVisible(false);
            }
        }
    }

    public void fillMapPrototype(List<LatLng> bins){
        for(LatLng ll : bins) {
            mMap.addMarker(new MarkerOptions().position(ll).title("Poubelle").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bin)));
        }
        mMap.setOnMarkerClickListener(this);
    }

    public void fillMap(List<Bin> bins){
        for(Bin bin : bins){
            LatLng ll = new LatLng(bin.getLat(), bin.getLong());
            Marker binMarker;
            if(bin.isFull()){
                binMarker = mMap.addMarker(new MarkerOptions().position(ll).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bin_full)));
            } else {
                binMarker = mMap.addMarker(new MarkerOptions().position(ll).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bin)));
            }
            binMarker.setTag(bin);
        }

        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Bin bin = (Bin) marker.getTag();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        if(bin.isFull()) {
            builder.setMessage(bin.getAdress()).setTitle("Benne remplie");
        } else {
            builder.setMessage(bin.getAdress()).setTitle("Benne disponible");
        }
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }


}
