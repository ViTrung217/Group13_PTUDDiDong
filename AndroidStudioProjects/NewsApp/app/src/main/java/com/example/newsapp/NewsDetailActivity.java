package com.example.newsapp;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class NewsDetailActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        String url = getIntent().getStringExtra("url");
        webView.loadUrl(url);
    }
}
