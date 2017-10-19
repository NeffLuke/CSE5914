package com.example.myfirstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class DiscoveryActivity extends AppCompatActivity {
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textView = (TextView)findViewById(R.id.textView2);

        setContentView(R.layout.activity_discovery);
        String myStr = getIntent().getStringExtra("response");
        textView.setText(myStr);
    }
}
