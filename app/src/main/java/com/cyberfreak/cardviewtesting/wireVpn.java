package com.cyberfreak.cardviewtesting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class wireVpn extends AppCompatActivity {
private Button button1,button2,button3,button4,button5,button6;
TextView config;
ImageView img;
    private static final String PREFS_NAME = "NewPref";
    private static final String KEY_POLICY_ACCEPTED = "policy_accepted";
private CardView cardView;
    String tunnel_link=null;
    String res = null;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wire_vpn);
        button2=findViewById(R.id.button6);
        button3=findViewById(R.id.button7);
        button4=findViewById(R.id.button5);
        cardView=findViewById(R.id.card1);
        config=findViewById(R.id.gen_config);
        img=findViewById(R.id.imageView7);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(
                        "https://play.google.com/store/apps/details?id=com.wireguard.android"));
                intent.setPackage("com.android.vending");
                startActivity(intent);
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/H6m-5DiqkeE"));
                    wireVpn.this.startActivity(webIntent);
            }
        });

        Dialog dialog = new Dialog(wireVpn.this);   // Start Attack Dialog
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Dialog dialog1 = new Dialog(wireVpn.this); // Genrate Config Dialog
        dialog1.setContentView(R.layout.note);
        dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Customize dialog properties
        dialog.setCancelable(true);
        Button Cancel = dialog.findViewById(R.id.button8);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); return;
            }
        });

        dialog1.setCancelable(true); //  Cancel generate config
        Button Cancel1 = dialog1.findViewById(R.id.button8);
        Cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

        Button Yes=dialog.findViewById(R.id.button9);
        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAct1();
                dialog.dismiss();
            }
        });


        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.show();
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                prefs.edit().putBoolean(KEY_POLICY_ACCEPTED, true).apply();

                Button Yes1=dialog1.findViewById(R.id.button9);
                Yes1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "https://tunnel.pyjam.as/8080";
                        String line1="DNS = 8.8.8.8";
                        String line2="ListenPort = 8080";
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        tunnel_link=extractWebAddress(response);   //   Extract Web Addres
                                        String lineToComment = "PostUp";

                                        StringBuilder outputStringBuilder = new StringBuilder();

                                        try (BufferedReader reader = new BufferedReader(new StringReader(response))) {
                                            String line;
                                            while ((line = reader.readLine()) != null) {
                                                if (line.trim().startsWith(lineToComment)) {
                                                    outputStringBuilder.append(line1).append(System.lineSeparator());
                                                    outputStringBuilder.append(line2).append(System.lineSeparator());
                                                }
                                                if (line.trim().startsWith(lineToComment)) {
                                                    line = "#" + line;  // Comment out the line by adding a '#' at the beginning
                                                }
                                                outputStringBuilder.append(line).append(System.lineSeparator());
                                            }
                                            String outputString = outputStringBuilder.toString();
                                            res=outputString;
                                            tunnel_link=extractWebAddress(res);
                                            Context context = getApplicationContext();
                                            saveStringToFile(context,"link.txt",tunnel_link);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        String fileName = "vpn.conf";
                                        String content = res;

                                        ContentResolver resolver = getContentResolver();
                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
                                        contentValues.put(MediaStore.Downloads.MIME_TYPE, "application/octet-stream");

                                        Uri downloadsUri = null;
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                            downloadsUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
                                        }

                                        Cursor cursor = resolver.query(downloadsUri, null, MediaStore.Downloads.DISPLAY_NAME + "=?", new String[]{fileName}, null);

                                        if (cursor != null && cursor.moveToFirst()) {
                                            // File with the same name exists, delete it
                                            @SuppressLint("Range") long fileId = cursor.getLong(cursor.getColumnIndex(MediaStore.Downloads._ID));
                                            Uri fileUri = ContentUris.withAppendedId(downloadsUri, fileId);
                                            resolver.delete(fileUri, null, null);
                                            cursor.close();
                                        }

                                        String completeFileName = fileName;
                                        contentValues.put(MediaStore.Downloads.DISPLAY_NAME, completeFileName);
                                        Uri uri = resolver.insert(downloadsUri, contentValues);

                                        try (OutputStream outputStream = resolver.openOutputStream(uri)) {
                                            if (outputStream != null) {
                                                Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                                                writer.write(content);
                                                writer.close();
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                error -> {
                                    // Handle the error case
                                    error.printStackTrace();
                                });
// Add the request to the request queue to initiate the HTTP request
                        requestQueue.add(stringRequest);
                        Toast.makeText(wireVpn.this, "File saved to Downloads", Toast.LENGTH_SHORT).show();

                        dialog1.dismiss();
                    }
                });
            }
        });
    }
    private static String extractWebAddress(String inputString) {
        String regex = "https://[\\w.-]+(/[\\w.-]*)*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inputString);

        if (matcher.find()) {
            return matcher.group(0);
        } else {
            return null;  // Web address not found
        }
    }
    public static boolean saveStringToFile(Context context, String fileName, String content) {
        try {
            FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void openAct1(){
        Intent intent1 = new Intent(wireVpn.this, location_data_main.class);
        intent1.putExtra("message_key",tunnel_link);
        startActivity(intent1);
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    @Override
    protected void onResume() {
        super.onResume();

        // Check SharedPreferences for policy acceptance
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean policyAccepted = prefs.getBoolean(KEY_POLICY_ACCEPTED, false);

        // If policy is accepted, go directly to the main activity
        if (policyAccepted) {
            button2.setBackgroundColor(androidx.cardview.R.color.cardview_dark_background);
            config.setText("Open WireGuard App and import Vpn.conf from Downloads(if not done).Make sure it is turned On.");
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(wireVpn.this, "Already Generated", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}