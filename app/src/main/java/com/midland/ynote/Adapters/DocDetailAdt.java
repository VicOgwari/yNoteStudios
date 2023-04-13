package com.midland.ynote.Adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.midland.ynote.Fragments.FirstDetails;
import com.midland.ynote.Fragments.SecondDetails;
import com.google.android.material.tabs.TabLayout;
import com.midland.ynote.Fragments.FirstDetails;

public class DocDetailAdt extends FragmentPagerAdapter {


    int pages;
    Context c;
    String schoolName; String dept; String mainField; String subField; String flag;
    String knowledgeBase; String semester; String institution; String unitCode; String docDetail;
    String docName; String docUri; String docSize;
    ArrayAdapter<CharSequence> semAdapter, institutionsAdapter, schoolsAdapter;
    FragmentManager fm;
    Button next;
    TabLayout tabs;

    public DocDetailAdt(@NonNull FragmentManager fm, int behavior, int tabs, Context c, String docName, String docUri, String docSize, String schoolName, String dept, String mainField, String subField, String flag,
                        String knowledgeBase, String semester, String institution, String unitCode, String docDetail,
                        ArrayAdapter<CharSequence> semAdapter, ArrayAdapter<CharSequence> institutionsAdapter, ArrayAdapter<CharSequence> schoolsAdapter, Button next, TabLayout tab) {
        super(fm, behavior);
        this.fm = fm;
        this.pages = tabs;
        this.c = c;
        this.docName = docName;
        this.docUri = docUri;
        this.docSize = docSize;
        this.schoolName = schoolName;
        this.dept = dept;
        this.knowledgeBase = knowledgeBase;
        this.semester = semester;
        this.institution = institution;
        this.unitCode = unitCode;
        this.docDetail = docDetail;
        this.flag = flag;
        this.mainField = mainField;
        this.subField = subField;
        this.semAdapter = semAdapter;
        this.institutionsAdapter = institutionsAdapter;
        this.schoolsAdapter = schoolsAdapter;
        this.next = next;
        this.tabs = tab;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new FirstDetails(schoolName, dept, mainField, subField, flag, knowledgeBase, semester, institution,
                        semAdapter, institutionsAdapter, schoolsAdapter, tabs);

            case 1:
                return new SecondDetails(c, docName, docUri, docSize, unitCode, docDetail, fm, next, tabs);
        }
        return null;
    }

    @Override
    public int getCount() {
        return pages;
    }

}

