package com.midland.ynote.Activities;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.midland.ynote.R;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

public class StreamActivity extends AppCompatActivity {

    private SimpleExoPlayer exoPlayer;
    private PlayerView playerView;

    private Context c;
//    private TextView streamVidName;
    private ImageView vidRating, vidComments;
    private String vidName;
    private Uri vidUri;
    private String nodeName;

    private RelativeLayout ratingRel;
    private Button submitButton;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);
        initViewSnActions();
    }

    private void initViewSnActions() {
        vidName = getIntent().getStringExtra("vidName");
        vidUri = Uri.parse(getIntent().getStringExtra("vidUri"));
        nodeName = getIntent().getStringExtra("nodeName");
//        streamVidName = findViewById(R.id.streamVidNameActivity);
        playerView = findViewById(R.id.streamExoPlayerActivity);
        vidRating = findViewById(R.id.vidRatingActivity);
        vidComments = findViewById(R.id.vidCommentsActivity);
        ratingRel = findViewById(R.id.ratingRelActivity);
        ratingBar = findViewById(R.id.ratingBarActivity);
        submitButton = findViewById(R.id.submitRateActivity);
        setExoPlayer(getApplication(), vidName, vidUri);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = String.valueOf(ratingBar.getRating());
                Toast.makeText(c, s + " stars", Toast.LENGTH_SHORT).show();
                SystemClock.sleep(1300);
                ratingRel.setVisibility(View.GONE);
            }
        });

        vidRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ratingRel.getVisibility() == View.GONE){
                    ratingRel.setVisibility(View.VISIBLE);
                }else {
                    ratingRel.setVisibility(View.GONE);
                }
            }
        });

        vidComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void setExoPlayer(Application app, String videoName, Uri videoUri){
//        streamVidName.setText(videoName);
        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(app).build();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(app);

            DefaultHttpDataSourceFactory sourceFactory = new DefaultHttpDataSourceFactory("video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videoUri,
                    sourceFactory, extractorsFactory, null, null);
            playerView.setPlayer(exoPlayer);
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            exoPlayer.setPlayWhenReady(true);


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("StreamingDialog", "exoPlayer error " + e.toString());
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
}