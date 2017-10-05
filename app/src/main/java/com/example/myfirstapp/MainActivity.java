package com.example.myfirstapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import 	android.support.design.widget.Snackbar;
public class MainActivity extends AppCompatActivity {

    public static final String MESSAGE_ID = "com.example.myfirstapp.MESSAGE";
    public EditText commandText;
    public EditText actionText;
    public String msg ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        commandText = (EditText) findViewById(R.id.commandField);
        commandText.setText("");

    }

    /** Called when the user taps the send button */

    public void sendTrigger(View view) {

        final Snackbar mySnackbar = Snackbar.make(view, "Error: Please connect to services", 1000);
        final Snackbar noString= Snackbar.make(view, "Please input a string", 1000);
        final Intent intent = new Intent(this, DisplayText.class);
        final NaturalLanguageClassifier service = new NaturalLanguageClassifier();
        service.setUsernameAndPassword("a475cc56-93c6-4b1c-9cc5-f76d8af50830", "rFlhaBf2aEtS");
        String inputString = commandText.getText().toString();


        if(inputString.trim().length()== 0)
        {
            noString.show();
        }
        else {
            final ServiceCall<Classification> classification = service.classify("bfad19x228-nlc-31622", inputString);
            final String key = AccountAuthorizations.getInstance().getIftttKey();

            new Thread() {
                @Override
                public void run() {
                    String msg = classification.execute().getClasses().get(0).getName();
                    String inputString = commandText.getText().toString();
                    ServiceCall<Classification> classification2 = service.classify("6a2a04x217-nlc-28653", inputString);
                    if (msg.equals("lifx") && key != null) {
                        //change classifier id

                    } else if (key == null) {

                    } else if (msg.equals("nest")) {
                        //change classifier id
                    }


                    if (key != null) {
                        msg = classification2.execute().getClasses().get(0).getName();

                        intent.putExtra(MESSAGE_ID, msg);
                        startActivity(intent);
                    } else {
                        mySnackbar.show();
                    }
                }
            }.start();
        }
    }

    public void nestLogin(View view) {
        startActivity(new Intent(this, NestLogin.class));
    }

    public void iftttLogin(View view) {
        startActivity(new Intent(this, IFTTTLogin.class));
    }
}

