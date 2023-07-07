package com.example.book_app.activities;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FcmNotificationSender {
    private String userFcToken;
    private String title;
    private String body;
    private Context mContext;

    private RequestQueue requestQueue;

    // URL for sending FCM notifications
    private final String postUrl = "https://fcm.googleapis.com/fcm/send";
    // FCM server key
    private final String fcmServerKey="key=AAAASbsVc4s:APA91bHOKugmr2EyNASJttB_U9ROTP-Q2C77cpAwPvTlPiIJH1OWONgk8jUpK8nBuiFlZdlyFDDhDwgT1vdy_KN02MelIFaritoBElN7Y1IY5er5QSdm834o115ZkqWvYbemZYAIufOb";
 // Replace with your FCM server key

    public FcmNotificationSender(String userFcToken, String title, String body, Context mContext) {
        this.userFcToken = userFcToken;
        this.title = title;
        this.body = body;
        this.mContext = mContext;
    }

    public void sendNotifications() {
        // Initialize the request queue
        requestQueue = Volley.newRequestQueue(mContext);

        // Create the JSON object for the FCM notification
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to", userFcToken);
            JSONObject notiObj = new JSONObject();
            notiObj.put("title", title);
            notiObj.put("body", body);
            mainObj.put("notification", notiObj);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Create the JSON object request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("notirespo", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("notirespo", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", fcmServerKey);
                return headers;
            }
        };

        // Add the request to the request queue
        requestQueue.add(request);
    }
}
