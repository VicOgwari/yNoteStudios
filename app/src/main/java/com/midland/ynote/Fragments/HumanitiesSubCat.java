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

public class HumanitiesSubCat extends Fragment {

    private ViewPager humanitiesViewPager;
    private CardView performingCard, visualCard, historyCard, homeEconCard, languagesCard, lawCard, philosophyCard, theologyCard;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_humanites, container, false);

        performingCard = v.findViewById(R.id.performingArtsCard);
        performingCard.setCardBackgroundColor(Color.YELLOW);
        visualCard = v.findViewById(R.id.visualArtsCard);
        historyCard = v.findViewById(R.id.historyCard);
        homeEconCard = v.findViewById(R.id.homeEconCard);
        languagesCard = v.findViewById(R.id.languagesCard);
        lawCard = v.findViewById(R.id.lawCard);
        philosophyCard = v.findViewById(R.id.philosophyCard);
        theologyCard = v.findViewById(R.id.theologyCard);

        clickListener(performingCard, 0);
        clickListener(visualCard, 1);
        clickListener(historyCard, 2);
        clickListener(homeEconCard, 3);
        clickListener(languagesCard, 4);
        clickListener(philosophyCard, 6);
        clickListener(theologyCard, 7);



        humanitiesViewPager = v.findViewById(R.id.humanitiesViewPager);
        TagsPagerAdapter tagsPagerAdapter = new TagsPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, 8, "humanities");
        humanitiesViewPager.setAdapter(tagsPagerAdapter);

        humanitiesViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                humanitiesViewPager.setCurrentItem(humanitiesViewPager.getCurrentItem());
                return false;
            }
        });


        return v;
    }

    private void clickListener(CardView card, final int flag){

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (flag){
                    case 0:
                        humanitiesViewPager.setCurrentItem(0, true);
                        performingCard.setCardBackgroundColor(Color.YELLOW);
                        visualCard.setCardBackgroundColor(Color.WHITE);
                        historyCard.setCardBackgroundColor(Color.WHITE);
                        homeEconCard.setCardBackgroundColor(Color.WHITE);
                        languagesCard.setCardBackgroundColor(Color.WHITE);
                        lawCard.setCardBackgroundColor(Color.WHITE);
                        philosophyCard.setCardBackgroundColor(Color.WHITE);
                        theologyCard.setCardBackgroundColor(Color.WHITE);

                        break;

                    case 1:
                        humanitiesViewPager.setCurrentItem(1, true);
                        performingCard.setCardBackgroundColor(Color.WHITE);
                        visualCard.setCardBackgroundColor(Color.YELLOW);
                        historyCard.setCardBackgroundColor(Color.WHITE);
                        homeEconCard.setCardBackgroundColor(Color.WHITE);
                        languagesCard.setCardBackgroundColor(Color.WHITE);
                        lawCard.setCardBackgroundColor(Color.WHITE);
                        philosophyCard.setCardBackgroundColor(Color.WHITE);
                        theologyCard.setCardBackgroundColor(Color.WHITE);

                        break;

                    case 2:
                        humanitiesViewPager.setCurrentItem(2, true);
                        performingCard.setCardBackgroundColor(Color.WHITE);
                        visualCard.setCardBackgroundColor(Color.WHITE);
                        historyCard.setCardBackgroundColor(Color.YELLOW);
                        homeEconCard.setCardBackgroundColor(Color.WHITE);
                        languagesCard.setCardBackgroundColor(Color.WHITE);
                        lawCard.setCardBackgroundColor(Color.WHITE);
                        philosophyCard.setCardBackgroundColor(Color.WHITE);
                        theologyCard.setCardBackgroundColor(Color.WHITE);
                        break;

                    case 3:
                        humanitiesViewPager.setCurrentItem(3, true);
                        performingCard.setCardBackgroundColor(Color.WHITE);
                        visualCard.setCardBackgroundColor(Color.WHITE);
                        historyCard.setCardBackgroundColor(Color.WHITE);
                        homeEconCard.setCardBackgroundColor(Color.YELLOW);
                        languagesCard.setCardBackgroundColor(Color.WHITE);
                        lawCard.setCardBackgroundColor(Color.WHITE);
                        philosophyCard.setCardBackgroundColor(Color.WHITE);
                        theologyCard.setCardBackgroundColor(Color.WHITE);
                        break;

                    case 4:
                        humanitiesViewPager.setCurrentItem(4, true);
                        performingCard.setCardBackgroundColor(Color.WHITE);
                        visualCard.setCardBackgroundColor(Color.WHITE);
                        historyCard.setCardBackgroundColor(Color.WHITE);
                        homeEconCard.setCardBackgroundColor(Color.WHITE);
                        languagesCard.setCardBackgroundColor(Color.YELLOW);
                        lawCard.setCardBackgroundColor(Color.WHITE);
                        philosophyCard.setCardBackgroundColor(Color.WHITE);
                        theologyCard.setCardBackgroundColor(Color.WHITE);
                        break;

                    case 5:
                        humanitiesViewPager.setCurrentItem(5, true);
                        performingCard.setCardBackgroundColor(Color.WHITE);
                        visualCard.setCardBackgroundColor(Color.WHITE);
                        historyCard.setCardBackgroundColor(Color.WHITE);
                        homeEconCard.setCardBackgroundColor(Color.WHITE);
                        languagesCard.setCardBackgroundColor(Color.WHITE);
                        lawCard.setCardBackgroundColor(Color.YELLOW);
                        philosophyCard.setCardBackgroundColor(Color.WHITE);
                        theologyCard.setCardBackgroundColor(Color.WHITE);
                        break;

                    case 6:
                        humanitiesViewPager.setCurrentItem(6, true);
                        performingCard.setCardBackgroundColor(Color.WHITE);
                        visualCard.setCardBackgroundColor(Color.WHITE);
                        historyCard.setCardBackgroundColor(Color.WHITE);
                        homeEconCard.setCardBackgroundColor(Color.WHITE);
                        languagesCard.setCardBackgroundColor(Color.WHITE);
                        lawCard.setCardBackgroundColor(Color.WHITE);
                        philosophyCard.setCardBackgroundColor(Color.YELLOW);
                        theologyCard.setCardBackgroundColor(Color.WHITE);
                        break;

                    case 7:
                        humanitiesViewPager.setCurrentItem(7, true);
                        performingCard.setCardBackgroundColor(Color.WHITE);
                        visualCard.setCardBackgroundColor(Color.WHITE);
                        historyCard.setCardBackgroundColor(Color.WHITE);
                        homeEconCard.setCardBackgroundColor(Color.WHITE);
                        languagesCard.setCardBackgroundColor(Color.WHITE);
                        lawCard.setCardBackgroundColor(Color.WHITE);
                        philosophyCard.setCardBackgroundColor(Color.WHITE);
                        theologyCard.setCardBackgroundColor(Color.YELLOW);
                        break;
                }
            }
        });

    }

}
