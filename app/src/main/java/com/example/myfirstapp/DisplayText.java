package com.example.myfirstapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class DisplayText extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_text);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.MESSAGE_ID);
        String key = intent.getStringExtra(MainActivity.KET_ID);

        showText(message);
        send2IFTTT(key, message);

    }

    public void showText(String str) {

        TextView classifier = (TextView) findViewById(R.id.classifier);

        classifier.setText(str);

    }

    public void send2IFTTT(String key, String msg) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://maker.ifttt.com/trigger/turn_" + msg + "_test/with/key/" + key;
        final JSONObject data = new JSONObject();
        try {
            data.put("value1", msg);
            data.put("value2", "");
            data.put("value3", "");
        } catch (JSONException e) {
            System.err.println(e.getMessage());
        }

        JsonObjectRequest stringRequest = new JsonObjectRequest(
                Request.Method.POST, url, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );

        queue.add(stringRequest);
    }
}
