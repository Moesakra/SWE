package com.example.cadeapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;



// telephony

import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

// email

import android.net.NetworkInfo;
import android.app.AlertDialog;
import android.net.ConnectivityManager;

// web

import android.webkit.WebView;
import android.webkit.WebViewClient;

// maps

public class ContactActivity extends AppCompatActivity {
    String[] items = new String[] { "Call", "Write", "Visit", "Find" };

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
        setContentView(R.layout.contact_activity);

        ListView contactListView = (ListView) findViewById(R.id.contactListView);
        contactListView.setAdapter(new ContactAdapter(this, items));
        contactListView.setTextFilterEnabled(true);
        contactListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String selectedValue = (String) parent.getItemAtPosition(position);

                if(selectedValue.equals("Call")) {
                    call("tel:7097587091");
                }
                else if(selectedValue.equals("Write")) {
                    sendEmail();
                }
                else if(selectedValue.equals("Visit")) {
                    loadWebsite("https://www.cna.nl.ca");
                }
                else if(selectedValue.equals("Find")) {
                    // Google maps
                    openGoogleMaps(47.58716274834393, -52.73459771611199);
                }
            }
        });
    }
    private void openGoogleMaps(double latitude, double longitude) {
        // Create a URI for the Google Maps location
        Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?z=15");

        // Create an Intent to open Google Maps
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

        // Specify that the intention is for Google Maps
        mapIntent.setPackage("com.google.android.apps.maps");

        // Open Google Maps to specified latitude and longitude
        startActivity(mapIntent);
        }
    private void call(String pn) {
        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener,
                PhoneStateListener.LISTEN_CALL_STATE);

        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse(pn));
        try {
            startActivity(Intent.createChooser(callIntent, "Complete Action Using"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ContactActivity.this, "There are no phone clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended, need detect flag
                // from CALL_STATE_OFFHOOK

                if (isPhoneCalling) {
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(
                                    getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    isPhoneCalling = false;
                    finish();
                }
            }
        }
    }

    private void sendEmail() {
        if(!isNetworkAvailable())
            new AlertDialog.Builder(ContactActivity.this).
                    setTitle("Error").setMessage("No Network Connection").
                    setNeutralButton("Close", null).show();
        else {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"bc170264@gmail.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "ETC Programs Info");
            // i.putExtra(Intent.EXTRA_TEXT, "body of email");
            try {
                startActivity(Intent.createChooser(emailIntent, "Complete Action Using"));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(ContactActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected

        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;

    }

    public void loadWebsite(String s) {

        Intent webIntent = new Intent(getApplicationContext(), WebIntent.class);
        webIntent.putExtra("url", "https://www.cna.nl.ca");
        startActivity(webIntent);

    }
}