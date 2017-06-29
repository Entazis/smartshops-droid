package me.dm7.barcodescanner.zbar.sample;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Entazis on 2017. 06. 08..
 */

public class AuthenticationActivity extends MainActivity{

    RequestQueue queue;
    StringRequest getRequest;
    String url;
    Button   mButton;
    EditText mName;
    EditText mPassword;
    JsonObjectRequest jsonRequest;
    String name;

    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_authentication);
        setupToolbar();

        queue = Volley.newRequestQueue(this);
        url = "http://elodani.tk:5000/auth/";

        mButton = (Button)findViewById(R.id.button_ok);
        mName   = (EditText)findViewById(R.id.text_name);
        mPassword = (EditText)findViewById(R.id.text_password);

        mButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        Log.v("mName = ", mName.getText().toString());
                        Log.v("mPassword = ", mPassword.getText().toString());

                        name=mName.getText().toString();
                        url=url+"demo";//name;

                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>(){
                                    @Override
                                    public void onResponse(String response){
                                        Toast.makeText(AuthenticationActivity.this, response, Toast.LENGTH_SHORT).show();
                                    }
                                }, new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error){
                                Toast.makeText(AuthenticationActivity.this, "Request did not work!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        queue.add(stringRequest);

                        jsonRequest = new JsonObjectRequest
                                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.v("Response = ", response.toString());
                                    }
                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // TODO Auto-generated method stub
                                        Log.v("Error = ", error.toString());
                                    }
                                });

                        queue.add(jsonRequest);
                    }
                });
    }

    void logHash(){
        Log.i("Eamorr",bin2hex(getHash("asdf")));
    }

    public byte[] getHash(String password){
        MessageDigest digest = null;
        try{
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e1){
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        digest.reset();
        return digest.digest(password.getBytes());
    }

    static String bin2hex(byte[] data){
        return String.format("%0" + (data.length*2) + "X", new BigInteger(1,data));
    }

}
