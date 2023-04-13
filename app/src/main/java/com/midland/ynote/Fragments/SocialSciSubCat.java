package com.midland.ynote.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

public class SocialSciSubCat extends Fragment {

    private ViewPager socialSciViewPager;
    private CardView anthropologyCard, archaeologyCard, economicsCard, geographyCard, politicalCard, psychologyCard, sociologyCard, socialWordCard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_social_sci, container, false);
        socialSciViewPager = v.findViewById(R.id.socialSciViewPager);
        TagsPagerAdapter tagsPagerAdapter = new TagsPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, 8, "socialSci");
        socialSciViewPager.setAdapter(tagsPagerAdapter);
        socialSciViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                socialSciViewPager.setCurrentItem(socialSciViewPager.getCurrentItem());
                return false;
            }
        });

        anthropologyCard = v.findViewById(R.id.anthroCard);
        anthropologyCard.setCardBackgroundColor(Color.YELLOW);
        archaeologyCard = v.findViewById(R.id.archaeologyCard);
        economicsCard = v.findViewById(R.id.economicsCard);
        geographyCard = v.findViewById(R.id.geographyCard);
        politicalCard = v.findViewById(R.id.politicalSciCard);
        psychologyCard = v.findViewById(R.id.psychologyCard);
        sociologyCard = v.findViewById(R.id.sociologyCard);
        socialWordCard = v.findViewById(R.id.socialWordCard);

        clickListener(anthropologyCard, 0);
        clickListener(archaeologyCard, 1);
        clickListener(economicsCard, 2);
        clickListener(geographyCard, 3);
        clickListener(politicalCard, 4);
        clickListener(psychologyCard, 5);
        clickListener(sociologyCard, 6);
        clickListener(socialWordCard, 7);

        return v;
    }

    private void clickListener(CardView card, final int flag){

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (flag){
                    case 0:
                        socialSciViewPager.setCurrentItem(0, true);
                        anthropologyCard.setCardBackgroundColor(Color.YELLOW);
                        archaeologyCard.setCardBackgroundColor(Color.WHITE);
                        economicsCard.setCardBackgroundColor(Color.WHITE);
                        geographyCard.setCardBackgroundColor(Color.WHITE);
                        politicalCard.setCardBackgroundColor(Color.WHITE);
                        psychologyCard.setCardBackgroundColor(Color.WHITE);
                        sociologyCard.setCardBackgroundColor(Color.WHITE);
                        socialWordCard.setCardBackgroundColor(Color.WHITE);

                        break;

                    case 1:
                        socialSciViewPager.setCurrentItem(1, true);
                        anthropologyCard.setCardBackgroundColor(Color.WHITE);
                        archaeologyCard.setCardBackgroundColor(Color.YELLOW);
                        economicsCard.setCardBackgroundColor(Color.WHITE);
                        geographyCard.setCardBackgroundColor(Color.WHITE);
                        politicalCard.setCardBackgroundColor(Color.WHITE);
                        psychologyCard.setCardBackgroundColor(Color.WHITE);
                        sociologyCard.setCardBackgroundColor(Color.WHITE);
                        socialWordCard.setCardBackgroundColor(Color.WHITE);

                        break;

                    case 2:
                        socialSciViewPager.setCurrentItem(2, true);
                        anthropologyCard.setCardBackgroundColor(Color.WHITE);
                        archaeologyCard.setCardBackgroundColor(Color.WHITE);
                        economicsCard.setCardBackgroundColor(Color.YELLOW);
                        geographyCard.setCardBackgroundColor(Color.WHITE);
                        politicalCard.setCardBackgroundColor(Color.WHITE);
                        psychologyCard.setCardBackgroundColor(Color.WHITE);
                        sociologyCard.setCardBackgroundColor(Color.WHITE);
                        socialWordCard.setCardBackgroundColor(Color.WHITE);
                        break;

                    case 3:
                        socialSciViewPager.setCurrentItem(3, true);
                        anthropologyCard.setCardBackgroundColor(Color.WHITE);
                        archaeologyCard.setCardBackgroundColor(Color.WHITE);
                        economicsCard.setCardBackgroundColor(Color.WHITE);
                        geographyCard.setCardBackgroundColor(Color.YELLOW);
                        politicalCard.setCardBackgroundColor(Color.WHITE);
                        psychologyCard.setCardBackgroundColor(Color.WHITE);
                        sociologyCard.setCardBackgroundColor(Color.WHITE);
                        socialWordCard.setCardBackgroundColor(Color.WHITE);
                        break;

                    case 4:
                        socialSciViewPager.setCurrentItem(4, true);
                        anthropologyCard.setCardBackgroundColor(Color.WHITE);
                        archaeologyCard.setCardBackgroundColor(Color.WHITE);
                        economicsCard.setCardBackgroundColor(Color.WHITE);
                        geographyCard.setCardBackgroundColor(Color.WHITE);
                        politicalCard.setCardBackgroundColor(Color.YELLOW);
                        psychologyCard.setCardBackgroundColor(Color.WHITE);
                        sociologyCard.setCardBackgroundColor(Color.WHITE);
                        socialWordCard.setCardBackgroundColor(Color.WHITE);
                        break;

                    case 5:
                        socialSciViewPager.setCurrentItem(5, true);
                        anthropologyCard.setCardBackgroundColor(Color.WHITE);
                        archaeologyCard.setCardBackgroundColor(Color.WHITE);
                        economicsCard.setCardBackgroundColor(Color.WHITE);
                        geographyCard.setCardBackgroundColor(Color.WHITE);
                        politicalCard.setCardBackgroundColor(Color.WHITE);
                        psychologyCard.setCardBackgroundColor(Color.YELLOW);
                        sociologyCard.setCardBackgroundColor(Color.WHITE);
                        socialWordCard.setCardBackgroundColor(Color.WHITE);
                        break;

                    case 6:
                        socialSciViewPager.setCurrentItem(6, true);
                        anthropologyCard.setCardBackgroundColor(Color.WHITE);
                        archaeologyCard.setCardBackgroundColor(Color.WHITE);
                        economicsCard.setCardBackgroundColor(Color.WHITE);
                        geographyCard.setCardBackgroundColor(Color.WHITE);
                        politicalCard.setCardBackgroundColor(Color.WHITE);
                        psychologyCard.setCardBackgroundColor(Color.WHITE);
                        sociologyCard.setCardBackgroundColor(Color.YELLOW);
                        socialWordCard.setCardBackgroundColor(Color.WHITE);
                        break;

                    case 7:
                        socialSciViewPager.setCurrentItem(7, true);
                        anthropologyCard.setCardBackgroundColor(Color.WHITE);
                        archaeologyCard.setCardBackgroundColor(Color.WHITE);
                        economicsCard.setCardBackgroundColor(Color.WHITE);
                        geographyCard.setCardBackgroundColor(Color.WHITE);
                        politicalCard.setCardBackgroundColor(Color.WHITE);
                        psychologyCard.setCardBackgroundColor(Color.WHITE);
                        sociologyCard.setCardBackgroundColor(Color.WHITE);
                        socialWordCard.setCardBackgroundColor(Color.YELLOW);
                        break;
                }
            }
        });

    }

}
