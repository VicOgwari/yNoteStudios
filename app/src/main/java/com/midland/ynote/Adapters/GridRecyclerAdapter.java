package com.midland.ynote.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.midland.ynote.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.midland.ynote.Activities.LectureStudio;
import com.midland.ynote.Activities.LectureStudio2;
import com.midland.ynote.Activities.LecturesList;
import com.midland.ynote.Activities.PhotoDoc;
import com.midland.ynote.Activities.SchoolDepartmentDocuments;
import com.midland.ynote.Activities.SearchResults;
import com.midland.ynote.Activities.SourceDocList;
import com.midland.ynote.Activities.UserProfile2;
import com.midland.ynote.Dialogs.VidType;
import com.midland.ynote.Objects.ImageObject;
import com.midland.ynote.Objects.PendingLecObj;
import com.midland.ynote.Objects.Schools;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GridRecyclerAdapter extends RecyclerView.Adapter<GridRecyclerAdapter.GridRecAdVH> {

    PendingLecObj pendingLecObj;
    Context c;
    ArrayList<ImageObject> imageObjects;
    ArrayList<PendingLecObj> pendingLecObs;
    ArrayList<Schools> lectureFilterObj;
    String flag;
    String fileName;

    public GridRecyclerAdapter(Context c, ArrayList<ImageObject> imageObjects,
                               ArrayList<PendingLecObj> pendingLecObs,
                               ArrayList<Schools> lectureFilterObj,
                               String flag, String fileName) {
        this.c = c;
        this.imageObjects = imageObjects;
        this.pendingLecObs = pendingLecObs;
        this.lectureFilterObj = lectureFilterObj;
        this.flag = flag;
        this.fileName = fileName;
    }

    @NonNull
    @Override
    public GridRecAdVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (flag) {
            case "ProfileItemsList":
                return new GridRecAdVH(LayoutInflater.from(c).inflate(R.layout.pending_lec_object, parent, false));
            case "LectureFilter":
                return new GridRecAdVH(LayoutInflater.from(c).inflate(R.layout.lecture_filter_grid_item, parent, false));
            case "UserProfile1":
                return new GridRecAdVH(LayoutInflater.from(c).inflate(R.layout.published_sch, parent, false));
            case "UserProfile2":
                return new GridRecAdVH(LayoutInflater.from(c).inflate(R.layout.published_sch1, parent, false));
            case "moreIdeas":
            default:
                return new GridRecAdVH(LayoutInflater.from(c).inflate(R.layout.gallery_object, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull GridRecAdVH holder, final int position) {
        switch (flag) {
            case "ProfileItemsList":
                pendingLecObj = pendingLecObs.get(position);

                if (Uri.parse(pendingLecObj.getIllustrationLocale()) != null) {
                    Glide.with(c).load(Uri.parse(pendingLecObj.getIllustrationLocale())).thumbnail((float) 0.9)
                            .placeholder(R.drawable.ic_launcher_background).into(holder.pendingLecImage);
                } else {
                    Glide.with(c).load(R.drawable.ic_launcher_background).thumbnail((float) 0.9).into(holder.pendingLecImage);
                }

                holder.pendingLecTitle.setText(pendingLecObj.getPendingLecTitle());

                holder.itemView.setOnClickListener(v -> {
                    Intent intent;
                    intent = new Intent(c.getApplicationContext(), LectureStudio2.class);
                    intent.putExtra("savedLecture", pendingLecObj);
                    c.startActivity(intent);
                });

                break;
            case "LectureFilter":
                Glide.with(c).load(lectureFilterObj.get(position).getSchoolEmblem()).thumbnail((float) 0.9)
                        .placeholder(R.drawable.ic_launcher_background).into(holder.lecFilterImage);
                holder.lecFilterTitle.setText(lectureFilterObj.get(position).getSchoolName());

                holder.schoolOptions.setOnClickListener(view -> {
                    PopupMenu popupMenu = new PopupMenu(c, holder.schoolOptions);
                    popupMenu.getMenuInflater().inflate(R.menu.school_options_menu1, popupMenu.getMenu());
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(item -> {
                        switch (item.getItemId()) {
                            case R.id.documents:
                                Intent intent = new Intent(c, SchoolDepartmentDocuments.class);
                                intent.putExtra("SchoolName", holder.lecFilterTitle.getText().toString());
                                c.startActivity(intent);
                                break;
                        }
                        return false;
                    });
                });
                holder.filterCard.setOnClickListener(v -> {
                    String sch = holder.lecFilterTitle.getText().toString();
                    Intent intent;
                    if (sch.equals("Confucius Institute") || sch.equals("Peace & Security Studies")){
                        intent = new Intent(c, SearchResults.class);
                        intent.putExtra("addDocBtn", "set");
                        intent.putExtra("school", sch);
                        intent.putExtra("flag", "Lecture search");
                    }else {
                        intent = new Intent(c, LecturesList.class);
                        intent.putExtra("child", lectureFilterObj.get(position).getSchoolName());
                    }
                    c.startActivity(intent);
                });

                final DocumentReference vidCount = FirebaseFirestore.getInstance().collection("Count")
                        .document("Lectures count");

                final DocumentReference userCount = FirebaseFirestore.getInstance().collection("Count")
                        .document("Enrollment count");

                ArrayList<String> vidCountArray = new ArrayList<>();
                ArrayList<String> userCountArray = new ArrayList<>();

                vidCount.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        for (Schools sch : lectureFilterObj) {
                            if (!String.valueOf(documentSnapshot.get(sch.getSchoolName())).equals("") && documentSnapshot.get(sch.getSchoolName()) != null) {
                                vidCountArray.add(String.valueOf(documentSnapshot.get(sch.getSchoolName())));
                            } else {
                                vidCountArray.add(String.valueOf(0));
                            }
                        }
                        try {
                            holder.vidCount.setText(vidCountArray.get(holder.getAbsoluteAdapterPosition()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        holder.docCountProg.setVisibility(View.GONE);
                        holder.vidCount.setVisibility(View.VISIBLE);
                    }

                });

                userCount.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        for (Schools sch : lectureFilterObj) {
                            if (!String.valueOf(documentSnapshot.get(sch.getSchoolName())).equals("") && documentSnapshot.get(sch.getSchoolName()) != null) {
                                userCountArray.add(String.valueOf(documentSnapshot.get(sch.getSchoolName())));
                            } else {
                                userCountArray.add(String.valueOf(0));
                            }
                        }
                        try {
                            holder.userCount.setText(userCountArray.get(holder.getAbsoluteAdapterPosition()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        holder.userCountProg.setVisibility(View.GONE);
                        holder.userCount.setVisibility(View.VISIBLE);
                    }

                });

                break;
            case "UserProfile1":
                Glide.with(c).load(lectureFilterObj.get(holder.getAbsoluteAdapterPosition()).getSchoolEmblem())
                        .centerInside().into(holder.schEmblem);
                holder.schTitle.setText(lectureFilterObj.get(holder.getAbsoluteAdapterPosition()).getSchoolName());
                holder.itemView.setOnClickListener(v -> {
                    if (holder.schTitle.getText().equals("Nothing yet!")) {
                        Intent intent = new Intent(c, SourceDocList.class);
                        intent.putExtra("userProfile", "UserProfile");
                        intent.putExtra("school", holder.schTitle.getText().toString());
                        c.startActivity(intent);
                    } else {
                        Intent intent = new Intent(c, SearchResults.class);
                        intent.putExtra("userProfile", "UserProfile1");
                        intent.putExtra("school", holder.schTitle.getText().toString());
                        c.startActivity(intent);
                    }
                });
            case "UserProfile2":
                Glide.with(c).load(lectureFilterObj.get(holder.getAbsoluteAdapterPosition()).getSchoolEmblem())
                        .centerInside().into(holder.schEmblem);
//                holder.schTitle.setText(lectureFilterObj.get(holder.getAbsoluteAdapterPosition()).getSchoolName());
                holder.itemView.setOnClickListener(v -> {
                    if (holder.schTitle.getText().equals("Nothing yet!")) {
                        VidType vidType = new VidType(c, null, null, null);
                        vidType.show();
                    } else {
                        Intent intent = new Intent(c, SearchResults.class);
                        intent.putExtra("userProfile", "UserProfile2");
                        c.startActivity(intent);
                    }
                });
                break;

            case "moreIdeas":
            default:
                final ImageObject imageObject = imageObjects.get(position);
                Glide.with(c).load(imageObject.getUri()).thumbnail((float) 0.9).into(holder.imageView);
                holder.itemView.setOnClickListener(v -> {

                    switch (flag) {
                        case "ProfileItemsList":
                            break;

                        case "NotesPopUp":
                            Intent intent1 = new Intent(c.getApplicationContext(), LectureStudio.class);
                            intent1.putExtra("SelectedPic", imageObject.getUri().toString());
                            c.startActivity(intent1);
                            break;

                        case "biggerPicture":
                            Intent intent2 = new Intent(c.getApplicationContext(), UserProfile2.class);
                            intent2.putExtra("SelectedPic", imageObject.getUri().toString());
                            c.startActivity(intent2);
                            break;

                    }


                    if (flag.equals("PhotoDocs")) {
                        Intent intent2 = new Intent(c.getApplicationContext(), PhotoDoc.class);
                        intent2.putExtra("SelectedPic", imageObject.getUri().toString());
                        intent2.putExtra("prevDesc", "desc");
                        intent2.putExtra("bitmaps", "gallery");
                        intent2.putExtra("fileName", fileName);
                        c.startActivity(intent2);
                    }

                    if (flag.equals("moreIdeas")) {
                        ArrayList<String> images = new ArrayList<>();
                        images.add(imageObject.getUri().toString());
                    }
                });

                break;
        }
    }

    @Override
    public int getItemCount() {
        switch (flag) {
            case "ProfileItemsList":
                return pendingLecObs.size();
            case "LectureFilter":
            case "UserProfile1":
            case "UserProfile2":
                return lectureFilterObj.size();
            case "moreIdeas":
            default:
                return imageObjects.size();
        }
    }

    public class GridRecAdVH extends RecyclerView.ViewHolder {
        ImageView pendingLecImage;
        TextView pendingLecTitle;
        ImageView lecFilterImage;
        TextView lecFilterTitle, schTitle, vidCount, userCount;
        Button filterCard, schoolOptions;
        CircleImageView schEmblem;
        ProgressBar docCountProg, userCountProg;
        ImageView imageView;

        public GridRecAdVH(@NonNull View itemView) {
            super(itemView);
            switch (flag) {
                case "ProfileItemsList":
                    pendingLecImage = itemView.findViewById(R.id.pendingLecImage);
                    pendingLecTitle = itemView.findViewById(R.id.pendingLecTitle);
                    break;
                case "LectureFilter":
                    lecFilterImage = itemView.findViewById(R.id.schoolEmblem);
                    lecFilterTitle = itemView.findViewById(R.id.schoolName);
                    filterCard = itemView.findViewById(R.id.filterCard);
                    vidCount = itemView.findViewById(R.id.docCount);
                    schoolOptions = itemView.findViewById(R.id.schoolOptions);
                    userCount = itemView.findViewById(R.id.userCount);
                    docCountProg = itemView.findViewById(R.id.docCountProg);
                    userCountProg = itemView.findViewById(R.id.userCountProg);
                    break;
                case "UserProfile1":
                case "UserProfile2":
                    schTitle = itemView.findViewById(R.id.schTitle);
                    schEmblem = itemView.findViewById(R.id.schEmblem);
                    break;
                case "moreIdeas":
                default:
                    imageView = itemView.findViewById(R.id.photo);
                    break;
            }

        }
    }


}
