package com.midland.ynote.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.midland.ynote.Activities.Library;
import com.midland.ynote.Activities.SourceDocList;
import com.midland.ynote.Adapters.MainPagerAdapter;
import com.midland.ynote.R;

public class HomeFragment extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ViewModelProviders.of(this).get(HomeViewModel.class);

        final View root = inflater.inflate(R.layout.home_activity, container, false);
        ViewPager homeViewPager = root.findViewById(R.id.homeViewPager);

        homeViewPager.setAdapter(new MainPagerAdapter(getParentFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, 2));

        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 7) {
            startActivity(new Intent(getContext(), SourceDocList.class));

        }

        if (requestCode == 8) {
            startActivity(new Intent(getContext(), Library.class));
        }

    }


}