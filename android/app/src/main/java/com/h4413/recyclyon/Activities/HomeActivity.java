package com.h4413.recyclyon.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.gson.Gson;
import com.h4413.recyclyon.Activities.Deposit.DepositQRActivity;
import com.h4413.recyclyon.Adapters.HistoricRecyclerViewAdapter;

import com.h4413.recyclyon.Listeners.NavigationItemSelectedListener;
import com.h4413.recyclyon.Model.Depot;
import com.h4413.recyclyon.Model.DepotList;
import com.h4413.recyclyon.Model.User;
import com.h4413.recyclyon.R;
import com.h4413.recyclyon.Services.UserServices;
import com.h4413.recyclyon.Utilities.HttpClient;
import com.h4413.recyclyon.Utilities.NavbarInitializer;
import com.h4413.recyclyon.Utilities.Routes;
import com.h4413.recyclyon.Utilities.SharedPreferencesKeys;

import org.json.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private Button mDepotButton;
    private TextView mDonationsText;
    private RecyclerView mHstoricRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_home);
        super.onCreate(savedInstanceState);

        NavbarInitializer.initNavigationMenu(this, R.id.nav_homepage, R.string.nav_homepage);

        mDonationsText = (TextView) findViewById(R.id.home_activity_donations_text);
        mDepotButton = (Button) findViewById(R.id.home_activity_depot_btn);
        mHstoricRecyclerView = (RecyclerView) findViewById(R.id.home_activity_recyclerView);

        mHstoricRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mHstoricRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)

        /*Historic historic = new Historic();
        historic.depots.add(new HistoricEntry(new Date(), 1.2f, "hfezigflbeuogfuiozb"));
        historic.depots.add(new HistoricEntry(new Date(), 2.4f, "lgkzmenogubz^^ihzizrg"));
        historic.depots.add(new HistoricEntry(new Date(), 3.3f, "foianeoifhiheaà!fg"));
        historic.depots.add(new HistoricEntry(new Date(), 4.1f, "ioazfhgfyigazipfgaiu"));*/


        mUser = UserServices.getCurrentUserFromSharedPreferences(this);
        boolean result = HttpClient.GET(Routes.Historic, mUser._id, this, new HttpClient.OnResponseCallback() {
            @Override
            public void onJSONResponse(int statusCode, JSONObject response) {
                Gson gson = new Gson();
                List<Depot> depots = gson.fromJson(response.toString(), DepotList.class).data;
                Collections.reverse(depots);
                mAdapter = new HistoricRecyclerViewAdapter(depots);
                mHstoricRecyclerView.setAdapter(mAdapter);
                calculateDonations(depots);
            }
        });
        if(!result)
        {
            Toast.makeText(getApplicationContext(), "Pas de connexion internet", Toast.LENGTH_LONG).show();
        }


        mDepotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, DepositQRActivity.class);
                startActivityForResult(intent, NavigationItemSelectedListener.REQUEST_CODE_DEPOT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_homepage);
        switch(requestCode) {
            case NavigationItemSelectedListener.REQUEST_CODE_DEPOT:
                HttpClient.GET(Routes.Historic, mUser._id, this, new HttpClient.OnResponseCallback() {
                    @Override
                    public void onJSONResponse(int statusCode, JSONObject response) {
                        Gson gson = new Gson();
                        List<Depot> depots = gson.fromJson(response.toString(), DepotList.class).data;
                        Collections.reverse(depots);
                        mAdapter = new HistoricRecyclerViewAdapter(depots);
                        mHstoricRecyclerView.setAdapter(mAdapter);
                        calculateDonations(depots);
                    }
                });
                break;
            case NavigationItemSelectedListener.REQUEST_CODE_ACCOUNT:
                break;
            case NavigationItemSelectedListener.REQUEST_CODE_MAP:
                break;
            case NavigationItemSelectedListener.REQUEST_CODE_SCAN:
                break;
            case NavigationItemSelectedListener.REQUEST_CODE_SCHEDULE:
                break;
            case NavigationItemSelectedListener.REQUEST_CODE_SETTINGS:
                break;
        }
    }

    private void calculateDonations(List<Depot> depots) {
        float sum = 0f;
        for (Depot d : depots) {
            sum += d.montant;
        }
        mDonationsText.setText(String.valueOf(sum)+"€");
    }
}
