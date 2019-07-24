package com.example.ppeepfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PPeepRideWebview extends AppCompatActivity {
    private WebView webView2;
    public static final String WEBSITE_ADDRESS = "website_address";
    Toolbar foodToolbar;
    private final int SPLASH_DISPLAY_LENGTH = 2200;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppeep_ride_webview);
        foodToolbar = (Toolbar) findViewById(R.id.foodtoolbar);
        setSupportActionBar(foodToolbar);

        ShowLoder("Loading .. ..");



        String url  = getIntent().getStringExtra(WEBSITE_ADDRESS);
        if (url == null || url.isEmpty()) finish();

        WebView webView = (WebView) findViewById(R.id.webview1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

        // dialog.dismiss();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                dialog.dismiss();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }


    public void ShowLoder(String message){
        dialog = ProgressDialog.show(this, "",
                message, true);
    }
}