package com.example.myfirstapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NestLogin extends AppCompatActivity {

    private static final String productId = "4be83a4b-8d1b-45f3-a45a-e7e9c1616f25";
    private static final String productSecret = "GhRzTb47vBwfXPNXzgYlv81MV";
    private static final String authUrl = "https://home.nest.com/login/oauth2?client_id=" + productId + "&state=STATE";
    private static final String oauthTokenUrl = "https://api.home.nest.com/oauth2/access_token";

    public EditText pinField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nest_login);

        WebView myWebView = (WebView) findViewById(R.id.nestLoginPage);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl(authUrl);

        pinField = (EditText) findViewById(R.id.pinField);
    }

    public void connectWithPin(View view) {
        final String pin = pinField.getText().toString();

        StringRequest request = new StringRequest(
                Request.Method.POST, oauthTokenUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            AccountAuthorizations
                                    .getInstance()
                                    .setNestAuthCode(
                                            obj.get("access_token").toString()
                                    );
                        } catch (JSONException e) {
                            System.err.println(e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.err.println(error.getMessage());
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("client_id", productId);
                params.put("client_secret", productSecret);
                params.put("grant_type", "authorization_code");
                params.put("code", pin);

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
        finish();
    }
}
