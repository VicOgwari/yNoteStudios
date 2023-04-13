package com.midland.ynote.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.midland.ynote.Activities.LecturesList;
import com.midland.ynote.Activities.SchoolDepartmentDocuments;
import com.midland.ynote.Activities.SearchResults;
import com.midland.ynote.Dialogs.LogInSignUp;
import com.midland.ynote.Objects.Schools;
import com.midland.ynote.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchoolsAdapter extends RecyclerView.Adapter<SchoolsAdapter.SchoolsVH> implements Filterable {

    private final Context c;
    private final ArrayList<Schools> schools;
    private final ArrayList<Schools> schoolsList;
    private final String flag;

    public SchoolsAdapter(Context c, ArrayList<Schools> schools, String flag) {
        this.c = c;
        this.schools = schools;
        this.flag = flag;
        schoolsList = new ArrayList<>(schools);
    }

    @NonNull
    @Override
    public SchoolsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (flag != null) {
            if (flag.equals("navDrawer")) {
                return new SchoolsVH(LayoutInflater.from(c).inflate(R.layout.nav_education_item, parent, false));
            } else if (flag.equals("contentSelection") || flag.equals("contentSelection1")) {
                return new SchoolsVH(LayoutInflater.from(c).inflate(R.layout.lecture_filter_grid_item1, parent, false));
            } else {
                return new SchoolsVH(LayoutInflater.from(c).inflate(R.layout.lecture_filter_grid_item, parent, false));
            }
        } else {
            return new SchoolsVH(LayoutInflater.from(c).inflate(R.layout.schools_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final SchoolsVH holder, final int position) {
        final Schools school = schools.get(position);
        final DocumentReference docCount = FirebaseFirestore.getInstance().collection("Count")
                .document("Documents count");
        final DocumentReference userCount = FirebaseFirestore.getInstance().collection("Count")
                .document("Enrollment count");

        if (flag != null) {
            if (flag.equals("contentSelection") || flag.equals("contentSelection1")) {
                Glide.with(c).load(school.getSchoolEmblem()).fitCenter().into(holder.schoolEmblem);
                holder.schoolName.setText(school.getSchoolName());
                holder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(c, SearchResults.class);
                    intent.putExtra("school", school.getSchoolName());
                    intent.putExtra("addDocBtn", "set");
                    if (flag.equals("contentSelection")) {
                        intent.putExtra("flag", "Doc search");
                    } else {
                        intent.putExtra("flag", "Lecture search");
                    }
                    c.startActivity(intent);
                });
            }
        }
        if (flag != null && flag.equals("navDrawer")) {
            holder.fieldTV.setText(school.getSchoolName());
            Glide.with(c).load(school.getSchoolEmblem()).fitCenter().into(holder.fieldIcon);

            holder.fieldTV.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(c, holder.fieldTV);
                popupMenu.getMenuInflater().inflate(R.menu.content_optios, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.documents: {
                            Intent intent = new Intent(c, SearchResults.class);
                            intent.putExtra("school", school.getSchoolName());
                            intent.putExtra("flag", "Doc search");
                            c.startActivity(intent);
                        }
                        case R.id.lectures: {
                            Intent intent = new Intent(c, SearchResults.class);
                            intent.putExtra("school", school.getSchoolName());
                            intent.putExtra("flag", "Lecture search");
                            c.startActivity(intent);
                        }
                    }
                    return false;
                });
            });

            ArrayList<String> loadedChecks = saveChecks("education", null, "Get checks");
            if (loadedChecks != null) {
                holder.fieldCheck.setChecked(loadedChecks.contains(school.getSchoolName()));
            }

            holder.fieldCheck.setOnClickListener(v -> {
                if (holder.fieldCheck.isChecked()) {
                    saveChecks("education", school.getSchoolName(), "Checked");
                } else {
                    saveChecks("education", school.getSchoolName(), "Unchecked");
                }
            });

        }
        else {

            ArrayList<String> docCountArray = new ArrayList<>();
            ArrayList<String> userCountArray = new ArrayList<>();

            userCount.get().addOnSuccessListener(snapshot -> {
                if (snapshot.exists()) {
                    for (Schools sch : schools) {
                        if (!String.valueOf(snapshot.get(sch.getSchoolName())).equals("") && snapshot.get(sch.getSchoolName()) != null) {
                            userCountArray.add(String.valueOf(snapshot.get(sch.getSchoolName())));
                        } else {
                            userCountArray.add(String.valueOf(0));
                        }
                    }
                }
                try {
                    holder.userCount.setText(userCountArray.get(holder.getAbsoluteAdapterPosition()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                holder.userCountProg.setVisibility(View.GONE);
                holder.userCount.setVisibility(View.VISIBLE);
            });
            docCount.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    for (Schools sch : schools) {
                        if (!String.valueOf(documentSnapshot.get(sch.getSchoolName())).equals("") && documentSnapshot.get(sch.getSchoolName()) != null) {
                            docCountArray.add(String.valueOf(documentSnapshot.get(sch.getSchoolName())));
                        } else {
                            docCountArray.add(String.valueOf(0));
                        }
                    }
                }
                try {
                    holder.docCount.setText(docCountArray.get(holder.getAbsoluteAdapterPosition()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                holder.docCountProg.setVisibility(View.GONE);
                holder.docCount.setVisibility(View.VISIBLE);
            });

            if (school.getEnrolled() == Boolean.TRUE) {
                holder.enrolled.setVisibility(View.VISIBLE);
            } else {
                holder.enrolled.setVisibility(View.GONE);
            }

            if (flag != null && !flag.equals("navDrawer")) {
                Glide.with(c).load(school.getSchoolEmblem()).fitCenter().into(holder.schoolLecEmblem);
                holder.schoolLecTitle.setText(school.getSchoolName());
            } else {

                //DOC COUNT & USER COUNT WILL STRESS YOU OUT
                holder.filterButton.setOnClickListener(v -> {
                    Intent intent;
                    String sch = holder.schoolName.getText().toString();
                    if (sch.equals("Confucius Institute")
                            || sch.equals("Peace & Security Studies")){
                        intent = new Intent(c.getApplicationContext(), SearchResults.class);
                        intent.putExtra("school", sch);
                        intent.putExtra("addDocBtn", "set");
                        intent.putExtra("flag", "Doc search");
                    }else {
                        intent = new Intent(c.getApplicationContext(), SchoolDepartmentDocuments.class);
                        intent.putExtra("SchoolName", sch);
                    }
                    c.startActivity(intent);

                });
                Glide.with(c).load(school.getSchoolEmblem()).fitCenter().into(holder.schoolEmblem);
                holder.schoolName.setText(school.getSchoolName());

                holder.schoolOptions.bringToFront();
                holder.schoolOptions.setOnClickListener(v -> {
                    PopupMenu popupMenu = new PopupMenu(c, holder.schoolOptions);
                    popupMenu.getMenuInflater().inflate(R.menu.school_options_menu, popupMenu.getMenu());
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(item -> {
                        switch (item.getItemId()) {
                            case R.id.lectures:
                                Intent intent = new Intent(c, LecturesList.class);
                                intent.putExtra("child", school.getSchoolName());
                                c.startActivity(intent);
                                break;
                        }
                        return false;
                    });

                });


            }
        }


    }

    @Override
    public int getItemCount() {
        return schools.size();
    }


    public static class SchoolsVH extends RecyclerView.ViewHolder {
        TextView schoolName, docCount, userCount, schoolLecTitle, enrolled;
        ImageView schoolEmblem, schoolLecEmblem;
        Button schoolOptions, filterButton;
        ProgressBar userCountProg, docCountProg;
        TextView fieldTV;
        ImageView fieldIcon;
        CheckBox fieldCheck;

        public SchoolsVH(@NonNull View itemView) {
            super(itemView);
            schoolName = itemView.findViewById(R.id.schoolName);
            docCount = itemView.findViewById(R.id.docCount);
            userCount = itemView.findViewById(R.id.userCount);
            schoolEmblem = itemView.findViewById(R.id.schoolEmblem);
            schoolOptions = itemView.findViewById(R.id.schoolOptions);
            enrolled = itemView.findViewById(R.id.enrolled);
            filterButton = itemView.findViewById(R.id.filterCard);

            docCountProg = itemView.findViewById(R.id.docCountProg);
            userCountProg = itemView.findViewById(R.id.userCountProg);

            schoolLecEmblem = itemView.findViewById(R.id.schoolEmblem);
            schoolLecTitle = itemView.findViewById(R.id.schoolName);

            fieldCheck = itemView.findViewById(R.id.fieldCheck);
            fieldTV = itemView.findViewById(R.id.fieldTV);
            fieldIcon = itemView.findViewById(R.id.fieldIcon);

        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Schools> filteredSchools = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredSchools.addAll(schoolsList);
            } else {
                String filteredPattern = constraint.toString().toLowerCase().trim();
                for (Schools school : schoolsList) {
                    if (school.getSchoolName().toLowerCase().startsWith(filteredPattern)) {
                        filteredSchools.add(school);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredSchools;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            schools.clear();
            schools.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    private ArrayList<String> saveChecks(String education, String field, String flag) {
        SharedPreferences preferences = c.getSharedPreferences(education, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        ArrayList<String> sch = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DocumentReference userCount1 = FirebaseFirestore.getInstance().collection("Count")
                .document("Enrollment count");

        switch (flag) {
            case "Checked": {
                Map<String, Object> map = new HashMap<>();
                map.put(field, FieldValue.increment(1));
                userCount1.update(map)
                        .addOnSuccessListener(unused -> {

                            ArrayList<String> oldList = saveChecks(education, null, "Get checks");
                            if (oldList == null) {
                                oldList = new ArrayList<>();
                            }
                            oldList.add(field);
                            String json = gson.toJson(oldList);
                            editor.putString(education, json);
                            editor.apply();
                            Toast.makeText(c, "Changes applied", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> Toast.makeText(c, e.getMessage(), Toast.LENGTH_SHORT).show());

                if (user != null) {
                    Map<String, Object> map1 = new HashMap<>();
                    map1.put("enrollment", FieldValue.increment(1));
                    map1.put("userIDs", FieldValue.arrayUnion(user.getUid()));

                    userCount1.collection("Users list")
                            .whereLessThan("enrollment", 50000).limit(1)
                            .get().addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                String docId = qds.getId();
                                userCount1.collection("Users list").document(docId)
                                        .update(map1)
                                        .addOnSuccessListener(unused1 -> {


                                        })
                                        .addOnFailureListener(e -> {

                                        });
                            }
                        } else {
                            userCount1.collection("Users list")
                                    .document().set(map1).addOnSuccessListener(unused -> {
                                userCount1.update(map).addOnSuccessListener(unused3 -> {

                                    ArrayList<String> oldList = saveChecks(education, null, "Get checks");
                                    if (oldList == null) {
                                        oldList = new ArrayList<>();
                                    }
                                    oldList.add(field);
                                    String json = gson.toJson(oldList);
                                    editor.putString(education, json);
                                    editor.apply();
                                    Toast.makeText(c, "Changes applied to a new document", Toast.LENGTH_SHORT).show();
                                }).addOnFailureListener(e -> Toast.makeText(c, e.getMessage(), Toast.LENGTH_SHORT).show());

                            })
                                    .addOnFailureListener(e -> {

                                    });
                        }

                    })
                            .addOnFailureListener(e -> {

                            });

                } else {
                    LogInSignUp logInSignUp = new LogInSignUp(c);
                    logInSignUp.show();
                }

                return null;
            }
            case "Unchecked": {
                Map<String, Object> map = new HashMap<>();
                map.put(field, FieldValue.increment(-1));
                userCount1.update(map).addOnSuccessListener(unused -> {
                    ArrayList<String> oldList = saveChecks(education, null, "Get checks");
                    if (oldList == null) {
                        oldList = new ArrayList<>();
                    } else {
                        oldList.remove(field);
                    }
                    String json = gson.toJson(oldList);
                    editor.putString(education, json);
                    editor.apply();
                    Toast.makeText(c, "Changes applied", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> Toast.makeText(c, e.getMessage(), Toast.LENGTH_SHORT).show());

                return null;
            }
            case "Get checks": {
                String json = preferences.getString(education, "");

                Type typeToken = new TypeToken<ArrayList<String>>() {
                }.getType();
                try {
                    sch = gson.fromJson(json, typeToken);

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    sch = new ArrayList<>();
                }
                break;
            }
        }
        return sch;
    }
}
