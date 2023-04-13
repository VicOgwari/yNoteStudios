package com.midland.ynote.Adapters;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.midland.ynote.Fragments.StudioBitmaps;

public class Studio0PA extends FragmentPagerAdapter {

    private final int tabsNumber;
    private final String uri;
    private final String pictorialLocale;
    private final String flag;
    private final Context c;


    @NonNull
    @Override
    public Fragment getItem(int position) {
            Toast.makeText(c, "OO", Toast.LENGTH_SHORT).show();
            return new StudioBitmaps(pictorialLocale);

    }

    public Studio0PA(@NonNull FragmentManager fm, int behavior, int tabs, String uri, String pictorialLocale, String flag, Context c) {
        super(fm, behavior);
        this.tabsNumber = tabs;
        this.uri = uri;
        this.pictorialLocale = pictorialLocale;
        this.flag = flag;
        this.c = c;
    }

    @Override
    public int getCount() {
        return tabsNumber;
    }
}
