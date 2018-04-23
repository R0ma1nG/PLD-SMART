package com.h4413.recyclyon.Connection;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.h4413.recyclyon.R;

public class InscriptionActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CHOOSE_ASSOC = 1;

    private Button mSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        configureToolbar();

        mSubmitButton = (Button) findViewById(R.id.inscription_activity_submit_btn);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InscriptionActivity.this, ChooseAssociationActivity.class);
                startActivityForResult(intent, REQUEST_CODE_CHOOSE_ASSOC);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_CHOOSE_ASSOC && resultCode == RESULT_OK)
        {
            // TODO Inscription dans la BDD

            // TODO avant le finish, mettre l'id du user dans un intent pour l'autocomplétion des champs dans l'accueil.
            setResult(RESULT_OK);
            finish();
        }
    }

    private void configureToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }
}
