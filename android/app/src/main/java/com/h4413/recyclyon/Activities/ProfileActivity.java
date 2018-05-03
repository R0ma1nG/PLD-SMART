package com.h4413.recyclyon.Activities;

import android.app.DatePickerDialog;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.h4413.recyclyon.Activities.Connection.CGUActivity;
import com.h4413.recyclyon.Activities.Connection.ChooseAssociationActivity;
import com.h4413.recyclyon.Activities.Connection.InscriptionActivity;
import com.h4413.recyclyon.Adapters.ChooseAssociationRecyclerViewAdapter;
import com.h4413.recyclyon.Listeners.NavigationItemSelectedListener;
import com.h4413.recyclyon.Model.Association;
import com.h4413.recyclyon.Model.AssociationList;
import com.h4413.recyclyon.Model.User;
import com.h4413.recyclyon.R;
import com.h4413.recyclyon.Services.UserServices;
import com.h4413.recyclyon.Utilities.HttpClient;
import com.h4413.recyclyon.Utilities.NavbarInitializer;
import com.h4413.recyclyon.Utilities.Routes;
import com.h4413.recyclyon.Utilities.SharedPreferencesKeys;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    private Spinner mSexInput;
    private EditText mAdressInput;
    private EditText mNameInput;
    private EditText mDateNaissanceInput;
    private TextView mAssociation;
    private Calendar myCalendar;

    private static final int REQUEST_CODE_ASSOCIATION = 1;

    private Button mChangeButton;
    private Button mCancelButton;
    private Button mSubmitButton;
    private ImageButton mAssociationChangeButton;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        NavbarInitializer.initNavigationMenu(this, R.id.nav_account, R.string.title_activity_account);


        mUser = new User();

        mSubmitButton = (Button) findViewById(R.id.profile_activity_submit_btn);
        mCancelButton = (Button)findViewById(R.id.profile_activity_cancel_btn);
        mSexInput = (Spinner) findViewById(R.id.profile_activity_gender_input);
        mNameInput=(EditText) findViewById(R.id.profile_activity_name_input);
        mAdressInput = (EditText) findViewById(R.id.profile_activity_adress_input);
        mDateNaissanceInput = (EditText) findViewById(R.id.profile_activity_date_input);
        mChangeButton = (Button) findViewById(R.id.profile_activity_modification_btn);
        mAssociationChangeButton=(ImageButton) findViewById(R.id.profile_activity_btn_change_association);
        mAssociation= (TextView)findViewById(R.id.profile_activity_text_chosen_association);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.inscriptionSexeChoices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSexInput.setAdapter(adapter);

        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        mUser = UserServices.getCurrentUserFromSharedPreferences(this);
        mAdressInput.setText(mUser.adresse);
        if(mUser.dateNaissance != null) {
            SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy");
            String strDt = simpleDate.format(mUser.dateNaissance);
            mDateNaissanceInput.setText(strDt);
        }
        mSexInput.setSelection(mUser.sexe + 1);
        mNameInput.setText(mUser.nom);

        HttpClient.GET(Routes.Associations, mUser.idAssoc, ProfileActivity.this, new HttpClient.OnResponseCallback() {
            @Override
            public void onJSONResponse(int statusCode, JSONObject response) {
                Gson gson = new Gson();
                Association association = gson.fromJson(String.valueOf(response), Association.class);
                mAssociation.setText(association.nom);
            }
        });
        
        //Verouille les champs sans le click sur modifier
        mSexInput.setEnabled(false);
        mNameInput.setEnabled(false);
        mAdressInput.setEnabled(false);
        mDateNaissanceInput.setEnabled(false);

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disableInputs();
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUser.adresse = mAdressInput.getText().toString();
                mUser.nom=mNameInput.getText().toString();
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
                mUser.dateNaissance = date;
                mUser.sexe = mSexInput.getSelectedItemPosition()-1;
                JSONObject obj = new JSONObject();
                try {
                    obj.put("mail", mUser.mail);
                    obj.put("motDePasse", mUser.motDePasse);
                    obj.put("nom", mUser.nom);
                    obj.put("adresse", mUser.adresse);
                    obj.put("dateNaissance", mUser.dateNaissance);
                    obj.put("sexe", mUser.sexe);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                HttpClient.PUT(Routes.Users, mUser._id, obj.toString(), ProfileActivity.this, new HttpClient.OnResponseCallback() {
                    @Override
                    public void onJSONResponse(int statusCode, JSONObject response) {
                        if(statusCode == 200) {
                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            sharedPref.edit().putString(SharedPreferencesKeys.USER_KEY, response.toString()).apply();
                        } else {
                            Toast.makeText(getApplicationContext(), "Erreur dans le changement de votre profil", Toast.LENGTH_LONG).show();
                        }
                        disableInputs();
                    }
                });
            }
        });

        mAssociationChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ChangeAssociationActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ASSOCIATION);
            }
        });

        mChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNameInput.setEnabled(true);
                mSexInput.setEnabled(true);
                mAdressInput.setEnabled(true);

                mDateNaissanceInput.setEnabled(true);
                mDateNaissanceInput.setInputType(InputType.TYPE_NULL);
                mDateNaissanceInput.setClickable(true);
                mDateNaissanceInput.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(ProfileActivity.this , date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

                findViewById(R.id.profile_activity_association_layout).setVisibility(View.GONE);
                mChangeButton.setVisibility(View.GONE);
                findViewById(R.id.profile_activity_modification_layout).setVisibility(View.VISIBLE);
            }
        });


    }

    private void disableInputs() {
        mNameInput.setEnabled(false);
        mSexInput.setEnabled(false);
        mAdressInput.setEnabled(false);
        mDateNaissanceInput.setEnabled(false);

        findViewById(R.id.profile_activity_association_layout).setVisibility(View.VISIBLE);
        mChangeButton.setVisibility(View.VISIBLE);
        findViewById(R.id.profile_activity_modification_layout).setVisibility(View.GONE);

        mAdressInput.setText(mUser.adresse);
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");
        String strDt = simpleDate.format(mUser.dateNaissance);
        mDateNaissanceInput.setText(strDt);
        mSexInput.setSelection(mUser.sexe+1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mUser = UserServices.getCurrentUserFromSharedPreferences(this);
        HttpClient.GET(Routes.Associations, mUser.idAssoc, ProfileActivity.this, new HttpClient.OnResponseCallback() {
            @Override
            public void onJSONResponse(int statusCode, JSONObject response) {
                Gson gson = new Gson();
                Association association = gson.fromJson(String.valueOf(response), Association.class);
                mAssociation.setText(association.nom);
            }
        });
    }

    private void updateLabel(){
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);

        mDateNaissanceInput.setText(sdf.format(myCalendar.getTime()));
    }
}
