package com.example.ppeepfinal;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class WebViewClass extends AppCompatActivity {
    private WebView webView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_class);


        webView2  = (WebView) findViewById(R.id.webview1);


        Intent intent = getIntent();
        String uri = intent.getStringExtra("uri");
        setContentView(webView2);
        webView2.loadUrl(uri);
    }
}
