package com.example.myfirstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

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
        AccountAuthorizations
                .getInstance()
                .setIftttKey(
                        keyField.getText().toString()
                );
        finish();
    }
}
