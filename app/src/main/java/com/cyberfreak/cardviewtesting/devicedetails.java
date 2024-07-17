package com.cyberfreak.cardviewtesting;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class devicedetails extends AppCompatActivity {
    private TextView textView;
    private ProgressBar loadingPB;
    private TextView userdetails,data,refresh1;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devicedetails);
        String id="null";
        Intent intent = getIntent();
        id = intent.getStringExtra("message_key");
        String test=id;
        GetUserDetails(test);
        refresh1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetUserDetails(test);
            }
        });
    }
    private void GetUserDetails(String id) {
        // url to post our data
        loadingPB = findViewById(R.id.progressBar);
        String url = "https://api.iplogger.org/logger/visitors/token=api_srDULEjNMdbPsTla9bzdYBjCUYHY7psp";
        loadingPB.setVisibility(View.VISIBLE);
        userdetails=findViewById(R.id.victim_details);
        data=findViewById(R.id.data);
        refresh1=findViewById(R.id.refresh1);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(devicedetails.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {

                // on below line we are displaying a success toast message.
//                Toast.makeText(devicedetails.this, "Data added to API", Toast.LENGTH_SHORT).show();
                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.

                    JSONObject respObj = new JSONObject(response);

                    String Domain2 = respObj.getString("result");
                    String load = respObj.getString("found");
                    if (load.equals("0")) {
                        refresh1.setVisibility(View.GONE);
                        GetUserDetails(id);
                    }
                    else  loadingPB.setVisibility(View.GONE);
                    Domain2=Domain2.substring(1,Domain2.length()-1);
                    JSONObject jsonObject = new JSONObject(Domain2);
                    String useragent=jsonObject.getString("useragent");
                    String platform=jsonObject.getString("platform");
                    String browser=jsonObject.getString("browser");
                    String bversion=jsonObject.getString("bversion");
                    String pversion=jsonObject.getString("pversion");
                    String bot=jsonObject.getString("bot");
                    //on below line we are setting this string s to our text view.
                    userdetails.setVisibility(View.GONE);
                    data.setText("Platform: \t"+platform+"\n\n"+"OS Version: \t"+pversion+"\n\n"+"Browser: \t"+browser+"\n\n"+"Browser Version: \t"+bversion+"\n\n"+"Useragent: \n"+useragent+"\n\n");
                    refresh1.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(devicedetails.this, "Failed to get Response", Toast.LENGTH_SHORT).show();            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("id", id);

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
}