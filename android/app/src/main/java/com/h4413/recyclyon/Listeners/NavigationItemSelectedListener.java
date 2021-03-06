package com.h4413.recyclyon.Listeners;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.h4413.recyclyon.Activities.Connection.ConnectionActivity;
import com.h4413.recyclyon.Activities.Deposit.DepositQRActivity;
import com.h4413.recyclyon.Activities.HomeActivity;
import com.h4413.recyclyon.Activities.PlanningActivity;
import com.h4413.recyclyon.Activities.ProfileActivity;
import com.h4413.recyclyon.Activities.ScanPackaging.ScanPackagingActivity;
import com.h4413.recyclyon.Activities.MapsActivity;
import com.h4413.recyclyon.Activities.SettingsActivity;
import com.h4413.recyclyon.R;

public class NavigationItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {

    public final static int REQUEST_CODE_DEPOT = 1;
    public final static int REQUEST_CODE_ACCOUNT = 2;
    public final static int REQUEST_CODE_MAP = 3;
    public final static int REQUEST_CODE_SCAN = 4;
    public final static int REQUEST_CODE_SCHEDULE = 5;
    public final static int REQUEST_CODE_SETTINGS = 6;

    Activity mActivity;

    public NavigationItemSelectedListener(Activity activity) {
        mActivity = activity;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_homepage) {
            if(mActivity.getClass() != HomeActivity.class) {
                Intent intent = new Intent(mActivity.getApplicationContext(), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mActivity.startActivity(intent);
            }
        } else if (id == R.id.nav_deposit) {
            if(mActivity.getClass() != DepositQRActivity.class) {
                Intent intent = new Intent(mActivity.getApplicationContext(), DepositQRActivity.class);
                mActivity.startActivityForResult(intent, REQUEST_CODE_DEPOT);
            }
        } else if (id == R.id.nav_account) {
            if(mActivity.getClass() != ProfileActivity.class)
            {
                Intent intent = new Intent(mActivity.getApplicationContext(), ProfileActivity.class);
                mActivity.startActivityForResult(intent, REQUEST_CODE_SETTINGS);
            }
        } else if (id == R.id.nav_map) {
            if(mActivity.getClass() != MapsActivity.class) {
                Intent intent = new Intent(mActivity.getApplicationContext(), MapsActivity.class);
                mActivity.startActivityForResult(intent, REQUEST_CODE_MAP);
            }
        } else if (id == R.id.nav_scan) {
            if(mActivity.getClass() != ScanPackagingActivity.class)
            {
                Intent intent = new Intent(mActivity.getApplicationContext(), ScanPackagingActivity.class);
                mActivity.startActivityForResult(intent, REQUEST_CODE_SCAN);
            }
        } else if (id == R.id.nav_planning) {
            if(mActivity.getClass() !=PlanningActivity.class)
            {
                Intent intent = new Intent(mActivity.getApplicationContext(), PlanningActivity.class);
                mActivity.startActivity(intent);
            }
        } else if (id == R.id.nav_settings) {
            if(mActivity.getClass() != SettingsActivity.class)
            {
                Intent intent = new Intent(mActivity.getApplicationContext(), SettingsActivity.class);
                mActivity.startActivityForResult(intent, REQUEST_CODE_SETTINGS);
            }
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(mActivity.getApplicationContext(), ConnectionActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mActivity.startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) mActivity.findViewById(R.id.template_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
