package com.midland.ynote.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.midland.ynote.R;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.midland.ynote.Utilities.DocSorting;
import com.midland.ynote.Utilities.FilingSystem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstDetails extends Fragment implements AdapterView.OnItemSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Spinner institutionSpinner, mainFieldSpinnerTV, subFiledSpinnerTV, semSpinner;
    private TextView subFieldTV, mainFieldTV;
    private EditText institutionSpinnerET;
    private String schoolName, dept, mainField, subField, flag, knowledgeBase, semester, institution;
    private Context c;
    private Button next;
    private TabLayout tabs;
    private CheckBox yr1, yr2, yr3, yr4, yr5, masters, phD, cert, dip, higherDip, bridging;
    ArrayAdapter<CharSequence> semAdapter, institutionsAdapter, schoolsAdapter;

    public FirstDetails() {
        // Required empty public constructor
    }

    public FirstDetails(String schoolName, String dept, String mainField, String subField, String flag,
                        String knowledgeBase, String semester, String institution,
                        ArrayAdapter<CharSequence> semAdapter,
                        ArrayAdapter<CharSequence> institutionsAdapter,
                        ArrayAdapter<CharSequence> schoolsAdapter, TabLayout tabs) {

        this.schoolName = schoolName;
        this.dept = dept;
        this.mainField = mainField;
        this.subField = subField;
        this.flag = flag;
        this.knowledgeBase = knowledgeBase;
        this.semester = semester;
        this.institution = institution;
        this.semAdapter = semAdapter;
        this.institutionsAdapter = institutionsAdapter;
        this.schoolsAdapter = schoolsAdapter;
        this.tabs = tabs;
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstDetails newInstance(String param1, String param2) {
        FirstDetails fragment = new FirstDetails();
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
        View v = inflater.inflate(R.layout.fragment_first_details, container, false);
        knowledgeBase = "";
        yr1 = v.findViewById(R.id.yr1);
        yr2 = v.findViewById(R.id.yr2);
        yr3 = v.findViewById(R.id.yr3);
        yr4 = v.findViewById(R.id.yr4);
        yr5 = v.findViewById(R.id.yr5);
        cert = v.findViewById(R.id.cert);
        next = v.findViewById(R.id.next);
        dip = v.findViewById(R.id.dip);
        masters = v.findViewById(R.id.masters);
        phD = v.findViewById(R.id.phd);
        higherDip = v.findViewById(R.id.higherDip);
        bridging = v.findViewById(R.id.bridging);
        mainFieldTV = v.findViewById(R.id.mainFieldSpinner);
        subFieldTV = v.findViewById(R.id.subFieldSpinner);
        semSpinner = v.findViewById(R.id.semSpinner);

        SharedPreferences preferences = getContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        String json = preferences.getString("User", "");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> userDetail = gson.fromJson(json, type);


        semSpinner.setOnItemSelectedListener(this);

        semSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                semester = (String) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        semAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        semSpinner.setAdapter(semAdapter);

        yr1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            knowledgeBase = "";
            yr2.setChecked(false);
            yr3.setChecked(false);
            yr4.setChecked(false);
            yr5.setChecked(false);
            masters.setChecked(false);
            phD.setChecked(false);
            cert.setChecked(false);
            dip.setChecked(false);
            higherDip.setChecked(false);
            bridging.setChecked(false);
            knowledgeBase = "Yr 1";
        });

        yr2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            knowledgeBase = "";
            yr1.setChecked(false);
            yr3.setChecked(false);
            yr4.setChecked(false);
            yr5.setChecked(false);
            masters.setChecked(false);
            phD.setChecked(false);
            cert.setChecked(false);
            dip.setChecked(false);
            higherDip.setChecked(false);
            bridging.setChecked(false);
            knowledgeBase = "Yr 2";
        });

        yr3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            knowledgeBase = "";
            yr2.setChecked(false);
            yr1.setChecked(false);
            yr4.setChecked(false);
            yr5.setChecked(false);
            masters.setChecked(false);
            phD.setChecked(false);
            cert.setChecked(false);
            dip.setChecked(false);
            higherDip.setChecked(false);
            bridging.setChecked(false);
            knowledgeBase = "Yr 3";
        });

        yr4.setOnCheckedChangeListener((buttonView, isChecked) -> {
            knowledgeBase = "";
            yr2.setChecked(false);
            yr3.setChecked(false);
            yr1.setChecked(false);
            yr5.setChecked(false);
            masters.setChecked(false);
            phD.setChecked(false);
            cert.setChecked(false);
            dip.setChecked(false);
            higherDip.setChecked(false);
            bridging.setChecked(false);
            knowledgeBase = "Yr 4";
        });

        yr5.setOnCheckedChangeListener((buttonView, isChecked) -> {
            knowledgeBase = "";
            yr2.setChecked(false);
            yr3.setChecked(false);
            yr4.setChecked(false);
            yr1.setChecked(false);
            masters.setChecked(false);
            phD.setChecked(false);
            cert.setChecked(false);
            dip.setChecked(false);
            higherDip.setChecked(false);
            bridging.setChecked(false);
            knowledgeBase = "Yr 5";
        });

        cert.setOnCheckedChangeListener((buttonView, isChecked) -> {
            knowledgeBase = "";
            yr2.setChecked(false);
            yr3.setChecked(false);
            yr4.setChecked(false);
            yr1.setChecked(false);
            masters.setChecked(false);
            phD.setChecked(false);
            yr5.setChecked(false);
            dip.setChecked(false);
            higherDip.setChecked(false);
            bridging.setChecked(false);
            knowledgeBase = "Certificate";
        });

        dip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            knowledgeBase = "";
            yr2.setChecked(false);
            yr3.setChecked(false);
            yr4.setChecked(false);
            yr1.setChecked(false);
            masters.setChecked(false);
            phD.setChecked(false);
            cert.setChecked(false);
            yr5.setChecked(false);
            higherDip.setChecked(false);
            bridging.setChecked(false);
            knowledgeBase = "Diploma";
        });

        masters.setOnCheckedChangeListener((buttonView, isChecked) -> {
            knowledgeBase = "";
            yr2.setChecked(false);
            yr3.setChecked(false);
            yr4.setChecked(false);
            yr1.setChecked(false);
            yr5.setChecked(false);
            phD.setChecked(false);
            cert.setChecked(false);
            dip.setChecked(false);
            higherDip.setChecked(false);
            bridging.setChecked(false);
            knowledgeBase = "Masters";
        });

        phD.setOnCheckedChangeListener((buttonView, isChecked) -> {
            knowledgeBase = "";
            yr2.setChecked(false);
            yr3.setChecked(false);
            yr4.setChecked(false);
            yr1.setChecked(false);
            yr5.setChecked(false);
            masters.setChecked(false);
            cert.setChecked(false);
            dip.setChecked(false);
            higherDip.setChecked(false);
            bridging.setChecked(false);
            knowledgeBase = "phD";
        });

        higherDip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            knowledgeBase = "";
            yr2.setChecked(false);
            yr3.setChecked(false);
            yr4.setChecked(false);
            yr1.setChecked(false);
            yr5.setChecked(false);
            masters.setChecked(false);
            phD.setChecked(false);
            cert.setChecked(false);
            dip.setChecked(false);
            bridging.setChecked(false);
            knowledgeBase = "Higher diploma";
        });

        bridging.setOnCheckedChangeListener((buttonView, isChecked) -> {
            knowledgeBase = "";
            yr2.setChecked(false);
            yr3.setChecked(false);
            yr4.setChecked(false);
            yr1.setChecked(false);
            yr5.setChecked(false);
            masters.setChecked(false);
            phD.setChecked(false);
            cert.setChecked(false);
            dip.setChecked(false);
            higherDip.setChecked(false);
            knowledgeBase = "Bridging";
        });

        institutionSpinner = v.findViewById(R.id.institutionSpinner);
        institutionSpinner.bringToFront();
        institutionSpinnerET = v.findViewById(R.id.institutionSpinnerET);
        institutionSpinner.setOnItemSelectedListener(this);

        institutionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        institutionSpinner.setAdapter(institutionsAdapter);
        if (userDetail != null && userDetail.get(5) != null) {
            institutionSpinner.setSelection(institutionsAdapter.getPosition(userDetail.get(5)));
        }

        institutionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                institution = (String) parent.getItemAtPosition(position);
                if (institution.equals("Type my institution")) {
                    institutionSpinnerET.setVisibility(View.VISIBLE);
                } else {
                    institutionSpinnerET.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (flag != null) {
            subFiledSpinnerTV = v.findViewById(R.id.subFieldSpinnerTV);
            subFiledSpinnerTV.bringToFront();
            subFiledSpinnerTV.setOnItemSelectedListener(this);
            subFiledSpinnerTV.setVisibility(View.VISIBLE);

            if (flag.equals("mainHome")) {
                mainFieldSpinnerTV = v.findViewById(R.id.mainFieldSpinnerTV);
                mainFieldSpinnerTV.bringToFront();

                mainFieldSpinnerTV.setOnItemSelectedListener(this);

                mainFieldSpinnerTV.setVisibility(View.VISIBLE);
                mainFieldTV.setVisibility(View.GONE);
                subFieldTV.setVisibility(View.GONE);

                schoolsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mainFieldSpinnerTV.setAdapter(schoolsAdapter);

                if (userDetail != null && userDetail.get(6) != null) {
                    mainFieldSpinnerTV.setSelection(schoolsAdapter.getPosition(userDetail.get(6)));
                }
                mainFieldSpinnerTV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mainField = parent.getItemAtPosition(position).toString();
                        ArrayList<String> subFields = new ArrayList<>(Arrays.asList(DocSorting.getSubFields(position)));
                        if (subFields.size() == 0) {
                            subFields.add("-select a field-");
                        } else {
                            subFields.add(0, "-select a field-");
                        }
                        ArrayAdapter<String> subFieldAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_drop_down_yangu1, subFields);
                        subFieldAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        subFieldAdapter.notifyDataSetChanged();
                        subFiledSpinnerTV.setAdapter(subFieldAdapter);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            } else if (flag.equals("home")) {
                if (schoolName != null) {
                    mainFieldTV.setText(schoolName);
                    int pos = schoolsAdapter.getPosition(schoolName);
                    ArrayList<String> subFields = new ArrayList<>(Arrays.asList(DocSorting.getSubFields(pos)));
                    if (subFields.size() == 0) {
                        subFields.add("-select a field-");
                    } else {
                        subFields.add(0, "-select a field-");
                    }
                    ArrayAdapter<String> subFieldAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_drop_down_yangu1, subFields);
                    subFieldAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    subFieldAdapter.notifyDataSetChanged();
                    subFiledSpinnerTV.setAdapter(subFieldAdapter);
                    mainFieldTV.setVisibility(View.VISIBLE);
                    subFieldTV.setText(dept);
                    subFieldTV.setVisibility(View.GONE);
                    subFiledSpinnerTV.setVisibility(View.VISIBLE);

                }
            }

            subFiledSpinnerTV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    subField = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }


        next.setOnClickListener(v1 -> {
            if (knowledgeBase.equals("")) {
                Toast.makeText(getContext(), "Select a knowledge base.", Toast.LENGTH_SHORT).show();
            } else if (subField.equals("-select a field-")) {
                Toast.makeText(getContext(), "Please provide department details.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), subField, Toast.LENGTH_LONG).show();

                if (mainFieldTV.getVisibility() == View.VISIBLE) {
                    mainField = mainFieldTV.getText().toString();
                }

                FilingSystem.Companion.setKnowledgeBase(knowledgeBase);
                if (dept == null) {
                    FilingSystem.Companion.setDept(subField);
                    FilingSystem.Companion.setSubField(subField);
                } else {
                    FilingSystem.Companion.setDept(dept);
                    FilingSystem.Companion.setSubField(dept);
                }

                FilingSystem.Companion.setSemester(semester);
                FilingSystem.Companion.setInstitution(institution);
                FilingSystem.Companion.setMainField(mainField);

                tabs.getTabAt(1).select();
            }

        });
        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ArrayAdapter<String> subFieldAdapter = new ArrayAdapter<>(getContext(),
                R.layout.spinner_drop_down_yangu1,
                new ArrayList<>(Arrays.
                        asList(DocSorting.getSubFields(position))));
        subFieldAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subFieldAdapter.notifyDataSetChanged();
        subFiledSpinnerTV.setAdapter(subFieldAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public String getKnowledgeBase() {
        return knowledgeBase;
    }

    public String getMainField() {
        return mainField;
    }

    public String getSubField() {
        return subField;
    }

    public String getSemester() {
        return semester;
    }

    public String getInstitution() {
        return institution;
    }


}