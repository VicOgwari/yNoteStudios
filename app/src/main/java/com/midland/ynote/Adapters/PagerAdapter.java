package com.midland.ynote.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.midland.ynote.Fragments.BitMaps;
import com.midland.ynote.Fragments.Documents;
import com.midland.ynote.Fragments.Lectures;


public class PagerAdapter extends FragmentPagerAdapter {

    private final int tabsNumber;

    public PagerAdapter(@NonNull FragmentManager fm, int behavior, int tabs) {
        super(fm, behavior);
        this.tabsNumber = tabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return  new Lectures();

            case 1:
                return new BitMaps();

            case 2:
                return new Documents();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabsNumber;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

}
