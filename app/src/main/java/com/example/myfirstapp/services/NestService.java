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

    // the default value is only used for testing, it should be null for demo
    private String nestAuthCode = "c.gRNFQ66P3tnk4pIuRKvUiaFDqfQZTAoEzTtIQ3Xga6aS5VvLDyAhGoYsdqo5Vk8FHoQCUgbIf3icZFE4vgKiJncDwLJl7GiS5YbibBpp53nPTQs6qlc0balfPoxXf2KRlk7MKVllnYNMTgr3";
    private final List<String> nestThermostatIDs = new ArrayList<>();

    private interface JSONCallback {
        void onResponse(JSONObject reponse);
    }

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
                setHVACMode(null, "heat");
                updateTemperature(2);
                break;
            case "cool":
                setHVACMode(null, "cool");
                updateTemperature(-2);
                break;
            case "heat mode":
                setHVACMode(null, "heat");
                break;
            case "cool mode":
                setHVACMode(null, "cool");
                break;
            case "heat and cool":
                setHVACMode(null, "heat-cool");
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
            JSONObject data = new JSONObject()
                    .put("hvac_mode", mode);
            writeToThermostat(null, data);
        } catch (JSONException e) {
            System.err.println(e.getMessage());
        }
    }

    private void updateTemperature(final double degrees) {
        readFromThermostat(null, new JSONCallback() {
            @Override
            public void onResponse(JSONObject reponse) {
                try {
                    double temp = reponse.getDouble("target_temperature_c");
                    JSONObject data = new JSONObject()
                            .put("target_temperature_c", temp + degrees);
                    writeToThermostat(null, data);
                } catch (JSONException e) {
                    System.err.println(e.getMessage());
                }
            }
        });
    }

    private void readFromThermostat(String url, final JSONCallback callback) {
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .followRedirects(false)
                .followSslRedirects(false)
                .build();
        Request request = new Request.Builder()
                .url(url == null ? NEST_API_URL + "/devices/thermostats/" + nestThermostatIDs.get(0) : url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + nestAuthCode)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.err.println("ERROR FETCHING DATA");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isRedirect()) {
                    System.err.println("REDIRECTED");
                    String redirectUrl = response.header("Location");
                    readFromThermostat(redirectUrl, callback);
                } else {
                    try {
                        JSONObject obj = new JSONObject(response.body().string());
                        callback.onResponse(obj);
                    } catch (JSONException e) {
                        System.err.println(e.getMessage());
                    }
                }
            }
        });
    }

    private void writeToThermostat(String url, final JSONObject data) {
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .followRedirects(false)
                .followSslRedirects(false)
                .build();

        Request request = new Request.Builder()
                .put(RequestBody.create(MediaType.parse("application/json"), data.toString()))
                .url(url == null ? NEST_API_URL + "/devices/thermostats/" + nestThermostatIDs.get(0) : url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + nestAuthCode)
                .build();


        System.out.println(data.toString());

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
                    writeToThermostat(redirectUrl, data);
                }
            }
        });
    }
}
//activity.startActivity(newIntent(activity, IFTTTLogin.class));