package com.midland.ynote.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.midland.ynote.Fragments.MainFragment;
import com.midland.ynote.Fragments.MainFragment1;
import com.midland.ynote.Fragments.OpenAI;

public class MainPagerAdapter extends FragmentPagerAdapter {


    int pages;

    public MainPagerAdapter(@NonNull FragmentManager fm, int behavior, int tabs) {
        super(fm, behavior);
        this.pages = tabs;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new MainFragment();

            case 1:
                return new MainFragment1();

            case 2:
                return new OpenAI();
        }
        return null;
    }

    @Override
    public int getCount() {
        return pages;
    }

}
