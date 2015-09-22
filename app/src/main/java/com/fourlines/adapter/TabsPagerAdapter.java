package com.fourlines.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fourlines.doctor.R;
import com.fourlines.fragment.ChatFragment;
import com.fourlines.fragment.NotificationFragment;
import com.fourlines.fragment.ProfileFragment;
import com.fourlines.fragment.SickListFragment;
import com.fourlines.view.PagerSlidingTabStrip;

public class TabsPagerAdapter extends FragmentPagerAdapter
        implements PagerSlidingTabStrip.IconTabProvider {

    //    private String tabTitle[] = {"&#xf086;", "&#xf002;", "&#xf1ea;", "&#xf007;"};
    private int tabIcons[] = {R.drawable.selector_fragment_chat,
            R.drawable.selector_fragment_sick_list, R.drawable.selector_fragment_notif, R.drawable.selector_fragment_user
    };

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                return new ChatFragment();
            case 1:
                return new SickListFragment();
            case 2:
                return new NotificationFragment();
            case 3:
                return new ProfileFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    //    @Override
//    public CharSequence getPageTitle(int position) {
//        return tabTitle[position];
//    }
    @Override
    public int getPageIconResId(int position) {
        return tabIcons[position];
    }
}
