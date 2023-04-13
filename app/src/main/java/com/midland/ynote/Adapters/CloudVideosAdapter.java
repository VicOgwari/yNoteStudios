package com.midland.ynote.Adapters;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.midland.ynote.Activities.LecturesList;
import com.midland.ynote.Activities.StreamingActivity;
import com.midland.ynote.Activities.UserProfile2;
import com.midland.ynote.Objects.SelectedDoc;
import com.midland.ynote.R;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CloudVideosAdapter extends RecyclerView.Adapter<CloudVideosAdapter.CloudVidVH> implements Filterable {

    private Context context, con;
    private final ArrayList<SelectedDoc> cloudVideos;
    private final ArrayList<SelectedDoc> vidList;
    private Application application;
    private Activity a;
    private final PlayerView playerView;

    private final Application app;
    private LecturesList lecturesList;
    public int vidPosition;
    private int vidCommentCount;


    public CloudVideosAdapter(LecturesList lecturesList,
                              PlayerView playerView,
                              Application app, Context context, Context con, ArrayList<SelectedDoc> cloudVideos, Application application, Activity a) {
        this.context = context;
        this.lecturesList = lecturesList;
        this.con = con;
        this.cloudVideos = cloudVideos;
        this.application = application;
        this.a = a;
        this.playerView = playerView;
        this.app = app;
        vidList = new ArrayList<>(cloudVideos);
    }





    @NonNull
    @Override
    public CloudVidVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CloudVidVH(LayoutInflater.from(context).inflate(R.layout.vid_object, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CloudVidVH holder, final int position) {
//        holder.cloudVideoView.setVideoURI(Uri.parse(cloudVideos.get(position).getVideoUri()));
        String strRating = "0.0";
        int vidViews = 0;
        String saveCount = "";
        vidPosition = holder.getAbsoluteAdapterPosition();
        String vidTitle = cloudVideos.get(position).getDocMetaData().split("_-_")[0];
        final String generalDesc = cloudVideos.get(position).getDocMetaData().split("_-_")[1];
        String displayName = cloudVideos.get(position).getDocMetaData().split("_-_")[7];
//        if (cloudVideos.get(position).getDocMetaData().split("_-_").length > 7){
//            int relTint = Integer.parseInt(cloudVideos.get(position).getDocMetaData().split("_-_")[8]);
//            holder.colorRel.setBackgroundColor(relTint);
//        }
        final String uID = cloudVideos.get(position).getDocMetaData().split("_-_")[6];
        final String downloadLink = cloudVideos.get(position).getDocMetaData().split("_-_")[5];
        final String nodeName = cloudVideos.get(position).getDocDownloadLink();

        holder.vidTitle.setText(vidTitle);

        try {
            vidCommentCount = cloudVideos.get(position).getCommentsCount();
            float vidRatings = cloudVideos.get(position).getRatings().floatValue();
            int vidRaters = cloudVideos.get(position).getRatersCount();
            vidViews = cloudVideos.get(position).getViews();
            saveCount = String.valueOf(cloudVideos.get(position).getSaveCount());


            if (vidRaters != 0 && vidRatings != 0){
                float ratings = vidRatings / vidRaters;
                strRating = String.format(Locale.US, "%.1f", ratings);
            }

            holder.videoCommentCount.setText(String.valueOf(vidCommentCount));
            holder.vidRating.setText(strRating);
            holder.vidViews.setText(String.valueOf(vidViews));
        }catch (Exception e){
            e.printStackTrace();
            holder.vidRating.setText(strRating);
            holder.vidViews.setText(String.valueOf(vidViews));

        }



        holder.videoOptions.setOnClickListener(v -> {
            if (holder.handleRel.getVisibility() == View.VISIBLE){
                holder.handleRel.setVisibility(View.GONE);
                holder.titleLay.setVisibility(View.GONE);
                holder.statsLayout.setVisibility(View.GONE);
            }else {
                holder.handleRel.setVisibility(View.VISIBLE);
                holder.titleLay.setVisibility(View.VISIBLE);
                holder.statsLayout.setVisibility(View.VISIBLE);
            }
        });

        holder.userHandle.setText(displayName);
        holder.userHandle.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserProfile2.class);
            intent.putExtra("userID", uID);
            if (con != null)
            con.startActivity(intent);
        });

        String finalStrRating = strRating;
        String finalSaveCount = saveCount;
        holder.itemView.setOnClickListener(v -> {
//                ExoPlayerDialog exoPlayerDialog = new ExoPlayerDialog(context, a, cloudVideos.get(position).getVideoUri());
//                exoPlayerDialog.show();

//                viewerRel.setVisibility(View.VISIBLE);
//                setExoPlayer(app, cloudVideos.get(position));

//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setDataAndType(Uri.parse(downloadLink), "video/*");
//            con.startActivity(Intent.createChooser(intent, "Complete action with..."));



            Intent intent1 = new Intent(context, StreamingActivity.class);
            intent1.putExtra("scrollTo", holder.getAbsoluteAdapterPosition());
            intent1.putExtra("cloudVideo", downloadLink);
            intent1.putExtra("generalDesc", generalDesc);
            intent1.putExtra("videos", cloudVideos);
            intent1.putExtra("nodeName", nodeName);
            intent1.putExtra("publisherId", uID);
            intent1.putExtra("commentsCount", String.valueOf(vidCommentCount));
            intent1.putExtra("saveCount", finalSaveCount);
            intent1.putExtra("ratings", finalStrRating);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);



//            StreamingDialog streamingDialog = new StreamingDialog(context, context, application, cloudVideos.get(position));
//            streamingDialog.show();

//                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                retriever.setDataSource(context, Uri.parse(cloudVideos.get(position).getDocMetaData().split("_-_")[5]));
//                String vidLength = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//                long timeInMillis = Long.parseLong(vidLength);
//                Toast.makeText(context, vidLength, Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, (int) timeInMillis, Toast.LENGTH_SHORT).show();
//                retriever.release();
        });

        Glide.with(context).load(cloudVideos.get(position).getThumbNail())
                .placeholder(R.drawable.ic_launcher_background).into(holder.cloudVideoView);


    }

    @Override
    public int getItemCount() {
        return cloudVideos.size();
    }

    public static class CloudVidVH extends RecyclerView.ViewHolder {
        TextView vidRating, vidViews, userHandle, videoCommentCount;
        ReadMoreTextView vidTitle;
        LinearLayout commentsLayout, ratingsLayout;
        ImageView cloudVideoView;
        RelativeLayout colorRel, titleLay, statsLayout, handleRel;
        ImageButton comments, ratings, videoOptions;

        public CloudVidVH(@NonNull View itemView) {
            super(itemView);
            commentsLayout = itemView.findViewById(R.id.vidCommentsLayout);
            ratingsLayout = itemView.findViewById(R.id.ratingLayout);
            vidTitle = itemView.findViewById(R.id.videoTitle);
            vidRating = itemView.findViewById(R.id.videoRating);
            videoOptions = itemView.findViewById(R.id.videoOptions);
            vidViews = itemView.findViewById(R.id.videoViewCount);
            cloudVideoView = itemView.findViewById(R.id.videoThumbnail);
            videoCommentCount = itemView.findViewById(R.id.videoCommentCount);
            userHandle = itemView.findViewById(R.id.userHandleUpload);
            colorRel = itemView.findViewById(R.id.colorRel);
            handleRel = itemView.findViewById(R.id.handleRel);
            titleLay = itemView.findViewById(R.id.titleLay);
            statsLayout = itemView.findViewById(R.id.statsLayout);
            comments = itemView.findViewById(R.id.comments);
            ratings = itemView.findViewById(R.id.ratings);
        }
    }


    public static Bitmap thumbnailGen(String vidPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = null;
        try {
            retriever = new MediaMetadataRetriever();
            retriever.setDataSource(vidPath, new HashMap<String, String>());
            retriever.setDataSource(vidPath);
            bitmap = retriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in thumbnailGen" + e.getMessage());
        } finally {
            if (retriever != null) {
                retriever.release();
            }
        }
        return bitmap;
    }

