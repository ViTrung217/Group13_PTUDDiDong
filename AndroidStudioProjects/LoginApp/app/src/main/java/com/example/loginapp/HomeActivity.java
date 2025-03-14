package com.example.loginapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView textView = new TextView(this);
        textView.setText("Chào mừng bạn đến với trang chủ!");
        textView.setTextSize(20);
        setContentView(textView);
    }
}
