package com.example.myfirstapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.myfirstapp.logins.IFTTTLogin;
import com.example.myfirstapp.logins.NestLogin;
import com.example.myfirstapp.services.IFTTTService;
import com.example.myfirstapp.services.NestService;
import com.example.myfirstapp.services.Service;
import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import android.support.design.widget.Snackbar;

public class MainActivity extends AppCompatActivity {

    public static final String MESSAGE_ID = "com.example.myfirstapp.MESSAGE";
    private String serviceClassifierID = "bfad19x228-nlc-31622";

    private EditText commandText;
    private EditText actionText;
    private NaturalLanguageClassifier service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        commandText = (EditText) findViewById(R.id.commandField);
        commandText.setText("");

        service = new NaturalLanguageClassifier();
        service.setUsernameAndPassword("a475cc56-93c6-4b1c-9cc5-f76d8af50830", "rFlhaBf2aEtS");
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

        final ServiceCall<Classification> serviceClassification = service.classify(serviceClassifierID, command);

        new Thread() {
            @Override
            public void run() {
                String serviceClass = serviceClassification.execute().getTopClass();
                ServiceCall<Classification> commandClassification;

                Service service;

                switch (serviceClass) {
                    case "lifx":
                        service = IFTTTService.getInstance();
                        break;
                    case "nest":
                        service = NestService.getInstance();
                        break;
                    default:
                        return;
                }

                int errCode = service.executeCommand(command, MainActivity.this);
                if (errCode != 0) {
                    Snackbar.make(view, service.getErrorMessage(errCode), 1000).show();
                }
            }
        }.start();

    }

    public void nestLogin(View view) {
        startActivity(new Intent(this, NestLogin.class));
    }

    public void iftttLogin(View view) {
        startActivity(new Intent(this, IFTTTLogin.class));
    }
}

