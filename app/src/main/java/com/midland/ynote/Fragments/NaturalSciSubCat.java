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

public class NaturalSciSubCat extends Fragment {

    private ViewPager naturalViewPager;
    private CardView biologyCard, chemistryCard, earthCard, spaceCard, physicsCard;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_natural_sci, container, false);
        naturalViewPager = v.findViewById(R.id.naturalSciViewPager);
        TagsPagerAdapter tagsPagerAdapter = new TagsPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, 5, "naturalSci");
        naturalViewPager.setAdapter(tagsPagerAdapter);

        naturalViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                naturalViewPager.setCurrentItem(naturalViewPager.getCurrentItem());
                return false;
            }
        });

        biologyCard = v.findViewById(R.id.bioCard);
        biologyCard.setCardBackgroundColor(Color.YELLOW);
        chemistryCard = v.findViewById(R.id.chemCard);
        earthCard = v.findViewById(R.id.earthSciCard);
        spaceCard = v.findViewById(R.id.spaceSciCard);
        physicsCard = v.findViewById(R.id.physicsCard);


        clickListener(biologyCard, 0);
        clickListener(chemistryCard, 1);
        clickListener(earthCard, 2);
        clickListener(spaceCard, 3);
        clickListener(physicsCard, 4);


        return v;
    }

    private void clickListener(CardView card, final int flag){

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (flag){
                    case 0:
                        naturalViewPager.setCurrentItem(0, true);
                        biologyCard.setCardBackgroundColor(Color.YELLOW);
                        chemistryCard.setCardBackgroundColor(Color.WHITE);
                        earthCard.setCardBackgroundColor(Color.WHITE);
                        spaceCard.setCardBackgroundColor(Color.WHITE);
                        physicsCard.setCardBackgroundColor(Color.WHITE);
                        break;

                    case 1:
                        naturalViewPager.setCurrentItem(1, true);
                        biologyCard.setCardBackgroundColor(Color.WHITE);
                        chemistryCard.setCardBackgroundColor(Color.YELLOW);
                        earthCard.setCardBackgroundColor(Color.WHITE);
                        spaceCard.setCardBackgroundColor(Color.WHITE);
                        physicsCard.setCardBackgroundColor(Color.WHITE);
                        break;

                    case 2:
                        naturalViewPager.setCurrentItem(2, true);
                        biologyCard.setCardBackgroundColor(Color.WHITE);
                        chemistryCard.setCardBackgroundColor(Color.WHITE);
                        earthCard.setCardBackgroundColor(Color.YELLOW);
                        spaceCard.setCardBackgroundColor(Color.WHITE);
                        physicsCard.setCardBackgroundColor(Color.WHITE);
                        break;

                    case 3:
                        naturalViewPager.setCurrentItem(3, true);
                        biologyCard.setCardBackgroundColor(Color.WHITE);
                        chemistryCard.setCardBackgroundColor(Color.WHITE);
                        earthCard.setCardBackgroundColor(Color.WHITE);
                        spaceCard.setCardBackgroundColor(Color.YELLOW);
                        physicsCard.setCardBackgroundColor(Color.WHITE);
                        break;

                    case 4:
                        naturalViewPager.setCurrentItem(4, true);
                        biologyCard.setCardBackgroundColor(Color.WHITE);
                        chemistryCard.setCardBackgroundColor(Color.WHITE);
                        earthCard.setCardBackgroundColor(Color.WHITE);
                        spaceCard.setCardBackgroundColor(Color.WHITE);
                        physicsCard.setCardBackgroundColor(Color.YELLOW);
                        break;

                }
            }
        });

    }

}
