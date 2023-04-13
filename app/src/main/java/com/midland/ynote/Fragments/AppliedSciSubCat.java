package com.midland.ynote.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.midland.ynote.Adapters.TagsPagerAdapter;
import com.midland.ynote.R;

public class AppliedSciSubCat extends Fragment {


    private ViewPager appliedSciViewPager;
    private CardView businessCard, engineeringCard, medicineCard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_applied_sci, container, false);
        appliedSciViewPager = v.findViewById(R.id.appliedSciViewPager);
        TagsPagerAdapter tagsPagerAdapter = new TagsPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, 3, "appliedSci");
        appliedSciViewPager.setAdapter(tagsPagerAdapter);

        appliedSciViewPager.setOnTouchListener((v1, event) -> {
            appliedSciViewPager.setCurrentItem(appliedSciViewPager.getCurrentItem());
            return false;
        });

        businessCard = v.findViewById(R.id.businessCard);
        businessCard.setCardBackgroundColor(Color.YELLOW);
        medicineCard = v.findViewById(R.id.medCard);
        engineeringCard = v.findViewById(R.id.engCard);
        clickListener(businessCard, 0);
        clickListener(engineeringCard, 1);
        clickListener(medicineCard, 2);

        return v;
    }

    private void clickListener(CardView card, final int flag){

        card.setOnClickListener(v -> {
            switch (flag){
                case 0:
                    appliedSciViewPager.setCurrentItem(0, true);
                    businessCard.setCardBackgroundColor(Color.YELLOW);
                    engineeringCard.setCardBackgroundColor(Color.WHITE);
                    medicineCard.setCardBackgroundColor(Color.WHITE);
                    break;

                case 1:
                    appliedSciViewPager.setCurrentItem(1, true);
                    businessCard.setCardBackgroundColor(Color.WHITE);
                    engineeringCard.setCardBackgroundColor(Color.YELLOW);
                    medicineCard.setCardBackgroundColor(Color.WHITE);
                    break;

                case 2:
                    appliedSciViewPager.setCurrentItem(2, true);
                    businessCard.setCardBackgroundColor(Color.WHITE);
                    engineeringCard.setCardBackgroundColor(Color.WHITE);
                    medicineCard.setCardBackgroundColor(Color.YELLOW);
                    break;


            }
    });

    }

}
