package com.h4413.recyclyon.Activities.Deposit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;
import com.h4413.recyclyon.R;
import com.h4413.recyclyon.Utilities.NavbarInitializer;

public class DepositManualActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_QR = 1;

    private Button mValidateButton;
    private Button mCancelButton;
    private TextView mScanCodeLink;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_manual);
        NavbarInitializer.initNavigationMenu(this, R.id.nav_deposit, R.string.title_deposit);

        mValidateButton = (Button) findViewById(R.id.deposit_manual_validate);
        mCancelButton = (Button) findViewById(R.id.deposit_manual_cancel);
        mScanCodeLink = (TextView) findViewById(R.id.deposit_manual_scan);
        mEditText = (EditText) findViewById(R.id.deposit_manual_edittext);

        mValidateButton.setEnabled(false);
        mCancelButton.setEnabled(false);

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setText("");
            }
        });
        mValidateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DepositManualActivity.this, DepositInProgressActivity.class);
                Barcode barcode = new Barcode();
                barcode.displayValue =  mEditText.getText().toString();
                intent.putExtra("QRCode", barcode);
                startActivityForResult(intent, REQUEST_CODE_QR);
            }
        });
        mScanCodeLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.length() == 23 && s.toString().matches("^[a-zA-Z0-9]*$"))
                {
                    mValidateButton.setEnabled(true);
                    mCancelButton.setEnabled(true);
                } else {
                    mValidateButton.setEnabled(false);
                    mCancelButton.setEnabled(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(resultCode);
        finish();
    }
}
