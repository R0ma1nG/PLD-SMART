package com.h4413.recyclyon.Activities;

import android.content.DialogInterface;
import android.media.Image;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.h4413.recyclyon.Listeners.NavigationItemSelectedListener;
import com.h4413.recyclyon.R;

public class ProfileActivity extends AppCompatActivity {

    private Spinner SexInput;
    private EditText mAdressInput;
    private EditText mAdressConfirmation;
    private EditText mDateNaissanceInput;

    private Button mChangeButton;
    private Button mDeletteButton;
    private Button mSubmitButton;
    private ImageButton associationChangeButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initNavigationMenu();

        SexInput = (Spinner) findViewById(R.id.profile_activity_gender_input);
        mAdressInput = (EditText) findViewById(R.id.profile_activity_adress_input);
        mDateNaissanceInput = (EditText) findViewById(R.id.profile_activity_date_input);
        mAdressConfirmation = (EditText) findViewById(R.id.profile_activity_optional_adress_input);
        mChangeButton = (Button) findViewById(R.id.profile_activity_modification_btn);
        associationChangeButton=(ImageButton) findViewById(R.id.profile_activity_btn_change_association);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.inscriptionSexeChoices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SexInput.setAdapter(adapter);

        //appeler le service pour obtenir les informations déjà existantes et les
        //inserer avec append
        mAdressInput.append("Information à descendre du serveur");


        //Verouille les champs sans le click sur modifier
        SexInput.setEnabled(false);
        mAdressInput.setEnabled(false);
        mDateNaissanceInput.setEnabled(false);
        mAdressConfirmation.setEnabled(false);

        mChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SexInput.setEnabled(true);
                mAdressInput.setEnabled(true);
                mDateNaissanceInput.setEnabled(true);
                mAdressConfirmation.setEnabled(true);

                mSubmitButton = (Button) findViewById(R.id.profile_activity_submit_btn);
                mDeletteButton = (Button)findViewById(R.id.profile_activity_delete_btn);
                findViewById(R.id.profile_activity_association_layout).setVisibility(View.GONE);
                mChangeButton.setVisibility(View.GONE);
                findViewById(R.id.profile_activity_modification_layout).setVisibility(View.VISIBLE);
                mDeletteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setMessage("Etes-vous sur de vouloir supprimer definitivement votre compte?").setPositiveButton("Oui", dialogClickListener)
                                .setNegativeButton("Non", dialogClickListener).show();

                    }
                });
            }
        });


    }

    public void initNavigationMenu() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.template_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationItemSelectedListener(this));
        navigationView.setCheckedItem(R.id.nav_homepage);
    }
}
