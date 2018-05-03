package com.h4413.recyclyon.Activities.Connection;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.h4413.recyclyon.R;
import com.h4413.recyclyon.Services.UserServices;
import com.h4413.recyclyon.Utilities.HttpClient;
import com.h4413.recyclyon.Utilities.NavbarInitializer;
import com.h4413.recyclyon.Utilities.Routes;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPwdActivity extends AppCompatActivity {

    private Button mSubmitButton;
    private EditText mMailInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pwd);

        NavbarInitializer.configureToolbar(this, R.string.appName);

        mSubmitButton = (Button) findViewById(R.id.forgot_pwd_activity_submit_button);
        mMailInput = (EditText) findViewById(R.id.forgot_pwd_activity_mail_input);
        mSubmitButton.setEnabled(false);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO server must send the mail and send ok response if ok
                JSONObject obj = new JSONObject();
                try {
                    obj.put("mail", mMailInput.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                HttpClient.POST(Routes.Forgot, null, obj.toString(), ForgotPwdActivity.this, new HttpClient.OnResponseCallback() {
                    @Override
                    public void onJSONResponse(int statusCode, JSONObject response) {
                        if(statusCode == 200){
                            setResult(RESULT_OK);
                        } else {
                            setResult(RESULT_CANCELED);
                        }
                        finish();
                    }
                });
            }
        });

        mMailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSubmitButton.setEnabled(s.toString().length() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
