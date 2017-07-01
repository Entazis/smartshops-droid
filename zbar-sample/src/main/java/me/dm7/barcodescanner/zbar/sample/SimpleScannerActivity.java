package me.dm7.barcodescanner.zbar.sample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class SimpleScannerActivity extends BaseScannerActivity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;
    public String mProductName;
    public String mProductBrand;
    public String mProductType;
    public String mBarcode;
    public String mBarcodeFormat;
    public String mProductPrice;
    public Product mProduct;
    public static final String FLASH_STATE = "FLASH_STATE";
    public boolean mFlash;
    LinearLayout layout;
    AlertDialog.Builder builder;
    Toast toast;
    AlertDialog alertproduct;
    RequestQueue queue;
    //StringRequest postRequest;
    JsonObjectRequest postRequest;
    JSONObject postObject;
    String url;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_scaling_scanner);
        //setContentView(R.layout.activity_simple_scanner);
        setupToolbar();
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZBarScannerView(this);
        contentFrame.addView(mScannerView);

        queue = Volley.newRequestQueue(this);
        url = "http://elodani.tk:5000/demo";

        layout = new LinearLayout(SimpleScannerActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText input = new EditText(SimpleScannerActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Product name");
        layout.addView(input);

        final EditText input2 = new EditText(SimpleScannerActivity.this);
        input2.setInputType(InputType.TYPE_CLASS_TEXT);
        input2.setHint("Product brand");
        layout.addView(input2);

        final EditText input3 = new EditText(SimpleScannerActivity.this);
        input3.setInputType(InputType.TYPE_CLASS_TEXT);
        input3.setHint("Product type");
        layout.addView(input3);

        final EditText input4 = new EditText(SimpleScannerActivity.this);
        input4.setInputType(InputType.TYPE_CLASS_NUMBER);
        input4.setHint("Product price");
        layout.addView(input4);

        builder = new AlertDialog.Builder(SimpleScannerActivity.this);
        builder.setTitle("Product Creator");

        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mProductName = input.getText().toString();
                mProductBrand = input2.getText().toString();
                mProductType = input3.getText().toString();
                mProductPrice = input4.getText().toString();
                mProduct = new Product();
                mProduct.setBarcode(mBarcode);
                mProduct.setProductName(mProductName);
                mProduct.setProductBrand(mProductBrand);
                mProduct.setProductType(mProductType);
                mProduct.setProductPrice(mProductPrice);
                mProduct.setAmount();
                //mProduct.setBarcode();
                toast = Toast.makeText(getApplicationContext(),
                        "Name = " + mProduct.getProductName() +
                                ", Brand = " + mProduct.getProductBrand() +
                                ", Type = " + mProduct.getProductType() +
                                ", Barcode = " + mProduct.getBarcode() +
                                ", Price = " + mProduct.getProductPrice(), Toast.LENGTH_LONG);
                toast.show();

                url = "http://elodani.tk:5000/add/annakrisz";

                /*
                postObject = new JSONObject();
                try{
                    postObject.put("name", mProduct.getProductName());
                    postObject.put("brand", mProduct.getProductBrand());
                    postObject.put("type", mProduct.getProductType());
                    postObject.put("price", mProduct.getProductPrice());
                    postObject.put("amount", mProduct.getAmount());
                    postObject.put("barcode", mProduct.getBarcode());
                } catch(JSONException e){
                    e.printStackTrace();
                }
                */
/*
                postRequest = new JsonObjectRequest(Request.Method.POST, url, postObject,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response) {
                                // response
                                Toast.makeText(SimpleScannerActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                                Log.d("Response", response.toString());
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Toast.makeText(SimpleScannerActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                Log.d("Error.Response", error.getMessage());
                            }
                        }
                );
                queue.add(postRequest);*/

                StringRequest postStringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("Response", response);
                                Toast.makeText(SimpleScannerActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Toast.makeText(SimpleScannerActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                Log.d("Error.Response", error.getMessage());
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String>  params = new HashMap<String, String>();

                        params.put("barcode", mProduct.getBarcode());
                        params.put("name", mProduct.getProductName());
                        params.put("brand", mProduct.getProductBrand());
                        params.put("cost", mProduct.getProductPrice());
                        params.put("type", mProduct.getProductType());
                        params.put("user", "1");

                        return params;
                    }
                };
                queue.add(postStringRequest);
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
