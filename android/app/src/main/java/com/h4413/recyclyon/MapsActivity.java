package com.h4413.recyclyon;

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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.h4413.recyclyon.Connection.ConnectionActivity;
import com.h4413.recyclyon.MapUtility.MapPopulator;
import com.h4413.recyclyon.MapUtility.MapTrashCameraListener;
import com.h4413.recyclyon.Model.Bin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        NavigationView.OnNavigationItemSelectedListener{

    private final int ACCESS_FINE_LOCATION_REQUEST_CODE = 15;
    private GoogleMap mMap;
    public MapPopulator populator;
    private List<LatLng> bins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.map_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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



        List<Bin> bins = new ArrayList<>();
        bins.add(new Bin(1, "1 rue des teinturiers", 45.7580549, 4.7650808, true));
        bins.add(new Bin(1, "1 rue des teinturiers not ", 45.7582549, 4.7670808, false));

        //populator.fillMapPrototype(bins);

        populator.createMarkers(bins);


        LatLng position = new LatLng(45.7580539, 4.7650808);
        //mMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bin_full)));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(position).zoom(15).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        System.out.println(permissions[0]);
        System.out.println(Manifest.permission.ACCESS_FINE_LOCATION);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.map_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_homepage) {

        } else if (id == R.id.nav_deposit) {

        } else if (id == R.id.nav_account) {

        } else if (id == R.id.nav_map) {

        } else if (id == R.id.nav_scan) {

        } else if (id == R.id.nav_schedule) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.map_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
