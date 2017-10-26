package com.example.myfirstapp.services;

import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.discovery.v1.Discovery;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryPassages;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

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

public class Discovery2Service extends Service {

    private String iftttKey = null;
    private static final String username = "e628613c-edc3-460d-9677-a32872e6c6d9";
    private static final String password = "gHp7nZY8RMMO";
    private static final String collection_id = "af74d3aa-e649-4ac0-adad-2ec85fc3c194";
    private static final String environment_id = "9d26d096-4253-4e8d-8c78-efad5043e4e5";
    Discovery discovery;

    // Singleton Class Construction
    private static final Discovery2Service instance = new Discovery2Service();
    public static Discovery2Service getInstance() {
        return instance;
    }
    private Discovery2Service() { }

    public void setIftttKey(String key) {
        this.iftttKey = key;
    }

    @Override
    public Service execute(String command, Activity activity, ExecCallback callback) {
        discovery = new Discovery("2017-09-01");
        discovery.setEndPoint("https://gateway.watsonplatform.net/discovery/api/");
        discovery.setUsernameAndPassword(username, password);
        Boolean passageOption = true;
        QueryOptions queryOption = new QueryOptions.Builder()
                .environmentId(environment_id)
                .collectionId(collection_id)
                .passages(passageOption)
                .naturalLanguageQuery(command)
                .build();

        QueryResponse result = discovery.query(queryOption).execute();
        List<QueryPassages> check = result.getPassages();


        String first = check.get(0).getPassageText();
        callback.onResponse(first);

        return this;
    }

    @Override
    protected void handleClassifierResponse(String cmd, String cls, Activity activity, ExecCallback callback) {

    }


}
