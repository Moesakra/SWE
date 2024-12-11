package com.example.cadeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.graphics.Color;
import android.util.Log;

import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.PagerTabStrip;

public class TimetableActivity extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        // Start the main activity
        Intent intent = new Intent(this, SpinnerAdapter.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

        // Finish the current activity
        finish();
    }
    final String[] page_titles = new String[]{"Mon", "Tue", "Wed", "Thu", "Fri"};

    final String[] hours = new String[]{"9", "10", "11", "noon", "1", "2", "3", "4", "5", "6"};

    private String[][] days;
    private int pos;

    public static ArrayList<String> schedule = new ArrayList<String>();

    List<Map<String, String>>[] list = new List[5]; // Mon - Fri
    Map<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isNetworkAvailable())
            new AlertDialog.Builder(TimetableActivity.this).
                    setTitle("Error").setMessage("No Network Connection").
                    setNeutralButton("Close", null).show();
        else {
            Bundle extras = getIntent().getExtras();
            String timetableURL = extras.getString("timetableURL");
            new GetXML().execute(timetableURL);
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private class GetXML extends AsyncTask<String, Void, String> {
        String src = null;

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]); // Gets the timetable url passed in to the execute from the extra in onCreate
                Log.d("timetableURL", params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                src = readStream(con.getInputStream());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return src;
        }

        @Override
        protected void onPostExecute(String result) {
            if (src == null)
                new AlertDialog.Builder(TimetableActivity.this).
                        setTitle("Error").setMessage("No Schedule Found").
                        setNeutralButton("Close", null).show();
            else {
                parseXML(src);
            }

            setContentView(R.layout.timetable_layout);

            days = new String[5][10];
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 10; j++) {
                    int k = i * 10 + j;
                    days[i][j] = schedule.get(k);
                }
            }

            for (int i = 0; i < 5; i++)
                list[i] = new ArrayList<Map<String, String>>();

            int count = hours.length;

            for (int j = 0; j < 5; j++) {
                for (int i = 0; i < count; i++) {
                    map = new HashMap<String, String>();
                    map.put("time", hours[i]);
                    map.put("description", days[j][i]);
                    list[j].add(map);
                }
            }

            Calendar cal = Calendar.getInstance();
            int today = cal.get(Calendar.DAY_OF_WEEK) - 2;

            pos = 0;
            if (today >= 0 && today <= 4)
                pos = today;

            ViewPager viewPager = findViewById(R.id.ViewPager);
            CustomPagerAdapter adapter = new CustomPagerAdapter(TimetableActivity.this, list);
            PagerTabStrip pagerTabStrip = findViewById(R.id.pager_tab);

            int color = Color.parseColor("#33b7ee");
            pagerTabStrip.setTabIndicatorColor(color);

            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(pos);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    public void parseXML(String src) {
        try {
            StringReader sr = new StringReader(src);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(sr);

            int token = xpp.getEventType();
            while (token != XmlPullParser.END_DOCUMENT) {
                if (token == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals("cid")) {
                        token = xpp.nextToken();

                        if (xpp.getText() == null)
                            schedule.add("");

                        if (token == XmlPullParser.TEXT) {
                            schedule.add(xpp.getText());
                        }
                    }
                }

                token = xpp.nextToken();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        String line = null;
        StringBuffer sb = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
