package com.example.myfirstapp.Discovery;

import android.support.v7.util.SortedList;

import com.ibm.watson.developer_cloud.discovery.v1.Discovery;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryOptions;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryPassages;
import com.ibm.watson.developer_cloud.discovery.v1.model.QueryResponse;
import com.ibm.watson.developer_cloud.http.RequestBuilder;
import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.service.WatsonService;
import com.ibm.watson.developer_cloud.util.ResponseConverterUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by williamHoo on 10/18/17.
 */

public class DiscoveryService {


    private static final String username = "e628613c-edc3-460d-9677-a32872e6c6d9";
    private static final String password = "gHp7nZY8RMMO";
    private static final String collection_id = "af74d3aa-e649-4ac0-adad-2ec85fc3c194";
    private static final String environment_id = "9d26d096-4253-4e8d-8c78-efad5043e4e5";
    Discovery discovery;

    public DiscoveryService() {
        discovery = new Discovery("2017-09-01");
        discovery.setEndPoint("https://gateway.watsonplatform.net/discovery/api/");
        discovery.setUsernameAndPassword(username, password);

    }

    public void search(String target) {

        Boolean passageOption = true;
        QueryOptions queryOption = new QueryOptions.Builder()
                .environmentId(environment_id)
                .collectionId(collection_id)
                .passages(passageOption)
                .naturalLanguageQuery("target")
                .build();

        QueryResponse result = discovery.query(queryOption).execute();
        List<QueryPassages> check = result.getPassages();


        for (QueryPassages val : check) {
            System.out.println(val.getPassageText());
        }
    }
}









































