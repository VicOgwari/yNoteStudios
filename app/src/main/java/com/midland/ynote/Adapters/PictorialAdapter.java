package com.midland.ynote.Adapters;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;
import com.midland.ynote.Activities.PhotoDoc;
import com.midland.ynote.Fragments.StudioBitmaps;
import com.midland.ynote.Objects.PictorialObject;
import com.midland.ynote.R;
import com.midland.ynote.TemplateView;
import com.midland.ynote.Utilities.AdMob;
import com.midland.ynote.Utilities.FilingSystem;
import com.midland.ynote.Utilities.ItemTouchAdapter;
import com.midland.ynote.Utilities.Projector;
import com.ortiz.touchview.TouchImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;


public class PictorialAdapter extends RecyclerView.Adapter<PictorialAdapter.PicAdapterVH>
        implements ItemTouchAdapter {

    Context con;
    ArrayList<PictorialObject> pictorialObjects;
    String fileName, outPutFile;
    ContentValues contentValues;
    ImageButton fab;
    Activity a;
    Boolean isRecording = false, isPlaying = false;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private File narration;
    private String flag;
    private PhotoDoc photoDoc;
    private StudioBitmaps studioBitmaps;
    private ItemTouchHelper itemTouchHelper;
    private TouchImageView touchIV;

    public PictorialAdapter(Context con, ArrayList<PictorialObject> pictorialObjects,
                            String fileName, ImageButton fab, Activity a,
                            PhotoDoc photoDoc, StudioBitmaps studioBitmaps, String flag, TouchImageView touchIV) {
        this.con = con;
        this.pictorialObjects = pictorialObjects;
        this.fileName = fileName;
        this.fab = fab;
        this.a = a;
        this.photoDoc = photoDoc;
        this.studioBitmaps = studioBitmaps;
        this.flag = flag;
        this.touchIV = touchIV;
    }

    @NonNull
    @Override
    public PicAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PicAdapterVH(LayoutInflater.from(con).inflate(R.layout.photo_doc_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final PicAdapterVH holder, final int position) {

        final PictorialObject pictorialObject = pictorialObjects.get(holder.getAbsoluteAdapterPosition());


        if (pictorialObject.getNarrationPath() == null
                || pictorialObject.getNarrationPath().equals("")
                || !(new File(pictorialObject.getNarrationPath()).exists())) {

            holder.playNarration.setVisibility(View.GONE);
        } else {
            holder.playNarration.setVisibility(View.VISIBLE);

        }

        if (touchIV != null){
            touchIV.bringToFront();
        }

        if (flag.equals("online")) {

            if (position % 3 == 0 && position != 0) {
                AdMob.Companion.nativeAd(con, a, holder.nativeTemplate, holder.templateViewCard);
            }

            holder.narTimer.setVisibility(View.GONE);
            holder.narration.setVisibility(View.GONE);
            holder.saveEdits.setVisibility(View.GONE);

            if (pictorialObject.getNarrationPath().equals("null") || pictorialObject.getNarrationPath() == null) {
                holder.playNarration.setVisibility(View.GONE);
            } else {
                holder.playNarration.setVisibility(View.VISIBLE);
            }
        }

        holder.photoDocsOptions.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(con, holder.photoDocsOptions);

            if (flag.equals("online")) {
                popupMenu.getMenuInflater().inflate(R.menu.pictorial_menu1, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.projectSegment) {
                        Intent intent = new Intent(con, Projector.class);
                        ArrayList<PictorialObject> pictorials = new ArrayList<>();
                        pictorials.add(pictorialObject);
                        intent.putExtra("photoDocs", pictorials);
                        con.startActivity(intent);
                    }

                    return false;
                });
            } else if (flag.equals("studio")) {

            } else {
                popupMenu.getMenuInflater().inflate(R.menu.pictorial_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.delete:
                            PhotoDoc.Companion.deletePictorial(con, fileName, holder.getAbsoluteAdapterPosition());
                            PictorialAdapter.this.notifyItemRemoved(holder.getAbsoluteAdapterPosition());
                            break;

                        case R.id.discardAudio:
                            if (pictorialObject.getNarrationPath() == null) {
                                Toast.makeText(con, "No narration yet...", Toast.LENGTH_SHORT).show();
                            } else {
                                PhotoDoc.Companion.editDesc(con, fileName, position, pictorialObject.getPicDescription(),
                                        PictorialAdapter.this, null);
                                Toast.makeText(con, "Narration discarded.", Toast.LENGTH_SHORT).show();
                            }
                            break;

                        case R.id.selectAudio:
                            photoDoc.outSource(con, position, pictorialObject.getPicDescription());
                            break;

                        case R.id.projectSegment:
                            Intent intent = new Intent(con, Projector.class);
                            ArrayList<PictorialObject> pictorials = new ArrayList<>();
                            pictorials.add(pictorialObject);
                            intent.putExtra("photoDocs", pictorials);
                            con.startActivity(intent);

                            break;
                    }

                    return false;
                });
            }
            popupMenu.show();


        });

        holder.playNarration.setOnClickListener(view -> {
            if (flag.equals("offline")) {
                if (new File(pictorialObject.getNarrationPath()).exists()) {
                    narration = new File(pictorialObject.getNarrationPath());
                    if (mediaPlayer == null) {
                        holder.playNarration.setImageResource(R.drawable.ic_stop_circle);
                        playNarration(narration, holder.narTimer, null);
                        holder.narTimer.setVisibility(View.VISIBLE);
                        holder.narTimer.setBase(SystemClock.elapsedRealtime());
                        holder.narTimer.start();
                    } else {
                        holder.playNarration.setImageResource(R.drawable.ic_play_circle);
                        stopNarration();
//                        playNarration(narration, holder.narTimer, null);
                        holder.narTimer.stop();
                        holder.narTimer.setVisibility(View.GONE);
                    }

                } else {
                    narrationDialog("Missing narration", "Couldn't locate the narration specified for this entry. Would you like to do another narration?", pictorialObject, position, holder.narration, holder.narTimer);
                }
            } else if (flag.equals("online")) {
                if (mediaPlayer == null) {
                    playNarration(narration, holder.narTimer, pictorialObject);
                    holder.playNarration.setImageResource(R.drawable.ic_stop_circle);
                    holder.narTimer.setVisibility(View.VISIBLE);
                    holder.narTimer.setBase(SystemClock.elapsedRealtime());
                    holder.narTimer.start();
                } else {
                    stopNarration();
                    holder.playNarration.setImageResource(R.drawable.ic_play_circle);
//                    playNarration(narration, holder.narTimer, pictorialObject);
                    holder.narTimer.stop();
                    holder.narTimer.setVisibility(View.GONE);
                }
            }

        });

        holder.narration.setOnClickListener(v -> {

            if (flag.equals("offline") || flag.equals("studio")) {
                if (ContextCompat.checkSelfPermission(con, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(con, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(a, Manifest.permission.READ_EXTERNAL_STORAGE)
                            && ActivityCompat.shouldShowRequestPermissionRationale(a, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Snackbar.make(fab, "Permission", Snackbar.LENGTH_INDEFINITE)
                                .setAction("ENABLE", v1 -> ActivityCompat.requestPermissions(a, new String[]{
                                        Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
                                }, 7)).show();
                    } else {
                        ActivityCompat.requestPermissions(a, new String[]{
                                Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
                        }, 7);
                    }
                } else {

                    if (!new File(pictorialObject.getNarrationPath()).exists() || pictorialObject.getNarrationPath().equals("")) {
                        if (isRecording) {
                            isRecording = false;
                            stopRecording();
                            holder.narTimer.stop();
                            holder.narTimer.setVisibility(View.GONE);
                            holder.narration.setImageResource(R.drawable.ic_mic_off);
                            PhotoDoc.Companion.editDesc(con, fileName, position, pictorialObject.getPicDescription(), PictorialAdapter.this, outPutFile);
                            Toast.makeText(con, "Narration saved", Toast.LENGTH_SHORT).show();
                            PictorialAdapter.this.notifyDataSetChanged();
                        } else {
                            if (checkAudioPermission(a)) {
                                isRecording = true;
                                startRecording();
                                holder.narTimer.setBase(SystemClock.elapsedRealtime());
                                holder.narTimer.start();
                                holder.narration.setImageResource(R.drawable.ic_baseline_mic_24);
                                holder.narTimer.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        if (isRecording == Boolean.TRUE) {
                            isRecording = false;
                            stopRecording();
                            holder.narTimer.stop();
                            holder.narTimer.setVisibility(View.GONE);
                            holder.narration.setImageResource(R.drawable.ic_mic_off);
                            PhotoDoc.Companion.editDesc(con, fileName, position, pictorialObject.getPicDescription(), PictorialAdapter.this, outPutFile);
                            Toast.makeText(con, "Narration saved", Toast.LENGTH_SHORT).show();
                            PictorialAdapter.this.notifyDataSetChanged();
                        } else {
                            narrationDialog("Override narration?", "Do you wish to override the existing narration?", pictorialObject, position, holder.narration, holder.narTimer);
                        }
                    }
                }
            }


        });
        holder.saveEdits.setOnClickListener(v -> {
            if (holder.editPreviewDesc.getVisibility() == View.GONE) {
                holder.editPreviewDesc.setText(holder.previewDescription.getText().toString());
                holder.editPreviewDesc.setVisibility(View.VISIBLE);
                holder.previewDescription.setVisibility(View.GONE);
                holder.saveEdits.setImageResource(R.drawable.ic_done);
                if (fab != null) {
                    fab.setVisibility(View.INVISIBLE);
                }
            } else if (holder.editPreviewDesc.getVisibility() == View.VISIBLE) {
                //narration is null
                PhotoDoc.Companion.editDesc(con, fileName, position, holder.editPreviewDesc.getText().toString(),
                        PictorialAdapter.this, pictorialObject.getNarrationPath());
//                PictorialAdapter.this.notifyDataSetChanged();
                holder.previewDescription.setText(holder.editPreviewDesc.getText().toString());
                holder.editPreviewDesc.setVisibility(View.GONE);
                holder.previewDescription.setVisibility(View.VISIBLE);
                holder.saveEdits.setImageResource(R.drawable.ic_pages);
                holder.previewDescription.setText(pictorialObjects.get(position).getPicDescription());
                if (fab != null) {
                    fab.setVisibility(View.VISIBLE);
                }
            }

        });


        holder.itemView.setOnClickListener(view -> {
            if (flag.equals("studio")) {
                if (!holder.itemView.isSelected()) {
                    holder.itemView.setSelected(true);
                    holder.carrierRel.setBackgroundColor(Color.BLUE);

                } else {
                    holder.itemView.setSelected(false);
                    holder.carrierRel.setBackgroundColor(Color.WHITE);
                }
            }
        });
        
        holder.photoDocItem.setOnClickListener(view -> {
            if (touchIV != null) {
                if (flag.equals("offline") || flag.equals("studio")) {
                    touchIV.setImageURI(Uri.parse(pictorialObject.getPicture()));
                    touchIV.setVisibility(View.VISIBLE);
                }else
                    if (flag.equals("online")){
                        touchIV.setImageResource(R.drawable.ic_hourglass);
                        touchIV.setVisibility(View.VISIBLE);
                        FilingSystem.Companion.downloadImage(touchIV, pictorialObject.getPicture());
                    }
            }
        });

        holder.previewDescription.setText(pictorialObject.getPicDescription());

        Glide.with(con).load(Uri.parse(pictorialObject.getPicture())).thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop()
                .placeholder(R.drawable.ic_hourglass).into(holder.photoDocItem);


//        Picasso.get().load(Uri.parse(pictorialObject.getPicture()))
//                .placeholder(R.drawable.ic_hourglass).fit().into(holder.photoDocItem);

//        Glide.with(con).load(Uri.parse(pictorialObjects.get(position).getPicture()))
//                .thumbnail(0.5f)
////                .transform(new CircleTransform(con))
//                .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(holder.photoDocItem);

    }

    private void narrationDialog(String title, String body, PictorialObject pictorialObject, int position, ImageButton narration, Chronometer narTimer) {

        final AlertDialog.Builder fileNameDialog = new AlertDialog.Builder(con);
        fileNameDialog.setTitle(title);
        fileNameDialog.setMessage(body);
        fileNameDialog.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        fileNameDialog.setPositiveButton("Yes", (dialog, which) -> {
            if (isRecording) {
                isRecording = false;
                stopRecording();
                narTimer.stop();
                narTimer.setVisibility(View.GONE);
                narration.setImageResource(R.drawable.ic_mic_off);
                PhotoDoc.Companion.editDesc(con, fileName, position, pictorialObject.getPicDescription(), PictorialAdapter.this, outPutFile);
                Toast.makeText(con, "Narration saved", Toast.LENGTH_SHORT).show();
                PictorialAdapter.this.notifyDataSetChanged();
            } else {
                if (checkAudioPermission(a)) {
                    isRecording = true;
                    startRecording();
                    narTimer.setBase(SystemClock.elapsedRealtime());
                    narTimer.start();
                    narration.setImageResource(R.drawable.ic_baseline_mic_24);
                    narTimer.setVisibility(View.VISIBLE);
                }
            }
        });


        fileNameDialog.setOnCancelListener(dialog -> dialog.dismiss());
        fileNameDialog.show();
    }


    @Override
    public int getItemCount() {
        return pictorialObjects.size();
    }

    @Override
    public void onItemMove(int fromPos, int toPos) {
        PictorialObject picObj = pictorialObjects.get(fromPos);
        pictorialObjects.remove(picObj);
        pictorialObjects.set(toPos, picObj);
        notifyItemMoved(fromPos, toPos);
    }

    @Override
    public void onItemSwipe(int pos) {
        pictorialObjects.remove(pos);
        notifyItemRemoved(pos);
    }

    public class PicAdapterVH extends RecyclerView.ViewHolder
            implements View.OnTouchListener, GestureDetector.OnGestureListener {
        ImageView photoDocItem;
        TextView previewDescription;
        EditText editPreviewDesc;
        ImageButton saveEdits, narration, playNarration;
        Button photoDocsOptions;
        Chronometer narTimer;
        RelativeLayout carrierRel;
        GestureDetector gestureDetector;
        TemplateView nativeTemplate;
        CardView templateViewCard;

        public PicAdapterVH(@NonNull View itemView) {
            super(itemView);

            photoDocItem = itemView.findViewById(R.id.preview);
            carrierRel = itemView.findViewById(R.id.carrierRel);
            playNarration = itemView.findViewById(R.id.playNarration);
            previewDescription = itemView.findViewById(R.id.prevDescription);
            narration = itemView.findViewById(R.id.narration);
            nativeTemplate = itemView.findViewById(R.id.nativeTemplate);
            editPreviewDesc = itemView.findViewById(R.id.editPreviewDescription);
            templateViewCard = itemView.findViewById(R.id.templateCard);
            saveEdits = itemView.findViewById(R.id.submitEdit);
            narTimer = itemView.findViewById(R.id.narrationChronometer);
            photoDocsOptions = itemView.findViewById(R.id.photoDocsOptions);
            gestureDetector = new GestureDetector(itemView.getContext(), this);

            itemView.setOnTouchListener(this);

        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            itemTouchHelper.startDrag(this);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
//            gestureDetector.onTouchEvent(event);
            return false;
        }
    }

    private boolean checkAudioPermission(Activity activity) {
        try {
            if (ContextCompat.checkSelfPermission(con, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(con, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(a, Manifest.permission.READ_EXTERNAL_STORAGE)
                        && ActivityCompat.shouldShowRequestPermissionRationale(a, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Snackbar.make(photoDoc.getViewRel()
                            , "Permission", Snackbar.LENGTH_INDEFINITE)
                            .setAction("ENABLE", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ActivityCompat.requestPermissions(a, new String[]{
                                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    }, 8);
                                }
                            }).show();
                } else {
                    ActivityCompat.requestPermissions(a, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, 8);
                }
            } else {
                return true;
            }
        } catch (Exception e) {
            Toast.makeText(con, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (ActivityCompat.checkSelfPermission(con, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, 8);
            return false;
        }
    }

    private void startRecording() {
        mediaRecorder = new MediaRecorder();
        SimpleDateFormat smf = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
        Date now = new Date();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextWrapper contextWrapper = new ContextWrapper(con);
            File audioBooks = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_AUDIOBOOKS);
            File narrations = new File(audioBooks, smf.format(now) + UUID.randomUUID().toString() + ".mp3");
            outPutFile = narrations.getPath();
        }else {
            outPutFile = FilingSystem.Companion.getDubAnthems().getAbsolutePath() + "/" + "yN_narration_"
                    + smf.format(now) + UUID.randomUUID().toString() + ".3gp";
        }
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
//        mediaRecorder.setAudioEncodingBitRate(16*44100);
//        mediaRecorder.setAudioSamplingRate(44100);
        mediaRecorder.setOutputFile(outPutFile);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            Toast.makeText(con, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        Toast.makeText(con, "Recording started", Toast.LENGTH_LONG).show();

    }

    private void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    private void playNarration(File narration, final Chronometer cr, PictorialObject pictorialObject) {
        if (isPlaying) {
            mediaPlayer.stop();
            isPlaying = false;
        }
        mediaPlayer = new MediaPlayer();

        AudioManager audioManager;
        AudioAttributes playbackAttributes;

        AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = focusChange -> {
            if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                try {

                    mediaPlayer.setDataSource(con, Uri.parse(pictorialObject.getNarrationPath()));
                    mediaPlayer.prepare();
                    mediaPlayer.start();


                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                mediaPlayer.release();
            }
        };

        audioManager = (AudioManager) con.getSystemService(Context.AUDIO_SERVICE);
        playbackAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        if (pictorialObject == null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                AudioFocusRequest focusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                        .setAudioAttributes(playbackAttributes)
                        .setAcceptsDelayedFocusGain(true)
                        .setOnAudioFocusChangeListener(audioFocusChangeListener)
                        .build();
                final int audioFocusRequest = audioManager.requestAudioFocus(focusRequest);

                if (audioFocusRequest == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    try {
                        mediaPlayer.setDataSource(narration.getAbsolutePath());
                        mediaPlayer.prepare();
                        mediaPlayer.start();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else
            {
                try {
                    mediaPlayer.setDataSource(narration.getAbsolutePath());
                    mediaPlayer.prepare();
                    mediaPlayer.start();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {



            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                AudioFocusRequest focusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                        .setAudioAttributes(playbackAttributes)
                        .setAcceptsDelayedFocusGain(true)
                        .setOnAudioFocusChangeListener(audioFocusChangeListener)
                        .build();
                final int audioFocusRequest = audioManager.requestAudioFocus(focusRequest);

                if (audioFocusRequest == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    try {
                        mediaPlayer.setDataSource(con, Uri.parse(pictorialObject.getNarrationPath()));
                        mediaPlayer.prepare();
                        mediaPlayer.start();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else
            {
                try {
                    mediaPlayer.setDataSource(con, Uri.parse(pictorialObject.getNarrationPath()));
                    mediaPlayer.prepare();
                    mediaPlayer.start();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


        isPlaying = true;
        mediaPlayer.setOnCompletionListener(mp -> {
            cr.stop();
            cr.setVisibility(View.GONE);
        });
    }

    private void stopNarration() {
        if (isPlaying) {
            mediaPlayer.stop();
            isPlaying = false;
            mediaPlayer = null;
        }
    }

    public void setTouchHelper(ItemTouchHelper itemTouchHelper) {
        this.itemTouchHelper = itemTouchHelper;
    }
}
