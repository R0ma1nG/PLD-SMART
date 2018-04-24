package com.h4413.recyclyon.NavigationView;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.h4413.recyclyon.Connection.ConnectionActivity;
import com.h4413.recyclyon.Connection.ForgotPwdActivity;
import com.h4413.recyclyon.R;

public class NavigationItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {

    Activity mActivity;

    public NavigationItemSelectedListener(Activity activity) {
        mActivity = activity;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_homepage) {
            Intent intent = new Intent(mActivity.getApplicationContext(), ForgotPwdActivity.class);
            mActivity.startActivity(intent);
        } else if (id == R.id.nav_deposit) {

        } else if (id == R.id.nav_account) {

        } else if (id == R.id.nav_map) {

        } else if (id == R.id.nav_scan) {

        } else if (id == R.id.nav_schedule) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(mActivity.getApplicationContext(), ConnectionActivity.class);
            mActivity.startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) mActivity.findViewById(R.id.template_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
