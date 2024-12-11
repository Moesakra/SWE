package com.example.cp3490_project;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.app.AlertDialog;

public class SpinnerAdapter extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        // Start the main activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

        // Finish the current activity
        finish();
    }
    final static String[] programs = { "Select Program", "Architectural",
            "Civil", "Geomatics", "Computing Systems", "Biomedical",
            "Instrumentation", "Electrical (Power)",
            "Telecommunications", "Chemical Processing", "Industrial", "Mechanical",
            "Manufacturing", "Petroleum" };

    final static String[] years = { "Select Year", "First", "Second", "Third" };

    String p = new String();
    String y = new String();
    String cid = new String();
    String loc = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spinnerlayout);

        Spinner spinner_p = (Spinner) findViewById(R.id.spinner_p);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SpinnerAdapter.this,
                R.layout.spinnerrow, R.id.spinner_id, programs);
        spinner_p.setAdapter(arrayAdapter);

        spinner_p.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                switch(pos) {
                    case 1 : cid = "ae"; break;
                    case 2 : cid = "ce"; break;
                    case 3 : cid = "ge"; break;
                    case 4 : cid = "cs"; break;
                    case 5 : cid = "eb"; break;
                    case 6 : cid = "ei"; break;
                    case 7 : cid = "ep"; break;
                    case 8 : cid = "te"; break;
                    case 9 : cid = "cp"; break;
                    case 10: cid = "in"; break;
                    case 11: cid = "me"; break;
                    case 12: cid = "mm"; break;
                    case 13: cid = "pe"; break;
                    default: break;
                }
                p = programs[pos];
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner spinner_y = (Spinner) findViewById(R.id.spinner_y);
        ArrayAdapter<String> y_arrayAdapter = new ArrayAdapter<String>(SpinnerAdapter.this,
                R.layout.spinnerrow, R.id.spinner_id, years);
        spinner_y.setAdapter(y_arrayAdapter);

        spinner_y.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                y = years[pos];
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final Button b = (Button) findViewById(R.id.schedule);

        b.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(v == b) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        v.setAlpha(.3f);
                    }
                    else {
                        v.setAlpha(1f);
                    }
                }
                return false;
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(p.equals("Select Program") || y.equals("Select Year"))
                    new AlertDialog.Builder(SpinnerAdapter.this).
                            setTitle("Error").setMessage("No Program/Year selected").
                            setNeutralButton("Close", null).show();
                else {
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat monthFormat = new SimpleDateFormat("M");
                    String month_name = monthFormat.format(cal.getTime());

                    int month = Integer.parseInt(month_name);
                    int selectedYear = 0;

                    if (y.equals("First"))
                        selectedYear = 1;
                    else if (y.equals("Second"))
                        selectedYear = 2;
                    else if (y.equals("Third"))
                        selectedYear = 3;

                    int semester = 0;
                    if (month >= 9 && month <= 12) {
                        semester = 3 * selectedYear - 2;
                    } else if (month >= 1 && month <= 4) {
                        semester = 3 * selectedYear - 1;
                    } else {
                        semester = 3 * selectedYear;
                    }

                    StringBuffer sb = new StringBuffer();
                    sb.append("https://computingsystems.me/etcapp/timetables/timetable_"); sb.append(cid); sb.append(semester); sb.append(".xml");
                    loc = sb.toString();

                    Intent TimetableActivity = new Intent(getApplicationContext(), TimetableActivity.class); // Change TimetableActivity.class to your class name if it isn't that
                    TimetableActivity.putExtra("timetableURL", loc);
                    startActivity(TimetableActivity);
                }
            }
        });
    }
}

