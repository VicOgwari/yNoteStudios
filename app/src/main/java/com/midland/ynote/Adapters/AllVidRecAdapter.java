package com.midland.ynote.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.midland.ynote.Activities.VideoPlayer;
import com.midland.ynote.Activities.VideoUploader;
import com.midland.ynote.Objects.SourceDocObject;
import com.midland.ynote.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AllVidRecAdapter extends RecyclerView.Adapter<AllVidRecAdapter.AllVidViewHolder> {
    private final Context c;
    private final ArrayList<SourceDocObject> sourceDocObjects;
    private final String flag, school;

    public AllVidRecAdapter(Context c, ArrayList<SourceDocObject> sourceDocObjects, String flag, String school) {
        this.c = c;
        this.sourceDocObjects = sourceDocObjects;
        this.flag = flag;
        this.school = school;
    }

    @NonNull
    @Override
    public AllVidViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.all_vid_object, parent, false);
        return new AllVidViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final AllVidViewHolder holder, int position) {
        final SourceDocObject sourceDocObject = sourceDocObjects.get(holder.getAbsoluteAdapterPosition());
        Glide.with(c).load(sourceDocObject.getDocUri())
                .placeholder(R.drawable.ic_slow_motion_video).thumbnail((float) 0.9).into(holder.vidPreview);

        holder.vidName.setText(sourceDocObject.getName());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        holder.vidDate.setText(sourceDocObject.getFileDate());
        holder.vidSize.setText((Integer.parseInt(sourceDocObject.getFileSize())) / 1024 + " megabytes");
        holder.doc_options.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(c, holder.doc_options);
            popupMenu.getMenuInflater().inflate(R.menu.lecture_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()){
                    case R.id.rename:
                        holder.renameRel.setVisibility(View.VISIBLE);
                        holder.vidName.setVisibility(View.GONE);
                        break;

                    case R.id.upload:
                        Intent intent = new Intent(c, VideoUploader.class);
                        intent.putExtra("selectedVideo", sourceDocObject.getDocUri().toString());
                        intent.putExtra("fileName", sourceDocObject.getName());
                        c.startActivity(intent);
                        break;

                    case R.id.delete:
                        AlertDialog areYouSure = new AlertDialog.Builder(c)
                                .setTitle("Delete lecture?")
                                .setMessage("Do you wish to delete " + holder.vidName.getText().toString() + "?")
                                .setPositiveButton("Yup!", (dialog, which) -> {
                                    File rejectLec = new File(sourceDocObject.getDocUri().getPath());
                                    if (rejectLec.exists()){
                                        if (rejectLec.delete()){
                                            this.notifyItemRemoved(holder.getAbsoluteAdapterPosition());
                                        }else {
                                            Toast.makeText(c, "Try again..", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .setNegativeButton("Nope!", (dialog, which) -> {
                                    dialog.dismiss();
                                })
                                .create();

                        areYouSure.show();

                        break;
                }
                return false;
            });
            popupMenu.show();
        });

        holder.renameTV.setOnClickListener(view -> {
            String rename = holder.renameET.getText().toString().trim();
            if (rename.equals("")) {
                Toast.makeText(c, "No changes..", Toast.LENGTH_SHORT).show();
            } else {
                String prevName = holder.renameTV.getText().toString().trim();
                File renameFile = new File(sourceDocObject.getDocUri().getPath());
                        if (renameFile.exists()){
                            if (renameFile.renameTo(new File(prevName))){
                                Toast.makeText(c, "Changes saved..", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(c, "Something's not right..", Toast.LENGTH_SHORT).show();
                            }
                        }
                holder.vidName.setText(rename);

            }
            holder.renameRel.setVisibility(View.GONE);
            holder.vidName.setVisibility(View.VISIBLE);
        });
        holder.itemView.setOnClickListener(v -> {
            holder.vidPreview.invalidate();
//                Drawable s = holder.vidPreview.getDrawable();
            BitmapDrawable drawable = (BitmapDrawable) holder.vidPreview.getDrawable();
            Bitmap thumbnail = drawable.getBitmap();

            if (flag.equals("offline")){
                Intent intent = new Intent(c.getApplicationContext(), VideoPlayer.class);
                intent.putExtra("vidUri", sourceDocObject.getDocUri().toString());
                intent.putExtra("vidTitle", sourceDocObject.getName());
                intent.putExtra("resource", "memory");
                c.startActivity(intent);
            }else if (flag.equals("upload")){
                if (thumbnail == null){
                    Toast.makeText(c, "Still processing thumbnail...", Toast.LENGTH_SHORT).show();
                }else {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] bytes = stream.toByteArray();
                    Intent intent = new Intent(c.getApplicationContext(), VideoUploader.class);
                    intent.putExtra("selectedVideo", sourceDocObject.getDocUri().toString());
                    intent.putExtra("thumbnail", bytes);
                    intent.putExtra("fileName", sourceDocObject.getName());
                    intent.putExtra("school", school);
                    c.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return sourceDocObjects.size();
    }


    public static class AllVidViewHolder extends RecyclerView.ViewHolder{
        ImageView vidPreview;
        TextView vidName, vidDate, vidSize, renameTV;
        Button doc_options;
        RelativeLayout renameRel;
        EditText renameET;
        public AllVidViewHolder(@NonNull View itemView) {
            super(itemView);
            vidPreview = itemView.findViewById(R.id.vidPreview);
            vidName = itemView.findViewById(R.id.vidName);
            vidDate = itemView.findViewById(R.id.vidDate);
            vidSize = itemView.findViewById(R.id.vidSize);
            renameTV = itemView.findViewById(R.id.renameTV);
            renameET = itemView.findViewById(R.id.renameET);
            renameRel = itemView.findViewById(R.id.renameRel);
            doc_options = itemView.findViewById(R.id.doc_options);
        }
    }


}
