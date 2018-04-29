package com.h4413.recyclyon.Activities.ScanPackaging;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;
import com.h4413.recyclyon.Listeners.NavigationItemSelectedListener;
import com.h4413.recyclyon.R;
import com.h4413.recyclyon.Utilities.DownLoadImageTask;
import com.h4413.recyclyon.Utilities.NavbarInitializer;

public class PackagingInfoActivity extends AppCompatActivity {

    private Barcode mBarcode;

    private TextView mBarCodeText;
    private Button mScanAnotherButton;
    private ImageView mProductImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packaging_info);

        NavbarInitializer.initNavigationMenu(this, R.id.nav_scan, R.string.title_scan_packaging);

        Intent intent = getIntent();
        mBarcode = intent.getParcelableExtra("Barcode");
        mBarCodeText = (TextView) findViewById(R.id.packaging_info_barcode);
        mScanAnotherButton = (Button) findViewById(R.id.packaging_info_scan_another_btn);
        mProductImage = (ImageView) findViewById(R.id.packaging_info_product_image);
        mScanAnotherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
        new DownLoadImageTask(mProductImage).execute("https://static.openfoodfacts.org/images/products/317/973/232/6911/front_fr.11.400.jpg");
        if(mBarcode != null) {
            mBarCodeText.setText(mBarcode.displayValue);
        } else {
            mBarCodeText.setText("Erreur dans la lecture du code barre.");
        }
    }
}
