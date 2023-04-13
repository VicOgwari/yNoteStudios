package com.midland.ynote.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.midland.ynote.R;

import java.util.ArrayList;

public class SelectedItemsAdt extends RecyclerView.Adapter<SelectedItemsAdt.SelectedItemVH> {

    private Context c;
    private final ArrayList<String> selects;
    private String flag;

    public SelectedItemsAdt(Context c, ArrayList<String> selects, String flag) {
        this.c = c;
        this.selects = selects;
        this.flag = flag;
    }

    @NonNull
    @Override
    public SelectedItemsAdt.SelectedItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        if (flag.equals("Docs")){
            v = LayoutInflater.from(c).inflate(R.layout.doc_object, parent, false);
        }else if (flag.equals("Images")){
            v = LayoutInflater.from(c).inflate(R.layout.image_object, parent, false);
        }

        return new SelectedItemVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedItemsAdt.SelectedItemVH holder, int position) {
        if (flag.equals("Docs")){
            holder.docName.setText(selects.get(holder.getAbsoluteAdapterPosition()));
            holder.sizeCard.setVisibility(View.GONE);
            if (selects.get(holder.getAbsoluteAdapterPosition()).endsWith(".pdf") || selects.get(holder.getAbsoluteAdapterPosition()).endsWith(".PDF")) {
                Glide.with(c).load(R.drawable.pdf).thumbnail(0.8f).into(holder.docEmblem);
            }
            if (selects.get(holder.getAbsoluteAdapterPosition()).endsWith(".doc") || selects.get(holder.getAbsoluteAdapterPosition()).endsWith(".DOC")) {
                Glide.with(c).load(R.drawable.microsoft_word).thumbnail(0.8f).into(holder.docEmblem);
            }
            if (selects.get(holder.getAbsoluteAdapterPosition()).endsWith(".ppt") || selects.get(holder.getAbsoluteAdapterPosition()).endsWith(".PPT")) {
                Glide.with(c).load(R.drawable.powerpoint).thumbnail(0.8f).into(holder.docEmblem);
            }
            if (selects.get(holder.getAbsoluteAdapterPosition()).endsWith(".docx") || selects.get(holder.getAbsoluteAdapterPosition()).endsWith(".DOCX")) {
                Glide.with(c).load(R.drawable.microsoft_word).thumbnail(0.8f).into(holder.docEmblem);
            }
            if (selects.get(holder.getAbsoluteAdapterPosition()).endsWith(".pptx") || selects.get(holder.getAbsoluteAdapterPosition()).endsWith(".PPTX")) {
                Glide.with(c).load(R.drawable.powerpoint).thumbnail(0.8f).into(holder.docEmblem);
            }
        }else if (flag.equals("Images")){
//            Picasso.get().load(selects.get(holder.getAbsoluteAdapterPosition()))
//                    .into(holder.imageComment);
            Glide.with(c).load(selects.get(holder.getAbsoluteAdapterPosition())).thumbnail((float) 0.9)
                    .into(holder.imageComment);

            holder.closeDocView.bringToFront();
            holder.closeDocView.setOnClickListener(v -> {
                selects.remove(holder.getAbsoluteAdapterPosition());
                SelectedItemsAdt.this.notifyItemRemoved(holder.getAbsoluteAdapterPosition());
            });
        }
    }

    @Override
    public int getItemCount() {
        return selects.size();
    }

    public static class SelectedItemVH extends RecyclerView.ViewHolder {
        ImageView imageComment, docEmblem;
        TextView docName;
        CardView sizeCard;
        ImageButton closeDocView;

        public SelectedItemVH(@NonNull View itemView) {
            super(itemView);
            imageComment = itemView.findViewById(R.id.imageItem);
            closeDocView = itemView.findViewById(R.id.closeDocView);
            docEmblem = itemView.findViewById(R.id.picBitmap);
            docName = itemView.findViewById(R.id.bitmapDescription);
            sizeCard = itemView.findViewById(R.id.sizeCard);
        }
    }
}
