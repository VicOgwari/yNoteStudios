package com.midland.ynote.Utilities.Paint;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.midland.ynote.Interfaces.ViewOnClick;
import com.midland.ynote.R;

import java.io.File;
import java.util.List;

public class PaintFileAdapter extends RecyclerView.Adapter<PaintFileAdapter.FileViewHolder> {

    private Context mContext;
    private List<File> fileList;

    public PaintFileAdapter(Context mContext, List<File> fileList) {
        this.mContext = mContext;
        this.fileList = fileList;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.paint_file_row, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        holder.imageView.setImageURI(Uri.fromFile(fileList.get(position)));
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public static class FileViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public ViewOnClick viewOnClick;

        public void setViewOnClick(ViewOnClick viewOnClick) {
            this.viewOnClick = viewOnClick;
        }

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.fileImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewOnClick.onClick(getAdapterPosition());
                }
            });
        }
    }
}
