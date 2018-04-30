package com.h4413.recyclyon.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.h4413.recyclyon.Activities.Connection.AssociationAdapterCallback;
import com.h4413.recyclyon.Activities.Connection.CGUActivity;
import com.h4413.recyclyon.Adapters.ChooseAssociationRecyclerViewAdapter;
import com.h4413.recyclyon.Model.Association;
import com.h4413.recyclyon.Model.AssociationList;
import com.h4413.recyclyon.R;
import com.h4413.recyclyon.Utilities.HttpClient;
import com.h4413.recyclyon.Utilities.NavbarInitializer;
import com.h4413.recyclyon.Utilities.Routes;

import org.json.JSONObject;

public class ChangeAssociationActivity extends AppCompatActivity implements AssociationAdapterCallback {


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
        NavbarInitializer.configureToolbar(this, R.string.appName);

        mRecyclerView = (RecyclerView) findViewById(R.id.choose_association_activity_recyclerView);
        mCurrentApplicationText = (TextView) findViewById(R.id.choose_association_activity_association_text);
        mValidateButton = (Button) findViewById(R.id.choose_association_activity_validate_btn);

        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        /*Association[] testDataSet = {new Association("5ae0740198c0b2710d9df273", "Asso 1", "Ceci est une association", "http://www.gstatic.com/webp/gallery/1.jpg"),
                new Association("5ae0740198c0b2710d9df274","Asso 2", "Une autre association", "http://www.gstatic.com/webp/gallery/2.jpg"),
                new Association("5ae0740198c0b2710d9df275","Asso 3", "Encore une", "http://www.gstatic.com/webp/gallery/2.jpg")};*/
        HttpClient.GET(Routes.Associations, "", ChangeAssociationActivity.this, new HttpClient.OnResponseCallback() {
            @Override
            public void onJSONResponse(int statusCode, JSONObject response) {
                Gson gson = new Gson();
                AssociationList associations = gson.fromJson(String.valueOf(response), AssociationList.class);
                mAdapter = new ChooseAssociationRecyclerViewAdapter(associations.data, ChangeAssociationActivity.this);
                mRecyclerView.setAdapter(mAdapter);
            }
        });


        mValidateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //intent.putExtra("Association", mCurrentAssociation);

                Gson gson = new Gson();
                SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
                String json = sharedPref.getString(SP_KEY_ASSOCIATION, "");
                mCurrentAssociation = gson.fromJson(json, Association.class);
                Intent intent = new Intent();
                intent.putExtra(SP_KEY_ASSOCIATION, mCurrentAssociation);
                setResult(RESULT_OK, intent);
                finish();

            }
        });
        mValidateButton.setEnabled(false);
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
