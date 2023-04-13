package com.midland.ynote.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.midland.ynote.Objects.SelectedDoc;
import com.midland.ynote.R;
import com.midland.ynote.Utilities.NotesPopUp;

import java.util.ArrayList;

public class PointsAdapter extends RecyclerView.Adapter<PointsAdapter.PointsVH> {

    private Context c;
    private ArrayList<String> points;
    private String school, uid, flag;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private NotesPopUp notesPopUp;

    public PointsAdapter(Context c, ArrayList<String> aPoint, ProgressBar progressBar, RecyclerView recyclerView, String uid, String flag, NotesPopUp notesPopUp) {
        this.c = c;
        this.points = aPoint;
        this.progressBar = progressBar;
        this.recyclerView = recyclerView;
        this.uid = uid;
        this.flag = flag;
        this.notesPopUp = notesPopUp;
    }

    @NonNull
    @Override
    public PointsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (flag.equals("navigationDrawer")){
            v = LayoutInflater.from(c).inflate(R.layout.nav_education_item, parent, false);
        }else {
            v = LayoutInflater.from(c).inflate(R.layout.point_item, parent, false);
        }
        return new PointsVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final  PointsVH holder, int position) {
        school = points.get(position);
        holder.itemView.setOnClickListener(v -> {
            switch (flag) {
                case "Documents": {
                    progressBar.setVisibility(View.VISIBLE);
                    final ArrayList<SelectedDoc> selectedDocs = new ArrayList<>();

//                    final CloudVideosAdapter cloudVideosAdapter = new CloudVideosAdapter(rateLecture, lecRatingBar, addComment, docCommentET,
//                            docCommentRV, bottomSheet, bottomSheetBehavior, playerView, closeViewer, docComments,
//                            viewerRel, app, c, c, selectedVideos, getApplication(), getParent());
//
//                    cloudVideosAdapter.notifyDataSetChanged();

                    FirebaseFirestore.getInstance().collection("Content")
                            .document("Documents").collection(school)
                            .whereEqualTo("uid", uid).get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                    SelectedDoc selectedDoc = qds.toObject(SelectedDoc.class);
                                    selectedDocs.add(selectedDoc);
                                }
                                final DocumentAdapter documentAdapter = new DocumentAdapter(null, c, c, selectedDocs);
                                documentAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.INVISIBLE);
                            });
                    break;
                }
                case "Lectures": {
                    progressBar.setVisibility(View.VISIBLE);
                    final ArrayList<SelectedDoc> selectedDocs = new ArrayList<>();
                    final DocumentAdapter documentAdapter = new DocumentAdapter(
                            null, c, c, selectedDocs);
                    documentAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(documentAdapter);

                    FirebaseFirestore.getInstance().collection("Content")
                            .document("Lectures").collection(school)
                            .whereEqualTo("uid", uid).get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
                                    SelectedDoc selectedDoc = qds.toObject(SelectedDoc.class);
                                    selectedDocs.add(selectedDoc);
                                }
                                documentAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.INVISIBLE);
                            });
                    break;
                }
            }
        });
        holder.pointItem.setText(points.get(position));

        holder.pointCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                PopupMenu popupMenu = new PopupMenu(c, holder.itemView);
                popupMenu.getMenuInflater().inflate(R.menu.points_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.delete) {
                        if (notesPopUp != null){
                            notesPopUp.initFileDetailCapture(points);
                        }
                        points.remove(holder.getAbsoluteAdapterPosition());
                        notifyItemRemoved(holder.getAbsoluteAdapterPosition());
                        notifyItemRangeChanged(holder.getAbsoluteAdapterPosition(), points.size());
                    }
                    popupMenu.show();
                    return false;
                });
                popupMenu.show();
            }else {
                Toast.makeText(c, "Find more points.", Toast.LENGTH_SHORT).show();
            }
           
        });
    }

    @Override
    public int getItemCount() {
        return points.size();
    }

    public static class PointsVH extends RecyclerView.ViewHolder {
        TextView pointItem;
        CheckBox pointCheck;
        public PointsVH(@NonNull View itemView) {
            super(itemView);
            pointItem = itemView.findViewById(R.id.pointItem);
            pointCheck = itemView.findViewById(R.id.pointCheck);
        }
    }
}
