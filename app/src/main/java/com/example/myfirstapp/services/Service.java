package com.example.myfirstapp.services;

import android.content.Context;

import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;

/**
 * Created by simonrouse9461 on 10/17/17.
 */

public abstract class Service {

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

    protected final String getCommandClass(String command) {
        String cls = nlcService
                .classify(commandClassifierID, command)
                .execute()
                .getTopClass();

        System.out.println("SERVICE CLASSIFIER RESPONSE: " + cls);
        return cls;
    }

    public abstract int executeCommand(String command);

    public abstract String getErrorMessage(int errCode);

}
