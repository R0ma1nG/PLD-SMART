package com.h4413.recyclyon.Activities.Deposit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;
import com.h4413.recyclyon.R;


public class DepositInProgressActivity extends AppCompatActivity {

    private Barcode mQRCode;

    private TextView mQRCodeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_in_progress);
        configureToolbar();

        Intent intent = getIntent();
        mQRCode = intent.getParcelableExtra("QRCode");

        mQRCodeText = (TextView) findViewById(R.id.deposit_in_progress_infos);
        mQRCodeText.setText(mQRCode.displayValue);
    }

    private void configureToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_deposit_in_progress);
        setSupportActionBar(toolbar);
    }
}
