package com.midland.ynote.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.midland.ynote.Fragments.ReusableFragmentForTags;
import com.midland.ynote.Fragments.ReusableFragmentForTags;

public class TagsPagerAdapter extends FragmentPagerAdapter {

    String subCategory;
    int pages;

    public TagsPagerAdapter(@NonNull FragmentManager fm, int behavior, int tabs, String subCategory) {
        super(fm, behavior);
        this.subCategory = subCategory;
        this.pages = tabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                if (subCategory.equals("formalSci")){
                    return new ReusableFragmentForTags("Computer science");
                }

                if (subCategory.equals("naturalSci")){
                    return new ReusableFragmentForTags("Biology");
                }

                if (subCategory.equals("appliedSci")){
                    return new ReusableFragmentForTags("Business");
                }

                if (subCategory.equals("socialSci")){
                    return new ReusableFragmentForTags("Anthropology");
                }

                if (subCategory.equals("humanities")){
                    return new ReusableFragmentForTags("Performing arts");
                }

            case 1:
                if (subCategory.equals("formalSci")){
                    return new ReusableFragmentForTags("Mathematics");
                }

                if (subCategory.equals("naturalSci")){
                    return new ReusableFragmentForTags("Chemistry");
                }

                if (subCategory.equals("appliedSci")){
                    return new ReusableFragmentForTags("Engineering");
                }

                if (subCategory.equals("socialSci")){
                    return new ReusableFragmentForTags("Archaeology");
                }

                if (subCategory.equals("humanities")){
                    return new ReusableFragmentForTags("Visual arts");
                }
            case 2:
                if (subCategory.equals("formalSci")){
                    return new ReusableFragmentForTags("Statistics");
                }

                if (subCategory.equals("naturalSci")){
                    return new ReusableFragmentForTags("Earth science");
                }

                if (subCategory.equals("appliedSci")){
                    return new ReusableFragmentForTags("Medicine");
                }

                if (subCategory.equals("socialSci")){
                    return new ReusableFragmentForTags("Economics");
                }

                if (subCategory.equals("humanities")){
                    return new ReusableFragmentForTags("History");
                }

            case 3:
                if (subCategory.equals("formalSci")){
                    return new ReusableFragmentForTags(null);
                }

                if (subCategory.equals("naturalSci")){
                    return new ReusableFragmentForTags("Space science");
                }

                if (subCategory.equals("appliedSci")){
                    return new ReusableFragmentForTags(null);
                }

                if (subCategory.equals("socialSci")){
                    return new ReusableFragmentForTags("Geography");
                }

                if (subCategory.equals("humanities")){
                    return new ReusableFragmentForTags("Home economics");
                }

            case 4:
                if (subCategory.equals("formalSci")){
                    return new ReusableFragmentForTags(null);
                }

                if (subCategory.equals("naturalSci")){
                    return new ReusableFragmentForTags("Physics");
                }

                if (subCategory.equals("appliedSci")){
                    return new ReusableFragmentForTags(null);
                }

                if (subCategory.equals("socialSci")){
                    return new ReusableFragmentForTags("Political science");
                }

                if (subCategory.equals("humanities")){
                    return new ReusableFragmentForTags("Languages");
                }

            case 5:
                if (subCategory.equals("formalSci")){
                    return new ReusableFragmentForTags(null);
                }

                if (subCategory.equals("naturalSci")){
                    return new ReusableFragmentForTags(null);
                }

                if (subCategory.equals("appliedSci")){
                    return new ReusableFragmentForTags(null);
                }

                if (subCategory.equals("socialSci")){
                    return new ReusableFragmentForTags("Psychology");
                }

                if (subCategory.equals("humanities")){
                    return new ReusableFragmentForTags("Law");
                }

            case 6:
                if (subCategory.equals("formalSci")){
                    return new ReusableFragmentForTags(null);
                }

                if (subCategory.equals("naturalSci")){
                    return new ReusableFragmentForTags(null);
                }

                if (subCategory.equals("appliedSci")){
                    return new ReusableFragmentForTags(null);
                }

                if (subCategory.equals("socialSci")){
                    return new ReusableFragmentForTags("Sociology");
                }

                if (subCategory.equals("humanities")){
                    return new ReusableFragmentForTags("Philosophy");
                }

            case 7:
                if (subCategory.equals("formalSci")){
                    return new ReusableFragmentForTags(null);
                }

                if (subCategory.equals("naturalSci")){
                    return new ReusableFragmentForTags(null);
                }

                if (subCategory.equals("appliedSci")){
                    return new ReusableFragmentForTags(null);
                }

                if (subCategory.equals("socialSci")){
                    return new ReusableFragmentForTags("Social work");
                }

                if (subCategory.equals("humanities")){
                    return new ReusableFragmentForTags("Theology");
                }
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return pages;
    }
}
