package com.fourlines.doctor;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.fourlines.adapter.TabsPagerAdapter;
import com.fourlines.view.PagerSlidingTabStrip;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

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
//        View view = this.getCurrentFocus();
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
        if (getIntent().getStringExtra("Page") != null) {
            viewPager.setCurrentItem(Integer.parseInt(getIntent().getStringExtra("Page")));
            Log.d("FRAG", "page" + viewPager.getCurrentItem());
        }
        slideTabs.setViewPager(viewPager);
//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrolled(int position, float offset, int offsetPixels) {
//                final InputMethodManager imm = (InputMethodManager) getSystemService(
//                        Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return false;
    }
}