package com.midland.ynote.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.midland.ynote.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.midland.ynote.Activities.LectureStudio2;
import com.midland.ynote.Activities.PhotoDoc;
import com.midland.ynote.Dialogs.EnrollmentDialog;
import com.midland.ynote.Dialogs.LogInSignUp;
import com.midland.ynote.Objects.BitMapTitle;
import com.midland.ynote.Objects.PictorialObject;
import com.midland.ynote.Utilities.Publisher;

import java.util.ArrayList;

public class PicsAdapter extends RecyclerView.Adapter<PicsAdapter.PicsVH> {

    private Context context;
    private ArrayList<BitMapTitle> pictorials;
    private String flag;
    private String sourceDocUri, lectureName;


    public PicsAdapter(Context context, ArrayList<BitMapTitle> pictorials, String flag, String sourceDocUri, String lectureName) {
        this.context = context;
        this.pictorials = pictorials;
        this.flag = flag;
        this.sourceDocUri = sourceDocUri;
        this.lectureName = lectureName;
    }

    @NonNull
    @Override
    public PicsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PicsVH(LayoutInflater.from(context).inflate(R.layout.pictorial_obj, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final PicsVH holder, final int position) {
        final BitMapTitle bitMapTitle = pictorials.get(holder.getAbsoluteAdapterPosition());

        if (flag.equals("cloudBitmaps")) {
            holder.ratingsCard.setVisibility(View.VISIBLE);
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, PhotoDoc.class);
                intent.putExtra("docName", holder.bitmapTitle.getText().toString());
                intent.putExtra("bitmaps", "cloudMaps");
                context.startActivity(intent);
            });
        } else {
            holder.ratingsCard.setVisibility(View.GONE);
            if (flag.equals("activity")) {
                holder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(context, LectureStudio2.class);
                    intent.putExtra("bitmapTitle", holder.bitmapTitle.getText().toString());
                    intent.putExtra("fileName", lectureName);
                    intent.putExtra("bitmaps", "bitmaps");
                    if (sourceDocUri.equals("_Nothing was selected so studio 1_")) {
                        intent.putExtra("studio", "1");
                    } else if (sourceDocUri.equals("_Nothing was selected so studio 4_")) {
                        intent.putExtra("studio", "4");
                    } else {
                        intent.putExtra("selectedDoc", sourceDocUri);
                        intent.putExtra("studio", "5");
                    }
                    context.startActivity(intent);
                });
            } else if (flag.equals("fragment")) {
                holder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(context, PhotoDoc.class);
                    intent.putExtra("docName", holder.bitmapTitle.getText().toString());
                    intent.putExtra("bitmaps", "bitmaps");
                    context.startActivity(intent);
                });
            }

        }

        ArrayList<String> firstImages = new ArrayList<>();
        String firsImage;
        for (BitMapTitle t : pictorials) {
            if (PhotoDoc.Companion.loadPictorials(context, t.getTitle()).size() > 0) {
                firsImage = PhotoDoc.Companion.loadPictorials(context, t.getTitle())
                        .get(0).getPicture();
            } else {
                firsImage = null;
            }
            firstImages.add(firsImage);
        }


        if (firstImages.get(position) != null) {
            Glide.with(context).load(firstImages.get(position))
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(holder.picBitmap);
        } else {
            Glide.with(context).load(R.drawable.ic_add_black)
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(holder.picBitmap);
        }

        holder.bitmapTitle.setText(pictorials.get(position).getTitle());
        holder.relevanceTV.setText(pictorials.get(position).getRelevance());

        holder.renameTV.setOnClickListener(view -> {
            String rename = holder.renameET.getText().toString().trim();
            if (rename.equals("")) {
                Toast.makeText(context, "No changes..", Toast.LENGTH_SHORT).show();
                holder.renameRel.setVisibility(View.GONE);
                holder.bitmapTitle.setVisibility(View.VISIBLE);
            } else {
                String prevName = holder.renameTV.getText().toString().trim();
                ArrayList<BitMapTitle> currentSit = PhotoDoc.Companion.loadPictorials1(context, "bitmapTitle");
                ArrayList<PictorialObject> pictorials1 = PhotoDoc.Companion
                        .loadPictorials(context, holder.bitmapTitle.getText().toString());

                BitMapTitle title = pictorials.get(holder.getAbsoluteAdapterPosition());
                title.setTitle(rename);
                PhotoDoc.Companion.savePictorials1(context, "bitmapTitle", currentSit);
                PhotoDoc.Companion.savePictorials(context, rename, pictorials1);
                SharedPreferences pref = context.getSharedPreferences(prevName, Context.MODE_PRIVATE);
                pref.edit().clear().apply();
                holder.bitmapTitle.setText(rename);
                holder.renameRel.setVisibility(View.GONE);
                holder.bitmapTitle.setVisibility(View.VISIBLE);
                Toast.makeText(context, "Changes saved..", Toast.LENGTH_SHORT).show();

            }
        });

        holder.bitOptions.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.bitOptions);
            popupMenu.getMenuInflater().inflate(R.menu.bit_options, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                switch (item.getItemId()) {
                    case R.id.renameBitmap:
                        holder.renameRel.setVisibility(View.VISIBLE);
                        holder.bitmapTitle.setVisibility(View.GONE);

                        break;
                    case R.id.publishBitmaps:
                        if (user != null) {
                            if (holder.relevanceTV.getText().toString().equals("")){
                                Toast.makeText(context, "Brief description required.", Toast.LENGTH_LONG).show();
                                EnrollmentDialog relevance = new EnrollmentDialog(context, user,
                                        null, holder.getAbsoluteAdapterPosition(),
                                        "relevance", holder.bitmapTitle.getText().toString());
                                relevance.show();
                            }else{
                                Publisher.Comp.publishPictorial(context,
                                        holder.bitmapTitle.getText().toString(), user, holder.publishProgress,
                                        holder.stopUpload, "", holder.getAbsoluteAdapterPosition(), bitMapTitle);
                            }

                        } else {
                            LogInSignUp logInSignUp = new LogInSignUp(context);
                            logInSignUp.show();
                        }
                        break;

                    case R.id.relevance:
                        if (user == null) {
                            LogInSignUp logInSignUp = new LogInSignUp(context);
                            logInSignUp.show();
                        } else {
                            EnrollmentDialog relevance = new EnrollmentDialog(context, user,
                                    null, holder.getAbsoluteAdapterPosition(),
                                    "relevance", holder.bitmapTitle.getText().toString());
                            relevance.show();
                        }
                        break;

                    case R.id.deleteBitmap:
                        AlertDialog areYouSure = new AlertDialog.Builder(context)
                                .setTitle("Delete pictorial?")
                                .setMessage("Do you wish to delete " + holder.bitmapTitle.getText().toString() + "?")
                                .setPositiveButton("Yup!", (dialog, which) -> {
                                    BitMapTitle pictorial = pictorials.get(holder.getAbsoluteAdapterPosition());
                                    pictorials.remove(pictorial);
                                    PhotoDoc.Companion.savePictorials1(context, "bitmapTitle", pictorials);
                                    PicsAdapter.this.notifyItemRemoved(holder.getAbsoluteAdapterPosition());
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


    }

    @Override
    public int getItemCount() {
        return pictorials.size();
    }

    public static class PicsVH extends RecyclerView.ViewHolder {
        ImageView picBitmap;
        ImageButton stopUpload;
        TextView bitmapTitle, viewCount, commentsCount, renameTV, relevanceTV;
        CardView ratingsCard;
        Button bitOptions;
        ProgressBar publishProgress;
        RelativeLayout renameRel;
        EditText renameET;

        public PicsVH(@NonNull View itemView) {
            super(itemView);
            renameTV = itemView.findViewById(R.id.renameTV);
            viewCount = itemView.findViewById(R.id.viewCount);
            relevanceTV = itemView.findViewById(R.id.relevanceTV);
            renameET = itemView.findViewById(R.id.renameET);
            renameRel = itemView.findViewById(R.id.renameRel);
            stopUpload = itemView.findViewById(R.id.stopUpload);
            commentsCount = itemView.findViewById(R.id.commentsCount);
            ratingsCard = itemView.findViewById(R.id.ratingCard);
            picBitmap = itemView.findViewById(R.id.picBitmap);
            bitmapTitle = itemView.findViewById(R.id.bitmapTitle);
            bitOptions = itemView.findViewById(R.id.bitOptions);
            publishProgress = itemView.findViewById(R.id.publishProgress);
        }
    }
}
