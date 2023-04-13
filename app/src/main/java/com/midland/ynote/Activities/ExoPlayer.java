package com.midland.ynote.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.midland.ynote.R;
import com.midland.ynote.Utilities.VideoPlayerConfig;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExoPlayer extends AppCompatActivity {


    @BindView(R.id.buttonPlayUrlVideo)
    Button buttonPlayUrlVideo;
    @BindView(R.id.buttonPlayDefaultVideo)
    Button buttonPlayDefaultVideo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        Button buttonPlayDefaultVideo = findViewById(R.id.buttonPlayDefaultVideo);
        Button buttonPlayUrlVideo = findViewById(R.id.buttonPlayUrlVideo);
       buttonPlayDefaultVideo.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent mIntent = ExoPlayerActivity.getStartIntent(ExoPlayer.this, VideoPlayerConfig.DEFAULT_VIDEO_URL);
               startActivity(mIntent);
           }
       });

       buttonPlayUrlVideo.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               showDialogPrompt();
           }
       });
    }

    private void showDialogPrompt() {
        // get dialog_prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialog_prompts, null);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder( this);
        // set dialog_prompts.xml to dialog
        mBuilder.setView(promptsView);
        final EditText userInputURL = (EditText) promptsView
                .findViewById(R.id.editTextDialogUrlInput);
        // set dialog message here
        mBuilder.setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                boolean isURL = Patterns.WEB_URL.matcher(userInputURL.getText().toString().trim()).matches();
                                if (isURL) {
                                    Intent mIntent = ExoPlayerActivity.getStartIntent(ExoPlayer.this, userInputURL.getText().toString().trim());
                                    startActivity(mIntent);
                                } else {
                                    Toast.makeText(ExoPlayer.this,"Something's up!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).create().show();
    }
}