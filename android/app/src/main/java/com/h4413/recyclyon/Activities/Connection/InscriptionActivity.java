package com.h4413.recyclyon.Activities.Connection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.h4413.recyclyon.Activities.HomeActivity;
import com.h4413.recyclyon.Model.Association;
import com.h4413.recyclyon.Model.User;
import com.h4413.recyclyon.R;
import com.h4413.recyclyon.Utilities.HttpClient;
import com.h4413.recyclyon.Utilities.Routes;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class InscriptionActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CHOOSE_ASSOC = 1;
    public static final String SP_KEY_USER = "InscriptionUserSave";

    private Button mSubmitButton;

    private EditText mMailInput;
    private EditText mMdpInput;
    private EditText mConfirmMdpInput;
    private Spinner mSexeInput;
    private EditText mDateNaissanceInput;
    private EditText mAdressInput;
    private EditText mNomInput;

    private User mUtilisateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        configureToolbar();

        mSubmitButton = (Button) findViewById(R.id.inscription_activity_submit_btn);
        mMailInput = (EditText) findViewById(R.id.inscription_activity_mail_input);
        mMdpInput = (EditText) findViewById(R.id.inscription_activity_mdp_input);
        mConfirmMdpInput = (EditText) findViewById(R.id.inscription_activity_confirmMdp_input);
        mSexeInput = (Spinner) findViewById(R.id.inscription_activity_sexe_input);
        mDateNaissanceInput = (EditText) findViewById(R.id.inscription_activity_date_naissance_input);
        mAdressInput = (EditText) findViewById(R.id.inscription_activity_adress_input);
        mNomInput = (EditText) findViewById(R.id.inscription_activity_name_input);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.inscriptionSexeChoices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSexeInput.setAdapter(adapter);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEmpty(mMailInput) && !isEmpty(mMdpInput) && !isEmpty(mConfirmMdpInput)) {
                    if(mMdpInput.getText().toString().equals(mConfirmMdpInput.getText().toString())) {
                        Date date = null;
                        if(!mDateNaissanceInput.getText().toString().equals("")) {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            date = new Date();
                            try {
                                date = sdf.parse(mDateNaissanceInput.getText().toString());
                            } catch (ParseException e) {
                                Toast.makeText(getApplicationContext(), "Format de date incorrect : dd/mm/yyyy", Toast.LENGTH_LONG).show();
                            }
                        }

                        mUtilisateur = new User();
                        mUtilisateur.dateNaissance = date;
                        mUtilisateur.nom = mNomInput.getText().toString();
                        mUtilisateur.mail = mMailInput.getText().toString();
                        mUtilisateur.motDePasse = mMdpInput.getText().toString();
                        mUtilisateur.adresse = mAdressInput.getText().toString();
                        mUtilisateur.sexe = mSexeInput.getSelectedItemPosition()-1;

                        Intent intent = new Intent(InscriptionActivity.this, ChooseAssociationActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_CHOOSE_ASSOC);
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.inscriptionWrongPasswords), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.inscriptionAllFieldsMustBeCompleted), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Return", "Inscription result");
        if(requestCode == REQUEST_CODE_CHOOSE_ASSOC && resultCode == RESULT_OK)
        {
            Association association = (Association) data.getSerializableExtra(ChooseAssociationActivity.SP_KEY_ASSOCIATION);
            Gson gson = new Gson();
            SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
            String json = sharedPref.getString(SP_KEY_USER, "");
            mUtilisateur = gson.fromJson(json, User.class);
            mUtilisateur.idAssoc = association.id;

            HttpClient.POST(Routes.Signup, mUtilisateur.toString(), InscriptionActivity.this, new HttpClient.OnResponseCallback() {
                @Override
                public void onJSONResponse(int statusCode, JSONObject response) {
                    if(statusCode == 200) {
                        try {
                            mUtilisateur.idUtilisateur = response.get("idUtilisateur").toString();
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Erreur dans l'analyse des données du serveur.", Toast.LENGTH_LONG).show();
                            setResult(RESULT_CANCELED);
                            finish();
                        }
                        Intent intent = new Intent();
                        intent.putExtra(SP_KEY_USER, mUtilisateur);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Erreur dans l'envoi au serveur.", Toast.LENGTH_LONG).show();
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                }
            });


        }
    }

    @Override
    protected void onStop() {
        if(mUtilisateur != null)
        {
            SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
            Gson gson = new Gson();
            sharedPref.edit().putString(SP_KEY_USER, gson.toJson(mUtilisateur)).commit();
        }
        super.onStop();
    }

    private void configureToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}
