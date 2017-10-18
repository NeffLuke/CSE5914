package com.example.myfirstapp.logins;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.myfirstapp.R;
import com.example.myfirstapp.services.IFTTTService;

public class IFTTTLogin extends AppCompatActivity {

    private static final String webhookUrl = "https://ifttt.com/maker_webhooks";

    public EditText keyField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ifttt_login);

        keyField = (EditText) findViewById(R.id.keyField);
    }

    public void connectWithKey(View view) {
        String key = keyField.getText().toString();
        IFTTTService.getInstance().setIftttKey(key);
        finish();
    }
}
