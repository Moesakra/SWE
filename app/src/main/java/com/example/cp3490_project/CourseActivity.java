package com.example.cp3490_project;


import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class CourseActivity extends AppCompatActivity {
    private String cid, cname, credit, lect, lab;

    @Override
    public void onBackPressed() {
        // Start the main activity
        Intent intent = new Intent(this, CoursesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

        // Finish the current activity
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course);

        Bundle extras = getIntent().getExtras();
        cid = extras.getString("cid");
        cname = extras.getString("cname");
        credit = extras.getString("credit");
        lect = extras.getString("lect");
        lab = extras.getString("lab");

        this.setTitle(cid);

        TextView cnt = (TextView)findViewById(R.id.cname);
        cnt.setText(cname);

        TextView crt = (TextView)findViewById(R.id.credit);
        crt.setText(credit + " credit hours");

        TextView let = (TextView)findViewById(R.id.lect);
        let.setText(lect + " hours per week");

        TextView lbt = (TextView)findViewById(R.id.lab);
        lbt.setText(lab + " hours per week");
    }
}