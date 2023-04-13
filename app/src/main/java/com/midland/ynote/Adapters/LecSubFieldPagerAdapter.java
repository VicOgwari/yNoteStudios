package com.midland.ynote.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.midland.ynote.Fragments.AppliedSciSubCat;
import com.midland.ynote.Fragments.FormalSciSubCat;
import com.midland.ynote.Fragments.HumanitiesSubCat;
import com.midland.ynote.Fragments.NaturalSciSubCat;
import com.midland.ynote.Fragments.SocialSciSubCat;
import com.midland.ynote.Fragments.UserGenSubCat;
import com.midland.ynote.Fragments.AppliedSciSubCat;
import com.midland.ynote.Fragments.FormalSciSubCat;
import com.midland.ynote.Fragments.HumanitiesSubCat;
import com.midland.ynote.Fragments.NaturalSciSubCat;
import com.midland.ynote.Fragments.SocialSciSubCat;
import com.midland.ynote.Fragments.UserGenSubCat;


public class LecSubFieldPagerAdapter extends FragmentPagerAdapter {

    private int mainFields;

    public LecSubFieldPagerAdapter(@NonNull FragmentManager fm, int behavior, int tabs) {
        super(fm, behavior);
        this.mainFields = tabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new UserGenSubCat();
            case 1:
                return new HumanitiesSubCat();
            case 2:
                return  new SocialSciSubCat();
            case 3:
                return new NaturalSciSubCat();
            case 4:
                return new FormalSciSubCat();
            case 5:
                return new AppliedSciSubCat();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mainFields;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }


}
