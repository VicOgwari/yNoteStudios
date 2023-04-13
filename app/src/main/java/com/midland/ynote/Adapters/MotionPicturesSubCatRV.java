package com.midland.ynote.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.midland.ynote.R;
import com.midland.ynote.Utilities.FilingSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MotionPicturesSubCatRV extends RecyclerView.Adapter<MotionPicturesSubCatRV.SubCatVH> {

    private Context con;
    private ArrayList<String> subCategories;
    public ArrayList<String> compSciTags, mathTags, statsTags;
    private String flag;
    public static Map<String, String[]> lectureSubFieldTags = new HashMap<>();
    public MotionPicturesSubCatRV adapter1;
    final int[] selectedItemPos = {-1};
    final int[] lastSelectedItemPos = { -1 };


    public MotionPicturesSubCatRV(Context con, ArrayList<String> subCategories, String flag) {
        this.con = con;
        this.subCategories = subCategories;
        this.flag = flag;
    }


    @NonNull
    @Override
    public SubCatVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(con).inflate(R.layout.sub_category, parent, false);

        return new SubCatVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final SubCatVH holder, final int position) {

        Dialog tagsDialog = new Dialog(con, R.style.tag_preview_theme);
        TextView tagText = tagsDialog.findViewById(R.id.tagProjection);
        holder.subCatCardView.setCardBackgroundColor(Color.WHITE);

        holder.itemView.setOnLongClickListener(v -> {
            Toast.makeText(con, subCategories.toString(), Toast.LENGTH_SHORT).show();
            return false;
        });

        if (position == selectedItemPos[0]){
            holder.subCatCardView.setCardBackgroundColor(Color.BLUE);
        }else {
            holder.subCatCardView.setCardBackgroundColor(Color.WHITE);
        }

        holder.subCategory.setText(subCategories.get(position));
        holder.subCatCardView.setOnClickListener(v -> {
            if (flag.equals("motionPictures")) {

                Toast.makeText(con, "Filter " + holder.subCategory.getText().toString(), Toast.LENGTH_SHORT).show();
            } else if (flag.equals("lectureTags")) {


                    if (!(FilingSystem.Companion.getAllTags().contains(holder.subCategory.getText().toString()))){
                        if (FilingSystem.Companion.getAllTags().size() < 4){
                            FilingSystem.Companion.getAllTags().add(holder.subCategory.getText().toString());
                            Toast.makeText(con, FilingSystem.Companion.getAllTags().toString(), Toast.LENGTH_SHORT).show();
                            holder.subCatCardView.setCardBackgroundColor(Color.GREEN);
                        }else {
                            Toast.makeText(con, "Don't misuse tags.", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        FilingSystem.Companion.getAllTags().remove(holder.subCategory.getText().toString());
                        holder.subCatCardView.setCardBackgroundColor(Color.WHITE);
                        Toast.makeText(con, FilingSystem.Companion.getAllTags().toString(), Toast.LENGTH_SHORT).show();

                    }
                }


        });


        if (FilingSystem.Companion.getAllTags().contains(holder.subCategory.getText().toString())){
            holder.subCatCardView.setCardBackgroundColor(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return subCategories.size();
    }

    public class SubCatVH extends RecyclerView.ViewHolder {
        TextView subCategory;
        CardView subCatCardView;

        public SubCatVH(@NonNull View itemView) {
            super(itemView);

            subCatCardView = itemView.findViewById(R.id.subCatCardView);
            subCategory = itemView.findViewById(R.id.subCategory);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedItemPos[0] = getAdapterPosition();


                    if (lastSelectedItemPos[0] != -1) {
                        notifyItemChanged(lastSelectedItemPos[0]);
                    }
                    lastSelectedItemPos[0] = selectedItemPos[0];
                    notifyItemChanged(selectedItemPos[0]);
                }
            });
        }
    }
}
