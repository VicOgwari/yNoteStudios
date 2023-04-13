package com.midland.ynote.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.midland.ynote.Activities.PhotoDoc;
import com.midland.ynote.R;

import java.util.ArrayList;

public class PhotoDocsAdapter extends RecyclerView.Adapter<PhotoDocsAdapter.PhotoDocsVH> {

    private final Context con;
    private final ArrayList<Uri> photoUri;
    private final ArrayList<String> previewDesc;
    private final String fileName;


    public PhotoDocsAdapter(Context con, ArrayList<Uri> photoUri, ArrayList<String> previewDesc, String fileName) {
        this.con = con;
        this.photoUri = photoUri;
        this.previewDesc = previewDesc;
        this.fileName = fileName;
    }

    @NonNull
    @Override
    public PhotoDocsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(con).inflate(R.layout.photo_doc_item, parent, false);
        return new PhotoDocsVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final PhotoDocsVH holder, final int position) {
//        holder.previewDescription.setText(previewDesc.get(position));
        Glide.with(con).load(photoUri.get(position).toString()).fitCenter().into(holder.photoDocItem);

        holder.saveEdits.setOnClickListener(v -> {
            PhotoDoc.Companion.getPhotoDesc().set(position, holder.editPreviewDesc.getText().toString());
            PhotoDocsAdapter.this.notifyDataSetChanged();
            holder.editPreviewDesc.setVisibility(View.GONE);
            holder.previewDescription.setText(holder.editPreviewDesc.getText().toString());
            holder.previewDescription.setVisibility(View.VISIBLE);
            Toast.makeText(con, previewDesc.get(position), Toast.LENGTH_SHORT).show();


//                holder.previewDescription.setText(descriptions.get(position));
            PhotoDocsAdapter.this.notifyDataSetChanged();

        });


    }

    @Override
    public int getItemCount() {
        return photoUri.size();
    }

    public static class PhotoDocsVH extends RecyclerView.ViewHolder {
        ImageView photoDocItem;
        TextView previewDescription;
        EditText editPreviewDesc;
        Button saveEdits;

        public PhotoDocsVH(@NonNull View itemView) {
            super(itemView);
            photoDocItem = itemView.findViewById(R.id.preview);
            previewDescription = itemView.findViewById(R.id.prevDescription);
//            date = itemView.findViewById(R.id.prevDate);
//            size = itemView.findViewById(R.id.prevSize);
            editPreviewDesc = itemView.findViewById(R.id.editPreviewDescription);
            saveEdits = itemView.findViewById(R.id.submitEdit);
        }
    }




}
