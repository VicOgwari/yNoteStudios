package com.midland.ynote.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.midland.ynote.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ProjectorAdt extends RecyclerView.Adapter<ProjectorAdt.ProjectorVH> {

    private Context context;
    private ArrayList<Uri> photos;

    public ProjectorAdt(Context context, ArrayList<Uri> photos) {
        this.context = context;
        this.photos = photos;
    }

    @NonNull
    @NotNull
    @Override
    public ProjectorAdt.ProjectorVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ProjectorVH(LayoutInflater.from(context)
                .inflate(R.layout.projector_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProjectorAdt.ProjectorVH holder, int position) {
        Glide.with(context).load(photos.get(holder.getAbsoluteAdapterPosition()))
                .centerCrop()
                .thumbnail((float) 0.9)
                .into(holder.projectorItem);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public static class ProjectorVH extends RecyclerView.ViewHolder{
        ImageView projectorItem;
        public ProjectorVH(@NonNull @NotNull View itemView) {
            super(itemView);
            projectorItem = itemView.findViewById(R.id.imageItem);
        }
    }
}
