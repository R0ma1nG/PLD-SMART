package com.h4413.recyclyon.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.h4413.recyclyon.Activities.Connection.ConnectionActivity;
import com.h4413.recyclyon.Listeners.NavigationItemSelectedListener;
import com.h4413.recyclyon.MapUtility.MapPopulator;
import com.h4413.recyclyon.MapUtility.MapTrashCameraListener;
import com.h4413.recyclyon.Model.Bin;
import com.h4413.recyclyon.Model.BinList;
import com.h4413.recyclyon.R;
import com.h4413.recyclyon.Utilities.HttpClient;
import com.h4413.recyclyon.Utilities.NavbarInitializer;
import com.h4413.recyclyon.Utilities.Routes;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final int ACCESS_FINE_LOCATION_REQUEST_CODE = 15;
    private GoogleMap mMap;
    public MapPopulator populator;
    private List<LatLng> bins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        NavbarInitializer.initNavigationMenu(this, R.id.nav_map, R.string.title_activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_REQUEST_CODE);
        }

        populator = new MapPopulator(mMap, this);

        MapTrashCameraListener cameraListener = new MapTrashCameraListener(mMap, this);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnCameraMoveListener(cameraListener);
        mMap.setOnCameraIdleListener(cameraListener);


        HttpClient.GET(Routes.Bins, "", MapsActivity.this, new HttpClient.OnResponseCallback() {
            @Override
            public void onJSONResponse(int statusCode, JSONObject response) {
                //Toast.makeText(getApplicationContext(), "Poubelles : "+String.valueOf(statusCode), Toast.LENGTH_LONG).show();
                Gson gson = new Gson();
                BinList binList = gson.fromJson(response.toString(), BinList.class);
                populator.createMarkers(binList.data);
                populator.displayMarkers();
            }
        });


        /*List<Bin> bins = new ArrayList<>();
        bins.add(new Bin("1", "1 rue des teinturiers", 45.7580549, 4.7650808, true));
        bins.add(new Bin("1", "1 rue des teinturiers not ", 45.7582549, 4.7670808, false));*/

        //populator.fillMapPrototype(bins);



        LatLng position = new LatLng(45.7580539, 4.7650808);
        //mMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bin_full)));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(position).zoom(15).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == ACCESS_FINE_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0].compareTo(Manifest.permission.ACCESS_FINE_LOCATION) == 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
                Intent intent = new Intent(MapsActivity.this, ConnectionActivity.class);
                startActivityForResult(intent, 2);
            } else {

            }
        }
    }
}
