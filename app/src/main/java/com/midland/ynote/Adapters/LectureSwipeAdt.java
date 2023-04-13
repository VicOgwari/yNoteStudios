package com.midland.ynote.Adapters;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.midland.ynote.R;
import com.midland.ynote.Utilities.VideoPlayerConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LectureSwipeAdt extends RecyclerView.Adapter<LectureSwipeAdt.LectureVH> implements Player.EventListener {

    private List<String> videos;
    private Activity a;
    private Context c;
    private Application app;
    private String video;
    private SimpleExoPlayer exoPlayer;
    private ProgressBar progress;

    public LectureSwipeAdt(Context c, Application app, Activity a, List<String> videos) {
        this.c = c;
        this.app = app;
        this.videos = videos;
        this.a = a;
    }

    @NonNull
    @Override
    public LectureVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LectureVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_lecture_obj, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LectureVH holder, int position) {

        progress = holder.loadProgress;
        video = videos.get(holder.getAbsoluteAdapterPosition());
        holder.vidTitle.setText(video.split("_-_")[0]);
        holder.vidDesc.setText(video.split("_-_")[1]);

    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull LectureVH holder) {
        super.onViewAttachedToWindow(holder);

        String vidLink = video.split("_-_")[2];
        String generalDesc = video.split("_-_")[4];
        String nodeName = video.split("_-_")[5];

        initiate(c, app, vidLink, generalDesc, nodeName, holder.playerView, holder.loadProgress, holder.submitButton, holder.ratingBar, holder.ratingRel,
                holder.vidComments, holder.closeDialog, holder.vidRating);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull LectureVH holder) {
        super.onViewDetachedFromWindow(holder);
        releasePlayer();
    }

    public static class LectureVH extends RecyclerView.ViewHolder {
        PlayerView playerView;
        TextView vidDesc, vidTitle;
        ProgressBar loadProgress;
        ImageView closeDialog, vidRating, vidComments;
        RelativeLayout ratingRel;
        Button submitButton;
        RatingBar ratingBar;

        public LectureVH(@NonNull View itemView) {
            super(itemView);

            loadProgress = itemView.findViewById(R.id.loadProgress);
            playerView = itemView.findViewById(R.id.streamExoPlayer);
            closeDialog = itemView.findViewById(R.id.imageViewExit);
            vidRating = itemView.findViewById(R.id.vidRatings);
            vidComments = itemView.findViewById(R.id.vidComments);
            ratingRel = itemView.findViewById(R.id.ratingRel);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            vidTitle = itemView.findViewById(R.id.vidTitle);
            vidDesc = itemView.findViewById(R.id.vidDesc);
            submitButton = itemView.findViewById(R.id.submitRate);

        }


    }

    private void initiate(Context c,
                          Application app,
                          String vidLink,
                          String generalDesc,
                          String nodeName,
                          PlayerView playerView,
                          ProgressBar loadProgress,
                          Button submitButton,
                          RatingBar ratingBar,
                          RelativeLayout ratingRel,
                          ImageView vidComments,
                          ImageView closeDialog,
                          ImageView vidRating) {

        DocumentReference lectureDbRef = FirebaseFirestore.getInstance()
                .collection("Content")
                .document("Lectures")
                .collection(generalDesc)
                .document(nodeName);

        submitButton.setOnClickListener(v -> {
            Map<String, Object> ratings = new HashMap<>();
            ratings.put("ratersCount", FieldValue.increment(1));
            ratings.put("ratings", FieldValue.increment(ratingBar.getRating()));
            lectureDbRef.update(ratings).addOnSuccessListener(unused -> {
                String s = String.valueOf(ratingBar.getRating());
                Toast.makeText(c, s + " stars", Toast.LENGTH_SHORT).show();
                SystemClock.sleep(1300);
                ratingRel.setVisibility(View.GONE);
            }).addOnFailureListener(e -> {
                Toast.makeText(c, "Something's up! Try again.", Toast.LENGTH_SHORT).show();
            });


        });
        setExoPlayer(app, vidLink, playerView, loadProgress);

        closeDialog.setOnClickListener(v -> {
            releasePlayer();
            a.onBackPressed();
        });

        vidRating.setOnClickListener(v -> {

            if (ratingRel.getVisibility() == View.GONE) {
                ratingRel.setVisibility(View.VISIBLE);
                ratingRel.bringToFront();
            } else {
                ratingRel.setVisibility(View.GONE);
            }

        });

        vidComments.setOnClickListener(v -> {

        });

        if (vidLink != null) {
            setUp(Uri.parse(vidLink), c, playerView);
        } else {
            Toast.makeText(c, "Null vid", Toast.LENGTH_SHORT).show();
        }
    }

    private void setExoPlayer(Application app, String selectedVideo, PlayerView playerView, ProgressBar loadProgress) {
//        streamVidName.setText(selectedVideo.getVideoDescription().split("_-_")[0]);
        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(app).build();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(app);

            DefaultHttpDataSourceFactory sourceFactory = new DefaultHttpDataSourceFactory("video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(selectedVideo),
                    sourceFactory, extractorsFactory, null, null);
            playerView.setPlayer(exoPlayer);
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
            exoPlayer.prepare(mediaSource);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            exoPlayer.setPlayWhenReady(false);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("StreamingDialog", "exoPlayer error " + e.toString());
        }

        if (exoPlayer.isLoading()) {
            loadProgress.setVisibility(View.VISIBLE);
        } else if (exoPlayer.isPlaying()) {
            loadProgress.setVisibility(View.GONE);
        }
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    private void pausePlayer() {
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.getPlaybackState();
        }
    }

    private void resumePlayer() {
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(true);
            exoPlayer.getPlaybackState();
        }
    }

    private void setUp(Uri videoUri, Context c, PlayerView playerView) {
        initializePlayer(c, playerView);
        if (videoUri == null) {
            return;
        }
        buildMediaSource(videoUri, c);
    }

    private void initializePlayer(Context c, PlayerView playerView) {
        if (exoPlayer == null) {
            // 1. Create a default TrackSelector
            LoadControl loadControl = new DefaultLoadControl(
                    new DefaultAllocator(true, 16),
                    VideoPlayerConfig.MIN_BUFFER_DURATION,
                    VideoPlayerConfig.MAX_BUFFER_DURATION,
                    VideoPlayerConfig.MIN_PLAYBACK_START_BUFFER,
                    VideoPlayerConfig.MIN_PLAYBACK_RESUME_BUFFER, -1, true);

            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();//Diff
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);
            // 2. Create the player
            exoPlayer = ExoPlayerFactory.newSimpleInstance(c,
                    new DefaultRenderersFactory(c), trackSelector, loadControl);
            playerView.setPlayer(exoPlayer);
        }
    }

    private void buildMediaSource(Uri mUri, Context c) {
        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DefaultHttpDataSourceFactory sourceFactory = new DefaultHttpDataSourceFactory("video");
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(c,
                Util.getUserAgent(c, c.getString(R.string.app_name)), bandwidthMeter);
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mUri);
//        MediaSource videoSource = new ExtractorMediaSource(mUri, sourceFactory, extractorsFactory, null, null);

        // Prepare the player with the source.
        exoPlayer.prepare(videoSource);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.addListener(this);
    }


//    @Override
//    protected void onPause() {
//        super.onPause();
//        pausePlayer();
//        if (mRunnable != null) {
//            mHandler.removeCallbacks(mRunnable);
//        }
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        resumePlayer();
//    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {

            case Player.STATE_BUFFERING:
                progress.setVisibility(View.VISIBLE);
//                spinnerVideoDetails.setVisibility(View.VISIBLE);
                break;
            case Player.STATE_ENDED:
                // Activate the force enable
                break;
            case Player.STATE_IDLE:

                break;
            case Player.STATE_READY:
                progress.setVisibility(View.GONE);
//                spinnerVideoDetails.setVisibility(View.GONE);

                break;
            default:
                // status = PlaybackStatus.IDLE;
                break;
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

}
