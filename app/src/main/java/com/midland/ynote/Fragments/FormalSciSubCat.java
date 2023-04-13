package com.midland.ynote.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.midland.ynote.Adapters.TagsPagerAdapter;
import com.midland.ynote.R;

public class FormalSciSubCat extends Fragment {

    private ViewPager formalViewPager;
    private TextView stringBuilder, compSci, math, stats;
    private CardView compCard, mathCard, statsCard;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_formal_sci, container, false);

        TagsPagerAdapter tagsPagerAdapter = new TagsPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, 3, "formalSci");


        compSci = v.findViewById(R.id.compSci);
        math = v.findViewById(R.id.math);
        stats = v.findViewById(R.id.stats);
        stringBuilder = v.findViewById(R.id.stringBuilder);



        formalViewPager = v.findViewById(R.id.formalSciViewPager);
        formalViewPager.setAdapter(tagsPagerAdapter);

        formalViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                formalViewPager.setCurrentItem(formalViewPager.getCurrentItem());
                return false;
            }
        });


        compCard = v.findViewById(R.id.compSciCard);
        compCard.setCardBackgroundColor(Color.YELLOW);
        mathCard = v.findViewById(R.id.mathCard);
        statsCard = v.findViewById(R.id.statisticsCard);

        clickListener(compCard, 0);
        clickListener(mathCard, 1);
        clickListener(statsCard, 2);


        return v;
    }

    private void clickListener(CardView card, final int flag){

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (flag){
                    case 0:
                        formalViewPager.setCurrentItem(0, true);
                        mathCard.setCardBackgroundColor(Color.WHITE);
                        statsCard.setCardBackgroundColor(Color.WHITE);
                        compCard.setCardBackgroundColor(Color.YELLOW);
//                        if (stringBuilder.getText().toString().contains("Computer science")){
//                            stringBuilder.setText(stringBuilder.getText().toString().replace("Computer science_-_", ""));
//                            compCard.setCardBackgroundColor(Color.WHITE);
//                        }else {
//                            stringBuilder.setText(stringBuilder.getText().toString() + "Computer science_-_");
//                            compCard.setCardBackgroundColor(Color.YELLOW);
//
//                        }
                        break;

                    case 1:
                        formalViewPager.setCurrentItem(1, true);
                        mathCard.setCardBackgroundColor(Color.YELLOW);
                        statsCard.setCardBackgroundColor(Color.WHITE);
                        compCard.setCardBackgroundColor(Color.WHITE);

                        break;

                    case 2:
                        formalViewPager.setCurrentItem(2, true);
                        mathCard.setCardBackgroundColor(Color.WHITE);
                        statsCard.setCardBackgroundColor(Color.YELLOW);
                        compCard.setCardBackgroundColor(Color.WHITE);
                        break;
                }
            }
        });

    }

}
