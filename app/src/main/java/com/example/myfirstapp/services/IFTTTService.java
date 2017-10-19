package com.example.myfirstapp.services;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

    public int executeCommand(String command) {
        if (iftttKey == null) return 1;

        String cls = getCommandClass(command);

        switch (cls) {
            case "on":
                sendAction(cls);
                break;
            case "off":
                sendAction(cls);
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

    private void sendAction(String cls) {
        String action = "turn_" + cls + "_test";
        String action1 = "slack_test";

        try {
            OkHttpClient client = new OkHttpClient()
                    .newBuilder()
                    .build();

            Request request = new Request.Builder()
                    .post(RequestBody.create(MediaType.parse("application/json"), new JSONObject()
                            .put("value1", cls)
                            .put("value2", "")
                            .put("value3", "")
                            .toString()))
                    .url("https://maker.ifttt.com/trigger/" + action + "/with/key/" + iftttKey)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.err.println(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException { }
            });
        } catch (JSONException e) {
            System.err.println(e.getMessage());
        }
    }
}
