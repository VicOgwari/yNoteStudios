package com.midland.ynote.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.midland.ynote.R;

import java.util.ArrayList;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.TagsVH> {

    private Context c;
    private ArrayList<String> tags;

    public TagsAdapter(Context c, ArrayList<String> tags) {
        this.c = c;
        this.tags = tags;
    }

    @NonNull
    @Override
    public TagsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.tags_object, parent, false);
        return new TagsVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TagsVH holder, final int position) {
        holder.vidTag.setText(tags.get(position));
        holder.removeTag.setOnClickListener(v -> {
            tags.remove(tags.get(position));
            TagsAdapter.this.notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public static class TagsVH extends RecyclerView.ViewHolder{
        TextView vidTag;
        ImageButton removeTag;
        public TagsVH(@NonNull View itemView) {
            super(itemView);
            removeTag = itemView.findViewById(R.id.removeTag);
            vidTag = itemView.findViewById(R.id.vidTag);
        }
    }
}
