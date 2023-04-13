package com.midland.ynote.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.midland.ynote.R;
import com.midland.ynote.Activities.CloudPhotoDocs;
import com.midland.ynote.Activities.UserProfile2;
import com.midland.ynote.Fragments.MainFragment1;
import com.midland.ynote.Objects.BitMapTitle;
import com.midland.ynote.TemplateView;
import com.midland.ynote.Utilities.AdMob;
import com.midland.ynote.Utilities.FilingSystem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;

public class HomeBitmapsAdapter extends RecyclerView.Adapter<HomeBitmapsAdapter.BitmapsVH> {

    private final ArrayList<BitMapTitle> cloudMaps;
    private final Context c;
    private final Activity a;

    public HomeBitmapsAdapter(ArrayList<BitMapTitle> cloudMaps, Context c, Activity a) {
        this.cloudMaps = cloudMaps;
        this.c = c;
        this.a = a;
    }

    @NonNull
    @NotNull
    @Override
    public BitmapsVH onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pictorial_obj, parent, false);
        return new BitmapsVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BitmapsVH holder, int position) {

        if (position%5 == 0 && position != 0){
            AdMob.Companion.nativeAd(c, a, holder.nativeTemplate, holder.templateViewCard) ;
        }
        final BitMapTitle currentMap = cloudMaps.get(holder.getAbsoluteAdapterPosition());
        String strRating = "0.0";
        holder.ratingCard.setVisibility(View.VISIBLE);
        holder.commentsCount.setText(currentMap.getCommentsCount());
        holder.viewCount.setText(currentMap.getViewsCount());
        holder.bitTitle.setText(currentMap.getTitle());
        holder.publisher.setText(currentMap.getDisplayName());
        holder.slideCount.setText(currentMap.getPictures1().size() + " slides");
        holder.publisher.setOnClickListener(v -> {
            Intent intent = new Intent(c, UserProfile2.class);
            intent.putExtra("userID", currentMap.getUid());
            c.startActivity(intent);
        });


        if (currentMap.getThumbNail() != null) {
//            Picasso.get().load(currentMap.getThumbNail())
//                    .placeholder(R.drawable.ic_hourglass)
//                    .into(holder.bitThumb);
            Glide.with(c).load(currentMap.getThumbNail())
                    .placeholder(R.drawable.button_black)
                    .thumbnail((float) 0.9).into(holder.bitThumb);

            holder.bitThumb.setOnClickListener(view -> {
                MainFragment1.getTouchIV().setImageResource(R.drawable.ic_hourglass);
                MainFragment1.getTouchIV().setVisibility(View.VISIBLE);
                MainFragment1.getTouchIV().bringToFront();
                FilingSystem.Companion.downloadImage(MainFragment1.getTouchIV(), currentMap.getThumbNail());
            });
        } else {
            Glide.with(c).load(R.drawable.ic_hourglass_bottom_white).thumbnail((float) 0.9).into(holder.bitThumb);
        }


        if (currentMap.getRatings() != 0 && currentMap.getRatersCount() != 0){
            float ratings = (float) (currentMap.getRatings() / currentMap.getRatersCount());
            strRating = String.format(Locale.US, "%.1f", ratings);
        }
        holder.ratings.setText(strRating);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(c, CloudPhotoDocs.class);
            intent.putExtra("fileName", holder.bitTitle.getText().toString());
            intent.putExtra("commentsCount", holder.commentsCount.getText().toString());
            intent.putStringArrayListExtra("pictures", currentMap.getPictures1());
            intent.putExtra("userID", currentMap.getUid());
            intent.putStringArrayListExtra("narrations", currentMap.getNarrations1());
            intent.putStringArrayListExtra("descriptions", currentMap.getDescriptions1());
            c.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cloudMaps.size();
    }

    public static class BitmapsVH extends RecyclerView.ViewHolder {
        public final ImageView bitThumb;
        public final TextView bitTitle, viewCount, ratings, commentsCount, publisher, slideCount;
        public final Button bitOptions;
        public CardView ratingCard;
        public CardView templateViewCard;
        TemplateView nativeTemplate;


        public BitmapsVH(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            bitThumb = itemView.findViewById(R.id.picBitmap);
            bitTitle = itemView.findViewById(R.id.bitmapTitle);
            nativeTemplate = itemView.findViewById(R.id.nativeTemplate);
            slideCount = itemView.findViewById(R.id.slideCount);
            bitOptions = itemView.findViewById(R.id.bitOptions);
            ratingCard = itemView.findViewById(R.id.ratingCard);
            publisher = itemView.findViewById(R.id.publisher);
            viewCount = itemView.findViewById(R.id.viewCount);
            ratings = itemView.findViewById(R.id.ratings);
            templateViewCard = itemView.findViewById(R.id.templateCard);
            commentsCount = itemView.findViewById(R.id.commentsCount);
        }
    }
}
