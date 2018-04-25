package com.h4413.recyclyon.Activities.Connection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.h4413.recyclyon.Adapters.ChooseAssociationRecyclerViewAdapter;
import com.h4413.recyclyon.Model.Association;
import com.h4413.recyclyon.R;

public class ChooseAssociationActivity extends AppCompatActivity implements AssociationAdapterCallback{

    private static final int REQUEST_CODE_CGU = 1;
    public static final String SP_KEY_ASSOCIATION = "ChosenAssociation";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private TextView mCurrentApplicationText;
    private Button mValidateButton;

    private Association mCurrentAssociation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_association);
        configureToolbar();

        mRecyclerView = (RecyclerView) findViewById(R.id.choose_association_activity_recyclerView);
        mCurrentApplicationText = (TextView) findViewById(R.id.choose_association_activity_association_text);
        mValidateButton = (Button) findViewById(R.id.choose_association_activity_validate_btn);

        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        Association[] testDataSet = {new Association(123, "Asso 1", "Ceci est une association", "http://www.gstatic.com/webp/gallery/1.jpg"),
                new Association(456,"Asso 2", "Une autre association", "http://www.gstatic.com/webp/gallery/2.jpg"),
                new Association(789,"Asso 3", "Encore une", "http://www.gstatic.com/webp/gallery/2.jpg")};
        mAdapter = new ChooseAssociationRecyclerViewAdapter(testDataSet, this);
        mRecyclerView.setAdapter(mAdapter);

        mValidateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseAssociationActivity.this, CGUActivity.class);
                //intent.putExtra("Association", mCurrentAssociation);
                startActivityForResult(intent, REQUEST_CODE_CGU);
            }
        });
        mValidateButton.setEnabled(false);
    }

    private void configureToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CGU && resultCode == RESULT_OK) {
            Gson gson = new Gson();
            SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
            String json = sharedPref.getString(SP_KEY_ASSOCIATION, "");
            mCurrentAssociation = gson.fromJson(json, Association.class);
            Intent intent = new Intent();
            intent.putExtra(SP_KEY_ASSOCIATION, mCurrentAssociation);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    protected void onStop() {
        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();
        sharedPref.edit().putString(SP_KEY_ASSOCIATION, gson.toJson(mCurrentAssociation)).commit();
        super.onStop();
    }

    @Override
    public void onClickCallback(Association itemModel) {
        mCurrentApplicationText.setText(itemModel.nom);
        mCurrentAssociation = itemModel;
        mValidateButton.setEnabled(true);
    }
}
