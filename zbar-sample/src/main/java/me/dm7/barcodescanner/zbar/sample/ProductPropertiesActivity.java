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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Entazis on 2017. 06. 11..
 */

public class ProductPropertiesActivity extends ListActivity{

    LinearLayout layout;
    AlertDialog.Builder builder;

    ArrayList<Product> listItems=new ArrayList<Product>();
    ArrayAdapter<Product> adapter;
    ArrayList<String> listItemsName=new ArrayList<String>();
    ArrayAdapter<String> adapter2;

    JsonObjectRequest getRequest;
    JsonObjectRequest putRequest;
    StringRequest putStringRequest;
    JSONObject putObject;
    JSONArray jArray;
    JSONObject jObject;
    RequestQueue queue;
    String urlget;
    String urlput;
    AlertDialog alertproduct;

    Product mProduct;
    String mProductBarcode;
    int tempint=1;
    String tempstr="Product";

    //RECORDING HOW MANY TIMES THE BUTTON HAS BEEN CLICKED
    int clickCounter=0;

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

        //TODO input fields fill with starting values
        layout = new LinearLayout(ProductPropertiesActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText input = new EditText(ProductPropertiesActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Product name");
        layout.addView(input);

        final EditText input2 = new EditText(ProductPropertiesActivity.this);
        input2.setInputType(InputType.TYPE_CLASS_TEXT);
        input2.setHint("Product brand");
        layout.addView(input2);

        final EditText input3 = new EditText(ProductPropertiesActivity.this);
        input3.setInputType(InputType.TYPE_CLASS_TEXT);
        input3.setHint("Product type");
        layout.addView(input3);

        final EditText input4 = new EditText(ProductPropertiesActivity.this);
        input4.setInputType(InputType.TYPE_CLASS_NUMBER);
        input4.setHint("Product price");
        layout.addView(input4);

        builder = new AlertDialog.Builder(ProductPropertiesActivity.this);
        builder.setTitle("Product Modifier");

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
/*
                putObject = new JSONObject();
                try{
                    putObject.put("name", mProduct.getProductName());
                    putObject.put("brand", mProduct.getProductBrand());
                    putObject.put("type", mProduct.getProductType());
                    putObject.put("price", mProduct.getProductPrice());
                    putObject.put("amount", mProduct.getAmount());
                    putObject.put("barcode", mProduct.getBarcode());
                } catch(JSONException e){
                    e.printStackTrace();
                }

                putRequest = new JsonObjectRequest
                        (Request.Method.PUT, url, putObject, new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response) {
                                // response
                                Log.d("Response", response.toString());
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Log.d("Error.Response", error.getMessage());
                            }
                        }
                );
                queue.add(putRequest);
*/
                putStringRequest = new StringRequest(Request.Method.PUT, urlput,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("Response", response);
                                Toast.makeText(ProductPropertiesActivity.this, response, Toast.LENGTH_SHORT).show();
                                //refresh listview
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
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

        alertproduct = builder.create();

        listView = (ListView)findViewById(android.R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(),
                        "Click ListItem Number " + position, Toast.LENGTH_LONG)
                        .show();
                mProductBarcode = listItems.get(position).getBarcode();
                alertproduct.show();
            }
        });

    }

    public void modifyItem(View v){
        alertproduct.show();
    }

    public void addItems(View v) {
        mProduct = new Product();
        mProduct.setProductName("ProductNr"+tempint);
        mProduct.setProductBrand("ProductBrand");
        mProduct.setProductType("ProductType");
        mProduct.setProductPrice("Price: "+tempint*123);
        listItems.add(mProduct);
        adapter.notifyDataSetChanged();

        tempstr = new String("ProductNr"+ tempint++ + ": " + tempint*123);
        listItemsName.add(tempstr);
        adapter2.notifyDataSetChanged();
    }

    public void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
    }

    public void getProducts(){
        listItems.clear();
        listItemsName.clear();

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
                            listItemsName.add(mProduct.getProductName() + ": " + mProduct.getProductPrice());
                        }
                        Toast.makeText(ProductPropertiesActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
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
}