//    private void setExoPlayer(Application app, SelectedDoc selectedVideo) {
////        streamVidName.setText(selectedVideo.getDocMetaData().split("_-_")[0]);
//        try {
//            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(app).build();
//            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
//            exoPlayer = ExoPlayerFactory.newSimpleInstance(app);
//
//            DefaultHttpDataSourceFactory sourceFactory = new DefaultHttpDataSourceFactory("video");
//            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
//            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(selectedVideo.getDocMetaData().split("_-_")[5]),
//                    sourceFactory, extractorsFactory, null, null);
//            playerView.setPlayer(exoPlayer);
//            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
//            exoPlayer.prepare(mediaSource);
//            exoPlayer.prepare(mediaSource);
//            exoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
//            exoPlayer.setPlayWhenReady(false);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("StreamingDialog", "exoPlayer error " + e.toString());
//        }
//    }


//    private void postComment(FirebaseUser user) {
//        String comment = lecCommentET.getText().toString().trim();
//        if (TextUtils.isEmpty(comment)) {
//            Toast.makeText(context, "Say something....", Toast.LENGTH_SHORT).show();
//        }
//
//        Calendar callForDate = Calendar.getInstance();
//        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
//        String saveCurrentDate = currentDate.format(callForDate.getTime());
//
//        Calendar callForTime = Calendar.getInstance();
//        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
//        String saveCurrentTime = currentTime.format(callForTime.getTime());
//
//        String uId = user.getUid();
//        String userName = user.getDisplayName();
//
//        ref.document(cloudVideos.get(vidPosition).getDocDownloadLink())
//                .collection("Comments")
//                .document(String.valueOf(System.currentTimeMillis()))
//                .set(new CommentsObject(userName + "_-_" + uId + "_-_" + comment + "_-_" + saveCurrentDate + "_-_" + saveCurrentTime))
//                .addOnSuccessListener(aVoid -> {
//                    ref.document(cloudVideos.get(vidPosition).getDocDownloadLink()).update("commentsCount", FieldValue.increment(1));
//                    Toast.makeText(context, "Posted!", Toast.LENGTH_SHORT).show();
//                    lecCommentET.setText("");
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(context, "Something's interrupting your post.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//
//    }

//    private void getComments() {
//        comments = new ArrayList<>();
//        ref.document(cloudVideos.get(vidPosition).getDocDownloadLink())
//                .collection("Comments").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                ProgressDialog progressDialog = new ProgressDialog(con);
//                for (QueryDocumentSnapshot qds : queryDocumentSnapshots) {
//                    String object = qds.getString("comment");
//                    comments.add(new CommentsObject(object));
//                }
//
//                CommentsAdapter commentsAdapter = new CommentsAdapter(context, comments);
//                commentsAdapter.notifyDataSetChanged();
//                lecCommentRV.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
//                lecCommentRV.setAdapter(commentsAdapter);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//    }

    @Override
    public Filter getFilter() {
        return filter;
    }



    public final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<SelectedDoc> yearFilteredDocs = new ArrayList<>();
            List<SelectedDoc> stringFilteredDocs = new ArrayList<>();
//            if (DocRetrieval.Companion.getYearFilterString().equals("All")) {
            if (constraint == null || constraint.length() == 0) {
                stringFilteredDocs.addAll(vidList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (SelectedDoc doc : vidList) {
                    if (doc.getDocMetaData().toLowerCase().contains(filterPattern)) {
                        stringFilteredDocs.add(doc);
                    }
                }

            }
//            }
//            else {
//                if (constraint == null || constraint.length() == 0) {
//                    for (SelectedDoc doc : documentsList) {
//                        if (doc.getDocMetaData().split("_-_")[2].toLowerCase().contains(DocRetrieval.Companion.getYearFilterString())) {
//                            yearFilteredDocs.add(doc);
//                        }
//                    }
//                    stringFilteredDocs.addAll(yearFilteredDocs);
//                } else {
//                    String filterPattern = constraint.toString().toLowerCase().trim();
//                    for (SelectedDoc doc : yearFilteredDocs) {
//                        if (doc.getDocMetaData().split("_-_")[5].toLowerCase().startsWith(filterPattern)) {
//                            stringFilteredDocs.add(doc);
//                        }
//                    }
//
//                }
//            }
            FilterResults results = new FilterResults();
            results.values = stringFilteredDocs;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            cloudVideos.clear();
            cloudVideos.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
    public String replacer(String docName) {
        String docName1 = docName.replace("]", "");
        String docName2 = docName1.replace("[", "");
        String docName3 = docName2.replace(".", "");
        String docName4 = docName3.replace("$", "");
        String docName5 = docName4.replace("*", "");
        return docName5.replace("#", "");
    }


}

