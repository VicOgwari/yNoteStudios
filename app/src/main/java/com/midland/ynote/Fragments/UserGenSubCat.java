package com.midland.ynote.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.midland.ynote.Adapters.TagsAdapter;
import com.midland.ynote.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserGenSubCat#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserGenSubCat extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<String> subFields;

    public UserGenSubCat() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserGenSubCat.
     */
    // TODO: Rename and change types and number of parameters
    public static UserGenSubCat newInstance(String param1, String param2) {
        UserGenSubCat fragment = new UserGenSubCat();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_gen_sub_cat, container, false);

        subFields = new ArrayList<>();

        Button bestPractice = v.findViewById(R.id.bestPrac);
        Button addTag = v.findViewById(R.id.addTag);
        EditText userGenTagET = v.findViewById(R.id.userGenTagET);
        LinearLayout bestPracticeLay = v.findViewById(R.id.bestPracLay);
        RecyclerView userGenTagsRV = v.findViewById(R.id.userGenTagsRV);

        userGenTagsRV.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));
        TagsAdapter subFieldAdt = new TagsAdapter(getContext(), subFields);
        userGenTagsRV.setAdapter(subFieldAdt);

        bestPractice.setOnClickListener(v2 ->{
            if (bestPracticeLay.getVisibility() == View.GONE){
                bestPracticeLay.setVisibility(View.VISIBLE);
        }else if (bestPracticeLay.getVisibility() == View.VISIBLE){
                bestPracticeLay.setVisibility(View.GONE);
            }
        });

        addTag.setOnClickListener(v1 -> {
            if (userGenTagET.getText().toString().trim().equals("")){
                Toast.makeText(getContext(), "Please add a tag..", Toast.LENGTH_SHORT).show();
            }else {
                subFields.add(userGenTagET.getText().toString().trim());
                userGenTagET.setText("");
                Toast.makeText(getContext(), "Added!", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
}