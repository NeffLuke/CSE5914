package com.example.myfirstapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classifier;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {


    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user taps the send button */

    public void sendTrigger(View view) {


        final Intent intent = new Intent(this, DisplayText.class);


        final NaturalLanguageClassifier service = new NaturalLanguageClassifier();
        service.setUsernameAndPassword("a475cc56-93c6-4b1c-9cc5-f76d8af50830","rFlhaBf2aEtS");

        final ServiceCall<Classification> classification = service.classify("6a2a04x217-nlc-28653", "william");

        String str;

        new Thread() {
            @Override
            public void run() {


                //  Classifier classifier = service.getClassifier("6a2a04x217-nlc-28653").execute();
                String msg = classification.execute().getClasses().get(0).toString();
                intent.putExtra(EXTRA_MESSAGE, msg);
                startActivity(intent);
            }
        }.start();
    }



}

