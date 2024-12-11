package com.example.cadeapp;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.List;
import java.util.Map;


public class CustomPagerAdapter extends PagerAdapter {

    final String[] page_titles = new String[]{"Mon", "Tue", "Wed", "Thu", "Fri"};
    List <Map<String, String>> [] list;

    Context context;
    LayoutInflater layoutInflater;

    CustomPagerAdapter(Context context, List<Map<String, String>>[] list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return page_titles.length;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.ttpage, null);
        ListView timeListView = (ListView) view.findViewById(R.id.timetable);

        switch(position) {
            case 0 : timeListView.setAdapter(new TimeTableAdapter(context, list[0], 0)); break;
            case 1 : timeListView.setAdapter(new TimeTableAdapter(context, list[1], 1)); break;
            case 2 : timeListView.setAdapter(new TimeTableAdapter(context, list[2], 2)); break;
            case 3 : timeListView.setAdapter(new TimeTableAdapter(context, list[3], 3)); break;
            case 4 : timeListView.setAdapter(new TimeTableAdapter(context, list[4], 4)); break;
            default : break;
        }
        timeListView.setTextFilterEnabled(true);

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return page_titles[position];
    }
}

