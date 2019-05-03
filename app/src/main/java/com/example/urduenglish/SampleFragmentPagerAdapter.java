package com.example.urduenglish;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    public SampleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private String tabTitles[] = new String[] { "Numbers", "Family", "Colors" };
    @Override
    public Fragment getItem(int position) {
        if(position==0)
            return new Numbers_Fragment();
        else if(position==1)
            return new Family_Fragment();
        else
            return new Color_Fragment();
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
