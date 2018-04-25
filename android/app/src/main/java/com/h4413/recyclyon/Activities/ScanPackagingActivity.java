package com.h4413.recyclyon.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.h4413.recyclyon.Listeners.NavigationItemSelectedListener;
import com.h4413.recyclyon.R;
import com.h4413.recyclyon.Utilities.DrawView;

import java.io.IOException;

public class ScanPackagingActivity extends AppCompatActivity {

    private SurfaceView mCameraView;
    private TextView mBarCodeText;

    private CameraSource mCameraSource;
    private BarcodeDetector mBarCodeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_packaging);
        initNavigationMenu();

        mCameraView = (SurfaceView) findViewById(R.id.scan_packaging_surface_view);
        mBarCodeText = (TextView) findViewById(R.id.scan_packaging_barcode_info);

        mBarCodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.EAN_13).build();
        mCameraSource = new CameraSource.Builder(this, mBarCodeDetector).setAutoFocusEnabled(true).build();

        mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ContextCompat.checkSelfPermission(ScanPackagingActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                        //ask for authorisation
                        ActivityCompat.requestPermissions(ScanPackagingActivity.this, new String[]{Manifest.permission.CAMERA}, 50);
                    else
                        mCameraSource.start(mCameraView.getHolder());
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
                Frame.Metadata meta = detections.getFrameMetadata();
                if (barcodes.size() != 0) {
                    mBarCodeText.post(new Runnable() {
                        @Override
                        public void run() {
                            mBarCodeText.setText(barcodes.valueAt(0).displayValue);
                            mCameraSource.stop();
                            mBarCodeDetector.release();
                            /*DrawView drawView = new DrawView(ScanPackagingActivity.this, barcodes.valueAt(0).getBoundingBox());
                            drawView.setBackgroundColor(Color.TRANSPARENT);
                            setContentView(drawView);*/
                        }
                    });
                }
            }
        });
    }

    private void initNavigationMenu() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.template_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationItemSelectedListener(this));
        navigationView.setCheckedItem(R.id.nav_scan);
    }
}
