package com.example.myfirstapp.services;

import android.content.Context;

import com.ibm.watson.developer_cloud.http.ServiceCallback;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;

/**
 * Created by simonrouse9461 on 10/17/17.
 */

public abstract class Service {

    public interface ExecCallback {
        void onResponse(String message);
        void onFailure(Exception e);
    }

    private NaturalLanguageClassifier nlcService;

    private String commandClassifierID;

    public Service setNlcService(NaturalLanguageClassifier service) {
        nlcService = service;
        return this;
    }

    public Service setCommandClassifierID(String id) {
        commandClassifierID = id;
        return this;
    }

    public Service execute(final String command, final ExecCallback callback) {
        System.out.println("EXECUTING");
        nlcService.classify(commandClassifierID, command)
                .enqueue(new ServiceCallback<Classification>() {
                    @Override
                    public void onResponse(Classification response) {
                        String cls = response.getTopClass();
                        System.out.println("SERVICE CLASSIFIER RESPONSE: " + cls);
                        handleClassifierResponse(command, cls, callback);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        callback.onFailure(e);
                    }
                });

        return this;
    }

    protected abstract void handleClassifierResponse(String cmd, String cls, ExecCallback callback);

}
