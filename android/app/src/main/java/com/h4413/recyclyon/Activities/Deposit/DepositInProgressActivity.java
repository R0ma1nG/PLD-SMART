package com.h4413.recyclyon.Activities.Deposit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;
import com.h4413.recyclyon.Model.User;
import com.h4413.recyclyon.R;
import com.h4413.recyclyon.Utilities.HttpClient;
import com.h4413.recyclyon.Utilities.Routes;
import com.h4413.recyclyon.Utilities.SharedPreferencesKeys;

import org.json.JSONException;
import org.json.JSONObject;


public class DepositInProgressActivity extends AppCompatActivity {

    private Barcode mQRCode;

    private Button mFinishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_in_progress);
        configureToolbar();

        Intent intent = getIntent();
        mQRCode = intent.getParcelableExtra("QRCode");

        mFinishButton = (Button) findViewById(R.id.deposit_progress_button);

        Gson gson = new Gson();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String str = sharedPref.getString(SharedPreferencesKeys.USER_KEY, "");
        User usr = gson.fromJson(str, User.class);
        JSONObject body = new JSONObject();
        try {
            body.put("idUtilisateur", usr._id);
            body.put("idAssoc", usr.idAssoc);
            body.put("idCapteur", mQRCode.displayValue);
        } catch (JSONException e) { e.printStackTrace(); }

        HttpClient.POST(Routes.BeginDeposit, body.toString(), DepositInProgressActivity.this, new HttpClient.OnResponseCallback() {
            @Override
            public void onJSONResponse(int statusCode, JSONObject response) {
                if(statusCode == 200) {

                } else {
                    Intent intent = new Intent(DepositInProgressActivity.this, DepositRejectionActivity.class);
                    startActivity(intent);
                }
            }
        });

        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void configureToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_deposit_in_progress);
        setSupportActionBar(toolbar);
    }
}
