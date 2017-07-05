package me.dm7.barcodescanner.zbar.sample;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Entazis on 2017. 06. 11..
 */

public class ProductPropertiesActivity extends ListActivity{

    LinearLayout layout;
    AlertDialog alertproduct;
    AlertDialog.Builder builder;

    ArrayList<Product> listItems=new ArrayList<Product>();
    ArrayAdapter<Product> adapter;
    ArrayList<String> listItemsName=new ArrayList<String>();
    ArrayAdapter<String> adapter2;

    JsonObjectRequest getRequest;
    JsonObjectRequest putRequest;
    StringRequest putStringRequest;
    JSONArray jArray;
    JSONObject jObject;
    RequestQueue queue;
    String urlget;
    String urlput;

    Product mProduct;
    String mProductBarcode;
    String mProductBrand;
    String mProductName;
    String mProductType;
    String mProductPrice;

    ListView listView;

    @Override
    public void onCreate(Bundle state){
        super.onCreate(state);
        setContentView(R.layout.activity_product_properties);
        setupToolbar();

        adapter=new ArrayAdapter<Product>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        adapter2=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItemsName);

        //setListAdapter(adapter);
        setListAdapter(adapter2);


        queue = Volley.newRequestQueue(this);

        urlget = "http://elodani.tk:5000/scan/annakrisz";
        urlput = "http://elodani.tk:5000/add/annakrisz";

        //Get products from the server
        getProducts();

        layout = new LinearLayout(ProductPropertiesActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText input2 = new EditText(ProductPropertiesActivity.this);
        input2.setInputType(InputType.TYPE_CLASS_TEXT);
        input2.setHint("Product brand");
        layout.addView(input2);

        final EditText input = new EditText(ProductPropertiesActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Product name");
        layout.addView(input);

        final EditText input3 = new EditText(ProductPropertiesActivity.this);
        input3.setInputType(InputType.TYPE_CLASS_TEXT);
        input3.setHint("Product type");
        layout.addView(input3);

        final EditText input4 = new EditText(ProductPropertiesActivity.this);
        input4.setInputType(InputType.TYPE_CLASS_NUMBER);
        input4.setHint("Product price");
        layout.addView(input4);

        builder = new AlertDialog.Builder(ProductPropertiesActivity.this);

        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mProduct.setProductName(input.getText().toString());
                mProduct.setProductBrand(input2.getText().toString());
                mProduct.setProductType(input3.getText().toString());
                mProduct.setProductPrice(input4.getText().toString());
                mProduct.setBarcode(mProductBarcode);
                Toast.makeText(getApplicationContext(),
                        "Name = " + mProduct.getProductName() +
                                ", Brand = " + mProduct.getProductBrand() +
                                ", Type = " + mProduct.getProductType() +
                                ", Barcode = " + mProduct.getBarcode() +
                                ", Price = " + mProduct.getProductPrice(), Toast.LENGTH_LONG).show();

                putStringRequest = new StringRequest(Request.Method.PUT, urlput,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Response", response);
                                Toast.makeText(ProductPropertiesActivity.this, response, Toast.LENGTH_SHORT).show();
                                getProducts();
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ProductPropertiesActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                error.printStackTrace();
                                int ec = error.networkResponse.statusCode;
                                Log.d("Error.Response", error.toString());
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
                queue.add(putStringRequest);

                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        //alertproduct = builder.create();

        listView = (ListView)findViewById(android.R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(),
                        "Click ListItem Number " + position, Toast.LENGTH_LONG)
                        .show();
                mProductBarcode = listItems.get(position).getBarcode();
                mProductBrand = listItems.get(position).getProductBrand();
                mProductName = listItems.get(position). getProductName();
                mProductType = listItems.get(position).getProductType();
                mProductPrice = listItems.get(position).getProductPrice();

                layout.removeAllViewsInLayout();
                layout = new LinearLayout(ProductPropertiesActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                input2.setText(mProductBrand);
                input.setText(mProductName);
                input3.setText(mProductType);
                input4.setText(mProductPrice);

                layout.addView(input2);
                layout.addView(input);
                layout.addView(input3);
                layout.addView(input4);

                builder.setTitle(mProductBrand + " - " + mProductName + " " + mProductType);
                builder.setView(layout);

                alertproduct = builder.create();
                alertproduct.show();
            }
        });

    }

    public void modifyItem(View v){
        alertproduct.show();
    }

    public void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
    }

    public void getProducts(){
        listItems.clear();

        getRequest = new JsonObjectRequest
                (Request.Method.GET, urlget, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        jArray = response.optJSONArray("products");
                        for(int i = 0; i < jArray.length(); i++){
                            jObject = jArray.optJSONObject(i);
                            mProduct = new Product();
                            mProduct.setAmount();
                            mProduct.setBarcode(jObject.optString("barcode"));
                            mProduct.setProductName(jObject.optString("name"));
                            mProduct.setProductBrand(jObject.optString("brand"));
                            mProduct.setProductType(jObject.optString("type"));
                            mProduct.setProductPrice(jObject.optString("cost"));
                            listItems.add(mProduct);
                        }
                        Collections.sort(listItems, new ProductCompare());
                        refreshItemNames();

                        adapter2.notifyDataSetChanged();
                        Log.d("Response", response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Toast.makeText(ProductPropertiesActivity.this, "Request did not work!", Toast.LENGTH_SHORT).show();
                        Log.d("Error.Response", error.getMessage());
                    }
                });
        queue.add(getRequest);
    }

    public void refreshItemNames(){

        listItemsName.clear();
        for(Product product: listItems){
            mProductBrand = product.getProductBrand();
            mProductName = product.getProductName();
            mProductType = product.getProductType();
            mProductPrice = product.getProductPrice();

            listItemsName.add(mProductBrand + " - " + mProductName + " " + mProductType + ": " + mProductPrice + " Ft");
        }

    }
}
