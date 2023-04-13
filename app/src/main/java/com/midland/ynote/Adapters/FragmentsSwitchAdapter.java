package com.midland.ynote.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.midland.ynote.Fragments.BitMaps;
import com.midland.ynote.Fragments.Documents;
import com.midland.ynote.Fragments.Lectures;
import com.midland.ynote.Fragments.PhotoDocs;
import com.midland.ynote.Fragments.PhotoDocsComments;
import com.midland.ynote.Fragments.SearchDocs;
import com.midland.ynote.Fragments.SearchUsers;
import com.midland.ynote.Fragments.Studio;

import java.util.ArrayList;


public class FragmentsSwitchAdapter extends FragmentPagerAdapter {


    private final int tabsNumber;
    private final String flag;
    private String publisherId;

    private ArrayList<String> narrations;
    private ArrayList<String> descriptions;
    private ArrayList<String> cloudPictures;
    private Context con;
    private String fileName;
    private String mainField, subField;

    public FragmentsSwitchAdapter(@NonNull FragmentManager fm, int behavior, int tabs, String flag) {
        super(fm, behavior);
        this.tabsNumber = tabs;
        this.flag = flag;
    }

    public FragmentsSwitchAdapter(@NonNull FragmentManager fm, int behavior, int tabsNumber,
                                  Context con, String fileName, String flag,
                                  ArrayList<String> narrations, ArrayList<String> descriptions,
                                  ArrayList<String> cloudPictures, String publisherId) {
        super(fm, behavior);
        this.tabsNumber = tabsNumber;
        this.con = con;
        this.fileName = fileName;
        this.flag = flag;
        this.publisherId = publisherId;
        this.narrations = narrations;
        this.descriptions = descriptions;
        this.cloudPictures = cloudPictures;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        if (flag.equals("Library")){
            switch (position) {
                case 0:
                    return new BitMaps();

                case 1:
                    return new Documents();

            }
        }else
            if (flag.equals("Production")){
            switch (position) {
                case 0:
                    return new Studio();

                case 1:
                    return new Lectures();
            }
        }else
                if (flag.equals("Search")){
                switch (position) {
                    case 0:
                        return new SearchDocs();

                    case 1:
                        return new SearchUsers();

                }
            }else
                    if (flag.equals("Pictorials")){
                switch (position) {
                    case 0:
                        return new PhotoDocs(cloudPictures, narrations, descriptions, fileName);

                    case 1:
                        return new PhotoDocsComments(con, fileName, publisherId);

                }
            }
        return null;
    }

    @Override
    public int getCount() {
        return tabsNumber;
    }

}
