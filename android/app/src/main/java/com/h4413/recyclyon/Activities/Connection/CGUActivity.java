package com.h4413.recyclyon.Activities.Connection;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.h4413.recyclyon.R;
import com.h4413.recyclyon.Utilities.NavbarInitializer;

public class CGUActivity extends AppCompatActivity {

    private Button mAcceptButton;

    //private Association mAssociation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cgu);

        NavbarInitializer.configureToolbar(this, R.string.appName);

        mAcceptButton = (Button) findViewById(R.id.cgu_activity_accept_btn);
        mAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent();
                //intent.putExtra("Association", mAssociation);
                setResult(RESULT_OK);
                finish();
            }
        });

        /*Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mAssociation = (Association)extras.getSerializable("Association");
        }*/
    }
}
