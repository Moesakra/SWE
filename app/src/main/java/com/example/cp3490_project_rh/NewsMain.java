package com.example.cp3490_project_rh;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;

public class NewsMain extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        // Start the main activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

        // Finish the current activity
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_main);

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (mWifi.isConnected() == false && mMobile.isConnected() == false) {
            showErrorView();
        }
        else {
            System.out.println("Connected");
            setContentView(R.layout.news_main);

            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
            FileDownloader news = new FileDownloader("\n" +
                    "https://computingsystems.me/etcapp/news/news.xml", NewsMain.this);
            news.setOnResultsListener(new AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    Intent newsScreen = new Intent(getApplicationContext(), NewsActivity.class);
                    newsScreen.putExtra("xmlData", output);
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    startActivity(newsScreen);
                }
            });
            news.execute();
        }
    }

    private void showErrorView() {
        setContentView(R.layout.error_layout);
        TextView errorView = (TextView) findViewById(R.id.errorMessage);
        errorView.setText("App cannot connect to network. Check network settings and try again.");
    }
}

