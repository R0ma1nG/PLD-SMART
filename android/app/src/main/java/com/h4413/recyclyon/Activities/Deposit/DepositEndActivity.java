package com.h4413.recyclyon.Activities.Deposit;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.h4413.recyclyon.Activities.Connection.ConnectionActivity;
import com.h4413.recyclyon.Activities.HomeActivity;
import com.h4413.recyclyon.R;
import com.h4413.recyclyon.Utilities.DownLoadImageTask;
import com.h4413.recyclyon.Utilities.HttpClient;
import com.h4413.recyclyon.Utilities.IntentExtraKeys;
import com.h4413.recyclyon.Utilities.NavbarInitializer;
import com.h4413.recyclyon.Utilities.Routes;
import com.h4413.recyclyon.Utilities.SharedPreferencesKeys;

import org.json.JSONException;
import org.json.JSONObject;

public class DepositEndActivity extends AppCompatActivity {

    private Button mFinishButton;
    private TextView mAssociationName;
    private TextView mMontant;
    private ImageView mAssociationLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_end);

        NavbarInitializer.configureToolbar(this, R.string.title_deposit_end);

        Intent intent = getIntent();
        String idAssoc = intent.getStringExtra(IntentExtraKeys.ID_ASSOC_KEY);
        final String montant = intent.getStringExtra(IntentExtraKeys.DEPOT_MONTANT_KEY);

        mFinishButton = (Button)findViewById(R.id.deposit_end_button);
        mAssociationName = (TextView)findViewById(R.id.deposit_end_assoc);
        mMontant = (TextView) findViewById(R.id.deposit_end_montant);
        mAssociationLogo = (ImageView) findViewById(R.id.deposit_end_assoc_img);
        mFinishButton.setEnabled(false);

        HttpClient.GET(Routes.Associations, idAssoc, this, new HttpClient.OnResponseCallback() {
            @Override
            public void onJSONResponse(int statusCode, JSONObject response) {
                try {
                    String assocName = response.get("nom").toString();
                    String url = response.get("logoUrl").toString();
                    mMontant.setText(String.format("%.3f", Float.parseFloat(montant)*SharedPreferencesKeys.montantPerProduct)+"€");
                    mAssociationName.setText(assocName);
                    new DownLoadImageTask(mAssociationLogo).execute(url);
                    mFinishButton.setEnabled(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DepositEndActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
