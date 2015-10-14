package com.jesperleker.regnklader;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by jesperhellberg on 2015-10-14.
 */
public class myFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] tabTitles = new String[]{"Idag", "Imorgon"};
    private Context context;

    public myFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new todayFragment();
        }
        if (position == 1) {
            return new todayFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
