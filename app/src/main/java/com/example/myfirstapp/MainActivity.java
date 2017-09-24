package com.example.myfirstapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;

public class MainActivity extends AppCompatActivity {

    public static final String MESSAGE_ID = "com.example.myfirstapp.MESSAGE";
    public static final String ACTION_ID = "com.example.myfirstapp.ACTION";
    public static final String KET_ID = "com.example.myfirstapp.KEY";
    public EditText commandText;
    public EditText actionText;
    public EditText keyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        commandText = (EditText) findViewById(R.id.commandField);
        actionText = (EditText) findViewById(R.id.actionField);
        keyText = (EditText) findViewById(R.id.keyField);
    }

    /** Called when the user taps the send button */

    public void sendTrigger(View view) {
        final Intent intent = new Intent(this, DisplayText.class);
        final NaturalLanguageClassifier service = new NaturalLanguageClassifier();
        service.setUsernameAndPassword("a475cc56-93c6-4b1c-9cc5-f76d8af50830", "rFlhaBf2aEtS");
        String inputString = commandText.getText().toString();
        final ServiceCall<Classification> classification = service.classify("6a2a04x217-nlc-28653", inputString);
        final String action = actionText.getText().toString();
        final String key = keyText.getText().toString();

        new Thread() {
            @Override
            public void run() {
                String msg = classification.execute().getClasses().get(0).toString();
                intent.putExtra(MESSAGE_ID, msg);
                intent.putExtra(ACTION_ID, action);
                intent.putExtra(KET_ID, key);
                startActivity(intent);
            }
        }.start();
    }

}

