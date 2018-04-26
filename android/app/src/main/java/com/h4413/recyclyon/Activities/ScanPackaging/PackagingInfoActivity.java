package com.h4413.recyclyon.Activities.ScanPackaging;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;
import com.h4413.recyclyon.Listeners.NavigationItemSelectedListener;
import com.h4413.recyclyon.R;

public class PackagingInfoActivity extends AppCompatActivity {

    private Barcode mBarcode;

    private TextView mBarCodeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packaging_info);
        initNavigationMenu();
        Intent intent = getIntent();
        mBarcode = intent.getParcelableExtra("Barcode");
        mBarCodeText = (TextView) findViewById(R.id.packaging_info_barcode);
        if(mBarcode != null) {
            mBarCodeText.setText(mBarcode.displayValue);
        } else {
            mBarCodeText.setText("Erreur dans la lecture du code barre.");
        }
    }

    private void initNavigationMenu() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_packaging_info);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.template_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationItemSelectedListener(this));
        navigationView.setCheckedItem(R.id.nav_scan);
    }
}
