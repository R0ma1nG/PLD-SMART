package com.h4413.recyclyon.Connection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.h4413.recyclyon.HomeActivity;
import com.h4413.recyclyon.Model.User;
import com.h4413.recyclyon.R;

public class ConnectionActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_INSCRIPTION = 1;
    private static final int REQUEST_CODE_FORGOT_PWD = 2;
    private static final int REQUEST_CODE_HOME = 3;

    private EditText mMailInput;
    private EditText mPwdInput;
    private Button mConnectionButton;
    private TextView mForgotPwdText;
    private TextView mInscriptionText;

    private TextWatcher mInputListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mConnectionButton.setEnabled(mMailInput.getText().length() != 0 && mPwdInput.getText().length() != 0);
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        mMailInput = (EditText) findViewById(R.id.connection_activity_mail_input);
        mPwdInput = (EditText) findViewById(R.id.connection_activity_password_input);
        mConnectionButton = (Button) findViewById(R.id.connection_activity_connection_btn);
        mForgotPwdText = (TextView) findViewById(R.id.connection_activity_forgotpwd_txt);
        mInscriptionText = (TextView) findViewById(R.id.connection_activity_inscription_txt);

        mConnectionButton.setEnabled(false);
        mConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Connection, need to be implemented.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ConnectionActivity.this, HomeActivity.class);
                startActivityForResult(intent, REQUEST_CODE_HOME);
            }
        });
        mInscriptionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnectionActivity.this, InscriptionActivity.class);
                startActivityForResult(intent, REQUEST_CODE_INSCRIPTION);
            }
        });
        mForgotPwdText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnectionActivity.this, ForgotPwdActivity.class);
                startActivityForResult(intent, REQUEST_CODE_FORGOT_PWD);
            }
        });
        mMailInput.addTextChangedListener(mInputListener);
        mPwdInput.addTextChangedListener(mInputListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_INSCRIPTION) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Inscription réussie", Toast.LENGTH_SHORT).show();
                User utilisateur = (User) data.getSerializableExtra(InscriptionActivity.SP_KEY_USER);
                mMailInput.setText(utilisateur.mail);
            } else {
                Toast.makeText(getApplicationContext(), "Echec de l'inscription", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == REQUEST_CODE_FORGOT_PWD)
        {
            if(resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Mail envoyé.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Echec de l'envoi d'un mail.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
