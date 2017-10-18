package com.example.myfirstapp.services;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by simonrouse9461 on 10/17/17.
 */

public class NestService extends Service {

    private static final String NEST_PRODUCT_ID = "4be83a4b-8d1b-45f3-a45a-e7e9c1616f25";
    private static final String NEST_PRODUCT_SECRET = "GhRzTb47vBwfXPNXzgYlv81MV";
    private static final String NEST_OAUTH_URL = "https://api.home.nest.com/oauth2/access_token";
    private static final String NEST_API_URL = "https://developer-api.nest.com";
    private static final String NEST_AUTHORIZATION_URL = "https://home.nest.com/login/oauth2?client_id=" + NEST_PRODUCT_ID + "&state=STATE";

    private String nestAuthCode = null;
    private final List<String> nestThermostatIDs = new ArrayList<>();

    // Singleton Class Construction
    private static final NestService instance = new NestService();
    public static NestService getInstance() {
        return instance;
    }
    private NestService() { }

    public List<String> getNestThermostatIDs() {
        return this.nestThermostatIDs;
    }

    public String getNestAuthorizationUrl() {
        return NEST_AUTHORIZATION_URL;
    }

    public int executeCommand(String command, Context context) {
        if (nestAuthCode == null) {
            return 1;
        }

        String cls = getCommandClass(command);

        switch (cls) {
            case "heat":
                break;
            case "cool":
                break;
            case "heat mode":
                break;
            case "cool mode":
                break;
            case "heat and cool":
                break;
            case "off":
                break;
            case "home":
                break;
            case "away":
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
                return "Please login to your Nest account";
            default:
                return "Unknown error";
        }
    }

    public void authorizeWithPin(final String pin, final Context context) {
        StringRequest request = new StringRequest(
                Request.Method.POST, NestService.NEST_OAUTH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            nestAuthCode = obj.get("access_token").toString();
                            fetchThermostatIDs(context);
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
                params.put("client_id", NestService.NEST_PRODUCT_ID);
                params.put("client_secret", NestService.NEST_PRODUCT_SECRET);
                params.put("grant_type", "authorization_code");
                params.put("code", pin);

                return params;
            }
        };

        Volley.newRequestQueue(context).add(request);
    }

    private void fetchThermostatIDs(Context context) {
        StringRequest request = new StringRequest(
                Request.Method.GET, NEST_API_URL + "/devices/thermostats",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            nestThermostatIDs.clear();
                            nestThermostatIDs.add(obj.keys().next());
                            System.out.println("NEST THERMOSTAT ID: " + nestThermostatIDs.get(0));
                        } catch (JSONException e) {
                            System.err.println(e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.err.print("ERROR:");
                        System.err.println(new String(error.networkResponse.data));
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + nestAuthCode);
                return headers;
            }
        };

        try {
            System.out.println(request.getHeaders());
        } catch (AuthFailureError e) {

        }

        Volley.newRequestQueue(context).add(request);
    }
/*
    private void setHVACMode(String mode, Context context) {
        StringRequest request = new StringRequest(
                Request.Method.PUT, NEST_API_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            NestService.getInstance()
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

        Volley.newRequestQueue(context).add(request);
    }
    */

}
