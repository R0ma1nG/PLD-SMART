package com.h4413.recyclyon.Activities;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.h4413.recyclyon.Adapters.ChooseAssociationRecyclerViewAdapter;
import com.h4413.recyclyon.Adapters.HistoricRecyclerViewAdapter;
import com.h4413.recyclyon.Listeners.NavigationItemSelectedListener;
import com.h4413.recyclyon.Model.Historic;
import com.h4413.recyclyon.Model.HistoricEntry;
import com.h4413.recyclyon.Model.User;
import com.h4413.recyclyon.R;
import com.h4413.recyclyon.Utilities.HttpClient;
import com.h4413.recyclyon.Utilities.NetworkAccess;
import com.h4413.recyclyon.Utilities.Routes;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    private Button mDepotButton;
    private TextView mDonationsText;
    private RecyclerView mHstoricRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initNavigationMenu();


        mDonationsText = (TextView) findViewById(R.id.home_activity_donations_text);
        mDepotButton = (Button) findViewById(R.id.home_activity_depot_btn);
        mHstoricRecyclerView = (RecyclerView) findViewById(R.id.home_activity_recyclerView);

        mHstoricRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mHstoricRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)

        Historic historic = new Historic();
        historic.depots.add(new HistoricEntry(new Date(), 1.2f, "hfezigflbeuogfuiozb"));
        historic.depots.add(new HistoricEntry(new Date(), 2.4f, "lgkzmenogubz^^ihzizrg"));
        historic.depots.add(new HistoricEntry(new Date(), 3.3f, "foianeoifhiheaà!fg"));
        historic.depots.add(new HistoricEntry(new Date(), 4.1f, "ioazfhgfyigazipfgaiu"));
        mAdapter = new HistoricRecyclerViewAdapter(historic);
        mHstoricRecyclerView.setAdapter(mAdapter);

        mDepotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, DepositActivity.class);
                startActivity(intent);
            }
        });

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
        navigationView.setCheckedItem(R.id.nav_homepage);
    }
}
