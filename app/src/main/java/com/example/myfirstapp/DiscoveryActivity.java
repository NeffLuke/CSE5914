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
        setContentView(R.layout.activity_discovery);
        textView = (TextView)findViewById(R.id.textView2);


        String myStr = getIntent().getStringExtra("response");
        if(textView!=null)
            System.err.print("null");
        if(myStr!=null)
            textView.setText(myStr);
        else
            textView.setText("NULL");
    }
}
