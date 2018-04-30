package com.h4413.recyclyon.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.h4413.recyclyon.R;
import com.h4413.recyclyon.Utilities.NavbarInitializer;

public class PlanningActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning);
        NavbarInitializer.initNavigationMenu(this, R.id.nav_planning, R.string.nav_planning);

        Spinner spinner = (Spinner) findViewById(R.id.planning_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.planning_zone_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void openBrowser(View view){
        String url = (String) view.getTag();

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(url));

        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView greyTextView = findViewById(R.id.planning_grey_days_textview);
        TextView yellowTextView = findViewById(R.id.planning_yellow_days_textview);
        ImageView downloadButton = findViewById(R.id.planning_download_button);

        String[] greyDays = getResources().getStringArray(R.array.planning_grey_trash_day);
        String[] yellowDays = getResources().getStringArray(R.array.planning_yellow_trash_day);
        String[] links = getResources().getStringArray(R.array.planning_link_array);
        greyTextView.setText(greyDays[position]);
        yellowTextView.setText(yellowDays[position]);
        downloadButton.setTag(links[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
