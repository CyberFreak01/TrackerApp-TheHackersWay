package com.cyberfreak.cardviewtesting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class iploggerMain extends AppCompatActivity {
    private TextView responseTV,textView;
    private ProgressBar loadingPB;
    private EditText editText,link;
    private CardView cardView;
    private Button button1;
    private Context context;
    String id=null;
    String shortlink=null;
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String POST_URL ="https://is.gd/create.php?format=simple";
    private static final String iplogger_url = "https://api.iplogger.org/create/shortlink/token=api_E6ZZcI4Fe6cuFUvbd2sYFOjmh10T7Ci9";
    private String POST_PARAMS = "url="+shortlink;
    int i=0;String real_url=null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iplogger_main);
        Button button = findViewById(R.id.button3);
        link=findViewById(R.id.editText);
        responseTV = findViewById(R.id.idTVResponse);
        button1=findViewById(R.id.button2);
        editText=findViewById(R.id.generate);
        button1.setVisibility(View.GONE);
        cardView=findViewById(R.id.card3);
        cardView.setVisibility(View.GONE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inp=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inp.hideSoftInputFromWindow(link.getWindowToken(), 0);
                editText.setText("Generating link...");
                cardView.setVisibility(View.VISIBLE);
            String url=link.getText().toString();
            postDataUsingVolley(url,iplogger_url);
       // Shortener("shortlink");
            responseTV.setBackground(ContextCompat.getDrawable(iploggerMain.this,R.drawable.iplogger_bg));

            }
        });

button1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        openAct1();
//        dialog dialog1 = new dialog();
//        dialog1.show(getFragmentManager(),"dialog");
    }
});


    }

    private void postDataUsingVolley(String link,String url) {
        // url to post our data
        loadingPB = findViewById(R.id.idLoadingPB);
        loadingPB.setVisibility(View.VISIBLE);

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(iploggerMain.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                // inside on response method we are
                // hiding our progress bar
                // and setting data to edit text as empty
                loadingPB.setVisibility(View.GONE);
                if(i==0) real_url=link;
                if(i == 1){
                    String afterHttps = response.substring(response.indexOf("https://") + "https://".length());
                    URL url = null;
                    try {
                        url = new URL(real_url);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    String domain_url = url.getProtocol() + "://" + url.getHost();
                    String temp=domain_url+"-secured-by-google@";
                    String final_uri=temp+afterHttps;
                    responseTV.setText(final_uri);
                }
                i++;

                // on below line we are displaying a success toast message.
                Toast.makeText(iploggerMain.this, "Data added to API", Toast.LENGTH_SHORT).show();
                editText.setText("Send this link to the target user :");
                String shortLink = null;
                try {
                    // on below line we are parsing the response
                    // to json object to extract data from it.

                    JSONObject respObj = new JSONObject(response);
                    shortLink = "null";
                    String Domain2 = respObj.getString("result");
                    // Domain2=Domain2.substring(1,Domain2.length()-1);
                    JSONObject jsonObject = new JSONObject(Domain2);
                    shortLink = jsonObject.getString("shortlink");
                    id = jsonObject.getString("id");
                    shortlink=shortLink;
                    //responseTV.setText(shortLink);
                   if(i==1) postDataUsingVolley(shortLink,POST_URL);
                    button1.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(iploggerMain.this, "Failed to get Response", Toast.LENGTH_SHORT).show();            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
               if(i==1) params.put("url", shortlink);
               if(i==0) params.put("destination",link);

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);

    }

    public void openAct1(){
        Intent intent1 = new Intent(iploggerMain.this, user_details.class);
       intent1.putExtra("message_key", id);
        startActivity(intent1);
    }
}