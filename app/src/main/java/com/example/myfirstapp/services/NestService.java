package com.example.myfirstapp.services;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

    @Override
    public Service execute(String command, Activity activity, ExecCallback callback) {
        if (nestAuthCode == null) {
            callback.onFailure(new Exception("Please login to your Nest account"));
        } else {
            super.execute(command, activity, callback);
        }
        return this;
    }

    @Override
    protected void handleClassifierResponse(String cmd, String cls, Activity activity, ExecCallback callback) {
        switch (cls) {
            case "heat":
                break;
            case "cool":
                break;
            case "heat mode":
                setHVACMode(null, "heat");
                break;
            case "cool mode":
                setHVACMode(null, "cool");
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
    }

    public void authorizeWithPin(final String pin) {
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .build();

        Request request = new Request.Builder()
                .post(new FormBody.Builder()
                        .add("client_id", NEST_PRODUCT_ID)
                        .add("client_secret", NEST_PRODUCT_SECRET)
                        .add("grant_type", "authorization_code")
                        .add("code", pin)
                        .build())
                .url(NEST_OAUTH_URL)
                .build();

        client.newCall(request)
                .enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.err.println(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject obj = new JSONObject(response.body().string());
                    nestAuthCode = obj.get("access_token").toString();
                    fetchThermostatIDs(null);
                } catch (JSONException e) {
                    System.err.println(e.getMessage());
                }
            }
        });
    }

    private void fetchThermostatIDs(final String url) {
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .followRedirects(false)
                .followSslRedirects(false)
                .build();
        Request request = new Request.Builder()
                .url(url == null ? NEST_API_URL + "/devices/thermostats" : url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + nestAuthCode)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.err.println("ERROR FETCHING");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isRedirect()) {
                    System.err.println("REDIRECTED");
                    String redirectUrl = response.header("Location");
                    fetchThermostatIDs(redirectUrl);
                } else {
                    try {
                        JSONObject obj = new JSONObject(response.body().string());
                        nestThermostatIDs.clear();
                        nestThermostatIDs.add(obj.keys().next());
                        System.out.println("NEST THERMOSTAT ID: " + nestThermostatIDs.get(0));
                    } catch (JSONException e) {
                        System.err.println(e.getMessage());
                    }
                }
            }
        });
    }

    private void setHVACMode(String url, final String mode) {
        try {
            OkHttpClient client = new OkHttpClient()
                    .newBuilder()
                    .followRedirects(false)
                    .followSslRedirects(false)
                    .build();

            Request request = new Request.Builder()
                    .put(RequestBody.create(MediaType.parse("application/json"), new JSONObject()
                            .put("hvac_mode", mode)
                            .toString()))
                    .url(url == null ? NEST_API_URL + "/devices/thermostats/" + nestThermostatIDs.get(0) : url)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + nestAuthCode)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.err.println(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isRedirect()) {
                        System.err.println("REDIRECTED");
                        String redirectUrl = response.header("Location");
                        setHVACMode(redirectUrl, mode);
                    }
                }
            });
        } catch (JSONException e) {
            System.err.println(e.getMessage());
        }
    }

}
