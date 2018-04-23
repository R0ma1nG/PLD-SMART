package com.h4413.recyclyon.Connection;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.h4413.recyclyon.Model.Association;
import com.h4413.recyclyon.R;

public class ChooseAssociationActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_association);
        configureToolbar();

        mRecyclerView = (RecyclerView) findViewById(R.id.choose_association_activity_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        Association[] testDataSet = {new Association("Asso 1", "Ceci est une association", "http://www.gstatic.com/webp/gallery/1.jpg"),
                new Association("Asso 2", "Une autre association", "http://www.gstatic.com/webp/gallery/2.jpg"),
                new Association("Asso 3", "Encore une", "http://www.gstatic.com/webp/gallery/3.jpg")};
        mAdapter = new ChooseAssociationRecyclerViewAdapter(testDataSet);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void configureToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }
}
