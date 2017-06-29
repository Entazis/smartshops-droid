package me.dm7.barcodescanner.zbar.sample;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;


/**
 * Created by Entazis on 2017. 06. 05..
 */

public class SimpleReaderActivity extends BaseScannerActivity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;

    //FIXME
    private Product mProduct;
    private ArrayList<Product> mProductList = new ArrayList<Product>();;
    private ArrayList<String> mProductListString = new ArrayList<String>();

    private String mProductName;
    private String mProductBrand;
    private String mProductType;
    private String mBarcode;
    private String mBarcodeFormat;
    private String mProductPrice;
    private int mProductAmount;

    private static final String FLASH_STATE = "FLASH_STATE";
    private boolean mFlash;
    JsonObjectRequest getRequest;
    StringRequest stringRequest;
    RequestQueue queue;
    String url;
    TextView productListView;


    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_scaling_scanner);
        setupToolbar();
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZBarScannerView(this);
        contentFrame.addView(mScannerView);

        productListView = (TextView) findViewById(R.id.product_list);

        queue = Volley.newRequestQueue(this);
        url = "http://elodani.tk:5000/demo";

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

        mBarcode = rawResult.getContents();
        mBarcodeFormat = rawResult.getBarcodeFormat().toString();
        url = "http://elodani.tk:5000/scan/annakrisz/" + mBarcode;

        getRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        mProduct = new Product();
                        mProduct.setAmount();
                        mProduct.setBarcode(response.optString("barcode"));
                        mProduct.setProductName(response.optString("name"));
                        mProduct.setProductBrand(response.optString("brand"));
                        mProduct.setProductType(response.optString("type"));
                        mProduct.setProductPrice(response.optString("cost"));
                        //mProductList.add(mProduct);
                        Toast.makeText(SimpleReaderActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("Response", response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(SimpleReaderActivity.this, "Request did not work!", Toast.LENGTH_SHORT).show();
                        Log.d("Error.Response", error.getMessage());
                    }
                });
        queue.add(getRequest);

        //FIXME: refresh listview

        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(SimpleReaderActivity.this);
            }
        }, 2000);
    }

    public void toggleFlash(View v) {
        mFlash = !mFlash;
        mScannerView.setFlash(mFlash);
    }

    public void clearReader(View v){
        mProductList = null;
    }

    public void refreshListview(){
        if(mProductList.isEmpty()){
            //productListView.setText();
        }
        else{
            //productListView.append();
        }

    }
}
