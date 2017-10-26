package com.example.myfirstapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
//import android.speech.RecognizerIntent;
import com.example.myfirstapp.logins.IFTTTLogin;
import com.example.myfirstapp.logins.NestLogin;
import com.example.myfirstapp.services.IFTTTService;
import com.example.myfirstapp.services.NestService;
import com.example.myfirstapp.services.RootService;
import com.example.myfirstapp.services.Service;
import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import android.support.design.widget.Snackbar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int SPEECH_REQUEST_CODE = 0;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private EditText commandText;
    private NaturalLanguageClassifier service;
    private TextView speakButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        commandText = (EditText) findViewById(R.id.commandField);
        commandText.setText("");

        speakButton = (TextView) findViewById(R.id.speakButton);
        speakButton.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                displaySpeechRecognizer();
            }
        }));
        service = new NaturalLanguageClassifier();
        service.setUsernameAndPassword("a475cc56-93c6-4b1c-9cc5-f76d8af50830", "rFlhaBf2aEtS");
        RootService.getInstance()
                .setNlcService(service)
                .setCommandClassifierID("ebd15ex229-nlc-62507");
        IFTTTService.getInstance()
                .setNlcService(service)
                .setCommandClassifierID("6a2a04x217-nlc-28653");
        NestService.getInstance()
                .setNlcService(service)
                .setCommandClassifierID("ebd2f7x230-nlc-22389");

    }
//curl -i --user "{username}":"{password}" -F training_data=@{path_to_file}/weather_data_train.csv -F training_metadata="{\"language\":\"en\",\"name\":\"TutorialClassifier\"}" "https://gateway.watsonplatform.net/natural-language-classifier/api/v1/classifiers"
    /** Called when the user taps the send button */

    public void sendTrigger(final View view) {

        final String command = commandText.getText().toString();

        if (command.trim().length() == 0) {
            Snackbar.make(view, "Please enter a command", 1000).show();
            return;
        }
        RootService.getInstance()
                .execute(command, this, new Service.ExecCallback() {
            @Override
            public void onResponse(String message) { }

            @Override
            public void onFailure(Exception e) {
                Snackbar.make(view, e.getMessage(), 1000).show();
            }
        });

    }
    private void askSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Please Voice Enter a Command");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    public void nestLogin(View view) {
        startActivity(new Intent(this, NestLogin.class));
    }

    public void iftttLogin(View view) {
        startActivity(new Intent(this, IFTTTLogin.class));
    }
    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
// Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }
    // This callback is invoked when the Speech Recognizer returns.
// This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            // Do something with spokenText
            commandText.setText(spokenText);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

