package com.cyberfreak.cardviewtesting;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
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

public class user_details extends AppCompatActivity {
    private ProgressBar loadingPB;
    private TextView userdetails,data,refresh;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        loadingPB=findViewById(R.id.progressBar);
        String id="null";
    Intent intent = getIntent();
      id = intent.getStringExtra("message_key");
        String test=id;
        loadingPB.setVisibility(View.VISIBLE);
        GetUserDetails(test);
        refresh.setOnClickListener(new View.OnClickListener() {
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
        refresh=findViewById(R.id.refresh);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(user_details.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
         @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.

                    JSONObject respObj = new JSONObject(response);

                    String Domain2 = respObj.getString("result");
                    String load = respObj.getString("found");
                    if (load.equals("0")) {
                        refresh.setVisibility(View.GONE);
                        GetUserDetails(id);
                    }
                    else  loadingPB.setVisibility(View.GONE);
                    Domain2=Domain2.substring(1,Domain2.length()-1);
                    JSONObject jsonObject = new JSONObject(Domain2);
                    String ip=jsonObject.getString("ip");
                     String platform=jsonObject.getString("platform");
                     String browser=jsonObject.getString("browser");
                     String Country=jsonObject.getString("country");
                     String State=jsonObject.getString("state");
                     String city=jsonObject.getString("city");
                     String isp=jsonObject.getString("isp");
                    //on below line we are setting this string s to our text view.
                    userdetails.setVisibility(View.GONE);
                    data.setText("Ip: \t"+ip+"\n\n"+"Platform: \t"+platform+"\n\n"+"Browser: \t"+browser+"\n\n"+"Country: \t"+Country+"\n\n"+"State: \t"+State+"\n\n"+"City: \t"+city+"\n\n"+"ISP: \t"+isp+"\n\n");
                    refresh.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(user_details.this, "Failed to get Response", Toast.LENGTH_SHORT).show();            }
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