package com.midland.ynote.Adapters;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.midland.ynote.Fragments.FullCam;
import com.midland.ynote.Fragments.StudioBitmaps;
import com.midland.ynote.Fragments.StudioPdfViewer;

public class StudioPageAdapter extends FragmentPagerAdapter {

    private final int tabsNumber;
    private final String uri;
    private final String pictorialLocale;
    private final String flag;
    private final Context c;
    private final Activity a;


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:{

                if (flag.equals("0") || flag.equals("3") || flag.equals("5")){
                    return new StudioPdfViewer(uri);
                }

                if (flag.equals("1") || flag.equals("4")){
                    return new StudioBitmaps(pictorialLocale);
                }

                if (flag.equals("2")){
                    return new FullCam();
                }
            }


            case 1:
                switch (flag) {
                    case "3":
                    case "4":
                        return new FullCam();
                    case "5":
                        return new StudioBitmaps(pictorialLocale);
                }

            case 2:
                return new FullCam();

            default:
                return null;

        }


    }

    public StudioPageAdapter(@NonNull FragmentManager fm, int behavior, int tabs, String uri, String pictorialLocale, String flag, Context c, Activity a) {
        super(fm, behavior);
        this.tabsNumber = tabs;
        this.uri = uri;
        this.pictorialLocale = pictorialLocale;
        this.flag = flag;
        this.c = c;
        this.a = a;
    }

    @Override
    public int getCount() {
        return tabsNumber;
    }
}
