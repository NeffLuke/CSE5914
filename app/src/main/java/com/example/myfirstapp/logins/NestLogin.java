package com.example.myfirstapp.logins;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;

import com.example.myfirstapp.R;
import com.example.myfirstapp.services.NestService;

public class NestLogin extends AppCompatActivity {

    public EditText pinField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nest_login);

        WebView myWebView = (WebView) findViewById(R.id.nestLoginPage);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl(NestService
                .getInstance()
                .getNestAuthorizationUrl());

        pinField = (EditText) findViewById(R.id.pinField);
    }

    public void connectWithPin(View view) {
        final String pin = pinField.getText().toString();
        NestService.getInstance().authorizeWithPin(pin, this);
        finish();
    }
}
