package com.h4413.recyclyon.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.h4413.recyclyon.Activities.Connection.ConnectionActivity;
import com.h4413.recyclyon.Model.User;
import com.h4413.recyclyon.R;
import com.h4413.recyclyon.Utilities.HttpClient;
import com.h4413.recyclyon.Utilities.NavbarInitializer;
import com.h4413.recyclyon.Utilities.Routes;
import com.h4413.recyclyon.Utilities.SharedPreferencesKeys;

import org.json.JSONObject;

public class SettingsActivity extends AppCompatActivity {

    private Button mDeleteAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        NavbarInitializer.initNavigationMenu(this, R.id.nav_settings, R.string.nav_settings);

        Gson gson = new Gson();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String str = sharedPref.getString(SharedPreferencesKeys.USER_KEY, "");
        final User user = gson.fromJson(str, User.class);
        mDeleteAccount = (Button) findViewById(R.id.settings_delete_btn);
        mDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(SettingsActivity.this).create();
                alertDialog.setTitle(R.string.settings_delete_dialog_title);
                String mes = getString(R.string.settings_delete_dialog_text);
                alertDialog.setMessage(mes);
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Annuler",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Valider", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HttpClient.DELETE(Routes.Users, user._id, SettingsActivity.this, new HttpClient.OnResponseCallback() {
                            @Override
                            public void onJSONResponse(int statusCode, JSONObject response) {
                                Intent intent = new Intent(SettingsActivity.this, ConnectionActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        });
                    }
                });
                alertDialog.show();
            }
        });
    }
}
