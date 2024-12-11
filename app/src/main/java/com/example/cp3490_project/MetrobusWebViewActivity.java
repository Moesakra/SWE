package com.example.cp3490_project;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MetrobusWebViewActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        // Start the main activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

        // Finish the current activity
        finish();
    }
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.metrobus_webview);

        webView = findViewById(R.id.webView);
        configureWebViewSettings();
        loadMetrobusWebsite();
    }

    private void configureWebViewSettings() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Enable JavaScript if needed
        // Additional WebView settings can be configured here
    }

    private void loadMetrobusWebsite() {
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.metrobus.com");
    }
}