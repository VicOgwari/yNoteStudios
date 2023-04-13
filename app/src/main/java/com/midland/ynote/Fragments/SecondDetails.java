package com.midland.ynote.Fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.midland.ynote.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.midland.ynote.Adapters.TagsAdapter;
import com.midland.ynote.Dialogs.LectureSubCategoryDialog;
import com.midland.ynote.Dialogs.LogInSignUp;
import com.midland.ynote.Dialogs.PublishApproval;
import com.midland.ynote.Utilities.FilingSystem;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecondDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondDetails extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String pastPaper;
    private EditText unitCodeEditText, docDetails;
    private String unitCode, docDetail, docName, docUri, docSize;
    private CheckBox no, yes;
    private Button relevance, next, publish, cancel;
    private ImageButton back;
    private Context c;
    private Activity a;
    private TabLayout tabs;
    private FragmentManager fm;

    public SecondDetails() {
        // Required empty public constructor
    }

    public SecondDetails(Context c, String docName, String docUri, String docSize, String unitCode, String docDetail, FragmentManager fm, Button next, TabLayout tabs) {
        this.c = c;
        this.docName = docName;
        this.docUri = docUri;
        this.docSize = docSize;
        this.unitCode = unitCode;
        this.docDetail = docDetail;
        this.fm = fm;
        this.next = next;
        this.tabs = tabs;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecondDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static SecondDetails newInstance(String param1, String param2) {
        SecondDetails fragment = new SecondDetails();
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
        View v = inflater.inflate(R.layout.fragment_second_details, container, false);
        unitCodeEditText = v.findViewById(R.id.unitCodeEditText);
        docDetails = v.findViewById(R.id.docDetails);
        relevance = v.findViewById(R.id.relevance);
        back = v.findViewById(R.id.back);
        publish = v.findViewById(R.id.publish);
        cancel = v.findViewById(R.id.cancel);
        no = v.findViewById(R.id.checkboxNo);
        yes = v.findViewById(R.id.checkboxYes);

        docDetail = docDetails.getText().toString();
        unitCode = unitCodeEditText.getText().toString();

        no.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (buttonView.isChecked()){
                yes.setChecked(false);
                pastPaper = "False";
            }
        }));
        yes.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if (buttonView.isChecked()){
                no.setChecked(false);
                pastPaper = "True";
            }
        }));

        back.setOnClickListener(v13 -> tabs.getTabAt(0).select());
        relevance.setOnClickListener(v1 -> {
            TagsAdapter tagsAdapter = new TagsAdapter(c, FilingSystem.Companion.getAllTags());
            FragmentTransaction ft = fm.beginTransaction();
            Fragment prev = fm.findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            DialogFragment dialogFragment = new LectureSubCategoryDialog(tagsAdapter);
            dialogFragment.show(fm, "dialog");

        });

        publish.setOnClickListener(v12 -> {

            unitCode = unitCodeEditText.getText().toString().trim();
            docDetail = docDetails.getText().toString().trim();
            if (tabs.getSelectedTabPosition() == 1){
                if (pastPaper == null || pastPaper.equals("")){
                    Toast.makeText(getContext(), "Is this document a past paper?.", Toast.LENGTH_SHORT).show();
                }else
                if (unitCode.equals("") && docDetail.equals("")){
                    Toast.makeText(getContext(), "Fill in at least one slot.", Toast.LENGTH_SHORT).show();
                }else {
                    FilingSystem.Companion.setUnitCode(unitCode);
                    FilingSystem.Companion.setDocDetail(docDetail);
                    FilingSystem.Companion.getAllTags().add( unitCode);
                    FilingSystem.Companion.getAllTags().add(docDetail);
                    String docMetaData = FilingSystem.Companion.getMainField() + "_-_"
                            + FilingSystem.Companion.getSubField() + "_-_" + FilingSystem.Companion.getKnowledgeBase()
                            + "_-_" + FilingSystem.Companion.getDocDetail()
                            + "_-_" + FilingSystem.Companion.getUnitCode()
                            + "_-_" + docName + "_-_"
                            + FilingSystem.Companion.getAllTags() + "_-_" + docSize;

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user == null){
                        LogInSignUp logInSignUp = new LogInSignUp(getContext());
                        logInSignUp.show();
                    }else {
                        FilingSystem.Companion.getAllTags().add("Past papers");
                        PublishApproval publishApproval1 = new PublishApproval(getContext(),
                                docMetaData, FilingSystem.Companion.getInstitution(), FilingSystem.Companion.getSemester(),
                                Uri.parse(docUri), null, FirebaseAuth.getInstance().getCurrentUser(),
                                FilingSystem.Companion.getUnitCode(), FilingSystem.Companion.getDocDetail(),
                                FilingSystem.Companion.getAllTags());

                        publishApproval1.show();
                    }
                }
            }
        });

        cancel.setOnClickListener(v14 -> {

        });
        return v;
    }

}