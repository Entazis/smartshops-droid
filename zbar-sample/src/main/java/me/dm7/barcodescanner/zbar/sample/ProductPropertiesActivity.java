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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;

    JsonObjectRequest getRequest;
    JsonObjectRequest putRequest;
    RequestQueue queue;
    String url;
    AlertDialog alertproduct;

    Product mProduct;

    //RECORDING HOW MANY TIMES THE BUTTON HAS BEEN CLICKED
    int clickCounter=0;

    ListView listView;

    @Override
    public void onCreate(Bundle state){
        super.onCreate(state);
        setContentView(R.layout.activity_product_properties);
        setupToolbar();

        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        setListAdapter(adapter);

        queue = Volley.newRequestQueue(this);
        url = "http://elodani.tk:5000/demo";

        //TODO get request for the product list
        getRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //TODO adding product elements to listItems
                        Toast.makeText(ProductPropertiesActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
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

        //TODO input fields for the put request, fill with starting values
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
                Toast.makeText(getApplicationContext(),
                        "Name = " + mProduct.getProductName() +
                                ", Brand = " + mProduct.getProductBrand() +
                                ", Type = " + mProduct.getProductType() +
                                ", Barcode = " + mProduct.getBarcode() +
                                ", Price = " + mProduct.getProductPrice(), Toast.LENGTH_LONG).show();

                putRequest = new JsonObjectRequest
                        (Request.Method.PUT, url, null, new Response.Listener<JSONObject>()
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
                ) {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("name", "Alif");
                        params.put("domain", "http://itsalif.info");

                        return params;
                    }
                };
                queue.add(putRequest);

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
            }
        });

    }

    public void modifyItem(View v){
        //TODO dialog: inject clicked product obj, title barcode
        alertproduct.show();
    }

    public void addItems(View v) {
        listItems.add("Clicked : "+clickCounter++);
        adapter.notifyDataSetChanged();
    }

    public void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
    }
}
