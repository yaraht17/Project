package com.fourlines.doctor;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.fourlines.adapter.TabsPagerAdapter;
import com.fourlines.view.PagerSlidingTabStrip;

public class MainActivity extends AppCompatActivity {
    //tab layout
    private ViewPager viewPager;
    private TabsPagerAdapter mTabsAdapter;
    private PagerSlidingTabStrip slideTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        slideTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        mTabsAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mTabsAdapter);


        //ve fragment hien tai
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        if (getIntent().getStringExtra("Page") != null) {
            viewPager.setCurrentItem(Integer.parseInt(getIntent().getStringExtra("Page")));
            Log.d("FRAG", "page" + viewPager.getCurrentItem());
        }
        slideTabs.setViewPager(viewPager);
    }
}