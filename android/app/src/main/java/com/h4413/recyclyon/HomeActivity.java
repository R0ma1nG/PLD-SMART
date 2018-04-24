package com.h4413.recyclyon;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.h4413.recyclyon.NavigationView.NavigationItemSelectedListener;
import com.h4413.recyclyon.Utility.AsyncHTTP;
import com.h4413.recyclyon.Utility.NetworkAccess;

import java.util.concurrent.ExecutionException;

public class HomeActivity extends AppCompatActivity {

    public TextView mText;
    public Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initNavigationMenu();

        mText = (TextView) findViewById(R.id.home_activity_test);
        mButton = (Button) findViewById(R.id.home_activity_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHTTP http = new AsyncHTTP();
                try {
                    String str = http.execute("/api/users").get();
                    mText.setText(str);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        NetworkAccess network = new NetworkAccess(getApplicationContext());
        if(network.isNetworkAvailable())
        {
            Toast.makeText(getApplicationContext(), "Access to internet", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Unable to access to internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void initNavigationMenu() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.template_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationItemSelectedListener(this));
    }
}
