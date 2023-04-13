package com.midland.ynote.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.midland.ynote.Activities.SourceDocList;
import com.midland.ynote.Adapters.AllVidRecAdapter;
import com.midland.ynote.Objects.SourceDocObject;
import com.midland.ynote.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Lectures#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Lectures extends Fragment {

    private RecyclerView lecturesRV;
    public AllVidRecAdapter allVidAdapter;
    public Uri vidUri;
    public VideoView videoView;
    RelativeLayout relativeLayout;

    public void setVidUri(Uri vidUri) {
        this.vidUri = vidUri;
    }

    private ProgressBar progressGetDbVid;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Lectures() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OtherVideos.
     */
    // TODO: Rename and change types and number of parameters
    public static Lectures newInstance(String param1, String param2) {
        Lectures fragment = new Lectures();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        allVidAdapter = new AllVidRecAdapter(getContext(), getVideoFileObjects(), "offline", null);
        allVidAdapter.notifyDataSetChanged();
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.lectures_fragment, container, false);
        allVidAdapter.notifyDataSetChanged();

        lecturesRV = v.findViewById(R.id.lecturesRV);
        lecturesRV.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, true));
        relativeLayout = v.findViewById(R.id.lectureFragmentRel);
        lecturesRV.setAdapter(allVidAdapter);
//        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(lecturesRV);

        if (checkAndRequestPermission()) {
            if (getVideoFileObjects().size() == 0) {

            } else {

                allVidAdapter.notifyDataSetChanged();
            }

        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        allVidAdapter.notifyDataSetChanged();
    }


    //GETTING ALL VIDEOS FROM EXTERNAL STORAGE
    public ArrayList<SourceDocObject> getVideoFileObjects() {
        ArrayList<SourceDocObject> sourceDocObjects = new ArrayList<>();
        if (checkAndRequestPermission()) {
            File[] list =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).listFiles();

            if (list != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                for (File file : list) {
                    if (file.getName().endsWith("mp4")){
                        int fileSize = Integer.parseInt(String.valueOf(file.length() / 1024));
                        if (fileSize != 0) {
                            String newName = file.getName().replace("dubLecture", "mp4");
                            SourceDocObject sourceDocObject = new SourceDocObject(newName, Uri.fromFile(file), String.valueOf(fileSize),
                                    dateFormat.format(file.lastModified()), Uri.fromFile(file), file.lastModified());
                            sourceDocObjects.add(sourceDocObject);
                        }
                    }

                }
            }
        }

        return sourceDocObjects;
    }

    private boolean checkAndRequestPermission() {
        int readPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> neededPermission = new ArrayList<>();
        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            neededPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            neededPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!neededPermission.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), neededPermission.toArray(new String[neededPermission.size()]), Documents.REQUEST_MULTI_PERMISSION_ID);
            return false;
        }
        return true;
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT | ItemTouchHelper.ANIMATION_TYPE_SWIPE_CANCEL) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 7){
            startActivity(new Intent(getContext(), SourceDocList.class));

        }
    }
}