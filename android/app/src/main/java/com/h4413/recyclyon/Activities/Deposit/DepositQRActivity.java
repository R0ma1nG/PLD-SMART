package com.h4413.recyclyon.Activities.Deposit;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;
import com.h4413.recyclyon.Activities.Connection.ChooseAssociationActivity;
import com.h4413.recyclyon.Activities.Connection.InscriptionActivity;
import com.h4413.recyclyon.Activities.HomeActivity;
import com.h4413.recyclyon.Activities.ScanPackaging.PackagingInfoActivity;
import com.h4413.recyclyon.Activities.ScanPackaging.ScanPackagingActivity;
import com.h4413.recyclyon.Listeners.NavigationItemSelectedListener;
import com.h4413.recyclyon.Model.Association;
import com.h4413.recyclyon.Model.User;
import com.h4413.recyclyon.R;
import com.h4413.recyclyon.Utilities.HttpClient;
import com.h4413.recyclyon.Utilities.NavbarInitializer;
import com.h4413.recyclyon.Utilities.Routes;
import com.h4413.recyclyon.Utilities.SharedPreferencesKeys;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class DepositQRActivity extends AppCompatActivity {

    private final static int REQUEST_CODE_QR = 1;
    private final static int REQUEST_CODE_MANUAL = 2;

    private SurfaceView mCameraView;
    private Button mManualInputButton;

    private CameraSource mCameraSource;
    private BarcodeDetector mBarCodeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_qr);

        NavbarInitializer.initNavigationMenu(this, R.id.nav_deposit, R.string.title_deposit);

        mCameraView = (SurfaceView) findViewById(R.id.deposit_qr_surface_view);
        mManualInputButton = (Button) findViewById(R.id.deposit_qr_manual_button);

        mBarCodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        mCameraSource = new CameraSource.Builder(this, mBarCodeDetector).setAutoFocusEnabled(true).build();

        initCamera();

        mManualInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DepositQRActivity.this, DepositManualActivity.class);
                startActivityForResult(intent, REQUEST_CODE_MANUAL);
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 50) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            try {
                mCameraSource.start(mCameraView.getHolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initCamera() {
        mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ContextCompat.checkSelfPermission(DepositQRActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        //ask for authorisation
                        ActivityCompat.requestPermissions(DepositQRActivity.this, new String[]{Manifest.permission.CAMERA}, 50);
                    } else {
                        mCameraSource.start(mCameraView.getHolder());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mCameraSource.stop();
            }
        });

        mBarCodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //mBarCodeDetector.release();
                            if(barcodes.valueAt(0).displayValue.matches("^[a-zA-Z0-9_]*$")) {
                                mCameraSource.stop();
                                Intent intent = new Intent(DepositQRActivity.this, DepositInProgressActivity.class);
                                intent.putExtra("QRCode", barcodes.valueAt(0));
                                startActivityForResult(intent, REQUEST_CODE_QR);
                            } else {
                                //Toast.makeText(getApplicationContext(), "Ce n'est pas un QRCode valide", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
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
