package com.example.cp3490_project;

/**
 * Created by admin on 2018-07-20.
 */

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.Display;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailNewsActivity extends AppCompatActivity {
    Bitmap bitmap;
    String dnewsTitle, dnewsDate, dnewsDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detail_news_layout);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = 3 * width / 4;

        Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("picture");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        ImageView newsImage = (ImageView) findViewById(R.id.detailNewsImage);
        newsImage.getLayoutParams().height = height;
        newsImage.setImageBitmap(bitmap);

        dnewsTitle = extras.getString("title");
        dnewsDate = extras.getString("date");
        dnewsDescription = extras.getString("description");

        TextView dtitle = (TextView) findViewById(R.id.dnewsTitle);
        dtitle.setText(dnewsTitle);

        TextView ddate = (TextView) findViewById(R.id.dnewsDate);
        ddate.setText(dnewsDate);

        TextView ddescription = (TextView) findViewById(R.id.dnewsDescription);
        ddescription.setText(dnewsDescription);
    }
}
