package com.example.myfirstapp.services;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by simonrouse9461 on 10/17/17.
 */

public class IFTTTService extends Service {

    private String iftttKey = null;

    // Singleton Class Construction
    private static final IFTTTService instance = new IFTTTService();
    public static IFTTTService getInstance() {
        return instance;
    }
    private IFTTTService() { }

    public void setIftttKey(String key) {
        this.iftttKey = key;
    }

    public int executeCommand(String command, Context context) {
        if (iftttKey == null) return 1;

        String cls = getCommandClass(command);

        switch (cls) {
            case "on":
                sendAction(cls, context);
                break;
            case "off":
                sendAction(cls, context);
                break;
            default:
                break;
        }
        return 0;
    }

    public String getErrorMessage(int errCode) {
        switch (errCode) {
            case 0:
                return "";
            case 1:
                return "Please connect to IFTTT services";
            default:
                return "Unknown error";
        }
    }

    private void sendAction(String cls, Context context) {
        String action = "turn_" + cls + "_test";
        String action1 = "slack_test";
        String url = "https://maker.ifttt.com/trigger/" + action + "/with/key/" + iftttKey;
        final JSONObject data = new JSONObject();
        try {
            data.put("value1", cls);
            data.put("value2", "");
            data.put("value3", "");
        } catch (JSONException e) {
            System.err.println(e.getMessage());
        }

        JsonObjectRequest request = new JsonObjectRequest(
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

        Volley.newRequestQueue(context).add(request);
    }
}
