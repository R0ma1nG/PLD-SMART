package com.h4413.recyclyon.Activities.ScanPackaging;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;
import com.h4413.recyclyon.Model.ScannedProduct;
import com.h4413.recyclyon.R;
import com.h4413.recyclyon.Utilities.DownLoadImageTask;
import com.h4413.recyclyon.Utilities.HttpClient;
import com.h4413.recyclyon.Utilities.NavbarInitializer;
import com.h4413.recyclyon.Utilities.Routes;

import org.json.JSONException;
import org.json.JSONObject;

public class PackagingInfoActivity extends AppCompatActivity {

    private Barcode mBarcode;

    private TextView mBarCodeText;
    private Button mScanAnotherButton;
    private ImageView mProductImage;
    private TextView mTestTextView;

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
        mTestTextView = (TextView) findViewById(R.id.packaging_info_test_textview);
        mScanAnotherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PackagingInfoActivity.this, ScanPackagingActivity.class);
                startActivity(intent);
                setResult(RESULT_OK);
                finish();
            }
        });
        HttpClient.GET(Routes.Barcode, mBarcode.displayValue, this, new HttpClient.OnResponseCallback() {
            @Override
            public void onJSONResponse(int statusCode, JSONObject response) {
                if(statusCode==404) {
                    mBarCodeText.setText(R.string.packaging_info_product_not_found);
                    mProductImage.setImageResource(R.drawable.product_not_found);
                    return;
                }
                try {
                    response = (JSONObject) response.get("product");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                ScannedProduct product = gson.fromJson(response.toString(), ScannedProduct.class);
                new DownLoadImageTask(mProductImage).execute(product.image);
                mBarCodeText.setText(product.nom);
                mTestTextView.setText(product.emballage.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         setResult(RESULT_OK);
         finish();
    }
}
