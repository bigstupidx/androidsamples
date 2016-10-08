package com.example.admin.viewpagertest;

import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

/**
 * refer to https://developer.android.com/reference/android/support/v4/view/PagerTabStrip.html
 * http://www.voidynullness.net/blog/2015/08/12/android-pagertabstrip-with-viewpager-and-appcompatactivity/
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        PagerTabStrip tabs = (PagerTabStrip) findViewById(R.id.tabs);
        tabs.setTabIndicatorColor(0x0000ff);
        pager.setAdapter(adapter);
    }
}
