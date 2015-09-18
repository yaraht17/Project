package com.fourlines.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fourlines.view.PagerSlidingTabStrip.IconTabProvider;
import com.fourlines.doctor.R;
import com.fourlines.fragment.DoctorListFragment;
import com.fourlines.fragment.SickListFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter implements
        IconTabProvider {


    private int tabIcons[] = {R.drawable.selector_fragment_chat,
            R.drawable.selector_fragment_sick_list
    };

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                return new DoctorListFragment();
            case 1:
                return new SickListFragment();

        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public int getPageIconResId(int position) {
        return tabIcons[position];
    }
}
