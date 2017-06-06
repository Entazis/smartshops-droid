package me.dm7.barcodescanner.zbar.sample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class SimpleScannerActivity extends BaseScannerActivity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;
    private String mProductName;
    private String mBarcode;
    private String mBarcodeFormat;
    private String mProductPrice;
    private static final String FLASH_STATE = "FLASH_STATE";
    private boolean mFlash;
    LinearLayout layout;
    AlertDialog.Builder builder;
    Toast toast;
    AlertDialog alertproduct;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_scaling_scanner);
        //setContentView(R.layout.activity_simple_scanner);
        setupToolbar();
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZBarScannerView(this);
        contentFrame.addView(mScannerView);

        layout = new LinearLayout(SimpleScannerActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText input = new EditText(SimpleScannerActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Product name");
        layout.addView(input);
        final EditText input2 = new EditText(SimpleScannerActivity.this);
        input2.setInputType(InputType.TYPE_CLASS_NUMBER);
        input2.setHint("Product price");
        layout.addView(input2);

        builder = new AlertDialog.Builder(SimpleScannerActivity.this);
        builder.setTitle("Product Creator");

        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mProductName = input.getText().toString();
                mProductPrice = input2.getText().toString();
                toast = Toast.makeText(getApplicationContext(), "Product = " + mProductName + ", Barcode = " + mBarcode + ", Price = " + mProductPrice, Toast.LENGTH_SHORT);
                toast.show();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertproduct = builder.create();

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
        mScannerView.setFlash(mFlash);
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
    }

    @Override
    public void handleResult(Result rawResult) {

        mScannerView.stopCameraPreview();
        mBarcode = rawResult.getContents();
        mBarcodeFormat = rawResult.getBarcodeFormat().toString();

        alertproduct.show();

        Toast.makeText(this, "Contents = " + mBarcode +
                ", Format = " + mBarcodeFormat, Toast.LENGTH_SHORT).show();

        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(SimpleScannerActivity.this);
            }
        }, 2000);
    }

    public void toggleFlash(View v) {
        mFlash = !mFlash;
        mScannerView.setFlash(mFlash);
    }
}
