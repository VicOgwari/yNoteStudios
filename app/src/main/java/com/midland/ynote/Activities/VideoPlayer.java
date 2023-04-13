package com.midland.ynote.Activities;


import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.midland.ynote.R;


public class VideoPlayer extends AppCompatActivity {

    VideoView videoView;
    private int mCurrentPosition = 0;
    private static final String PLAYBACK_TIME = "play_time";

    //INTERNET THINGS
    private static final String VIDEO_SAMPLE = "https://developers.google.com/training/images/tacoma_narrows.mp4";
    private TextView mBufferingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME);
        }

        mBufferingTextView = findViewById(R.id.buffering_textview);
        TextView title = findViewById(R.id.noteDetailTitle);
        videoView = findViewById(R.id.vidView);


        Intent data = getIntent();

        if (data.getStringExtra("resource").equals("memory")){
            mBufferingTextView.setVisibility(VideoView.INVISIBLE);
            title.setText(data.getStringExtra("vidTitle"));
            videoView.setVideoURI(Uri.parse(data.getStringExtra("vidUri")));

        }else if (data.getStringExtra("resource").equals("internet")){
            mBufferingTextView.setVisibility(VideoView.VISIBLE);
            videoView.setVideoURI(getMedia(data.getStringExtra("url")));
        }

        if (mCurrentPosition > 0) {
            videoView.seekTo(mCurrentPosition);
        } else {
            // Skipping to 1 shows the first frame of the video.
            videoView.seekTo(1);
        }

        videoView.start();

        MediaController controller = new MediaController(this);
        controller.setMediaPlayer(videoView);
        videoView.setMediaController(controller);

        videoView.setOnPreparedListener(
                new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mBufferingTextView.setVisibility(VideoView.INVISIBLE);

                        if (mCurrentPosition > 0) {
                            videoView.seekTo(mCurrentPosition);
                        } else {
                            videoView.seekTo(1);
                        }

                        videoView.start();                    }
                });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(PLAYBACK_TIME, videoView.getCurrentPosition());
    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            videoView.pause();
        }
    }

    private void releasePlayer() {
        videoView.stopPlayback();
    }

//    to test whether the incoming string is a URL or a raw resource
    private Uri getMedia(String mediaName) {
        if (URLUtil.isValidUrl(mediaName)) {
            // media name is an external URL
            return Uri.parse(mediaName);
        } else { // media name is a raw resource embedded in the app
            return Uri.parse("android.resource://" + getPackageName() +
                    "/raw/" + mediaName);
        }
    }
}