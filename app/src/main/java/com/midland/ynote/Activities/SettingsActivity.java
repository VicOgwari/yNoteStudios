package com.midland.ynote.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.midland.ynote.R;

public class SettingsActivity extends AppCompatActivity implements
        PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    private static final String TITLE_TAG = "settingsActivityTitle";
    private CheckBox sketchPad, frontCam,
            backCam, noteBook, showAll, showEnrolled,
            privateNar, publicNar;

    private Button saveSettings;
    private SharedPreferences settings = getSharedPreferences("System_settings", Context.MODE_PRIVATE);


    private String sketch, frontC, backC, noteB, show, nar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        saveSettings = findViewById(R.id.saveSettings);
        sketchPad = findViewById(R.id.sketchPad);
        frontCam = findViewById(R.id.frontFacing);
        backCam = findViewById(R.id.backFacing);
        noteBook = findViewById(R.id.noteBook);


        sketch = settings.getString("sketchPad", sketch);
        frontC = settings.getString("frontCam", frontC);
        backC = settings.getString("backCam", backC);
        noteB = settings.getString("noteBook", noteB);
        show = settings.getString("show", show);
        nar = settings.getString("narration", nar);

        sketchPad.setChecked(sketch.equals("ON"));
        frontCam.setChecked(frontC.equals("ON"));
        backCam.setChecked(backC.equals("ON"));
        noteBook.setChecked(noteB.equals("ON"));
        if (show.equals("ENROLLED")){
            showEnrolled.setChecked(true);
            showAll.setChecked(false);
        }else{
            showEnrolled.setChecked(false);
            showAll.setChecked(true);
        }
        if (nar.equals("PRIVATE")){
            privateNar.setChecked(true);
            publicNar.setChecked(false);
        }else{
            privateNar.setChecked(false);
            publicNar.setChecked(true);
        }

        sketchPad.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isChecked()){
               sketch = "ON";
            }else {
                sketch = "OFF";
            }
        });
        frontCam.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isChecked()){
                frontC = "ON";
            }else {
                frontC = "OFF";
            }
        });
        backCam.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isChecked()){
                backC = "ON";
            }else {
                backC = "OFF";
            }
        });
        noteBook.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isChecked()){
                noteB = "ON";
            }else {
                noteB = "OFF";
            }
        });

        showAll = findViewById(R.id.showAll);
        showEnrolled = findViewById(R.id.showEnrolled);

        showAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isChecked()){
                showEnrolled.setChecked(false);
                show = "ALL";
            }
        });
        showEnrolled.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isChecked()){
                showAll.setChecked(false);
                show = "ENROLLED";
            }
        });

        privateNar = findViewById(R.id.privateNar);
        publicNar = findViewById(R.id.publicNar);

        privateNar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isChecked()){
                publicNar.setChecked(false);
                nar = "PRIVATE";
            }
        });
        publicNar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isChecked()){
                privateNar.setChecked(false);
                nar = "PUBLIC";
            }
        });

        saveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("sketchPad", sketch);
                editor.putString("frontCam", frontC);
                editor.putString("backCam", backC);
                editor.putString("noteBook", noteB);
                editor.putString("show", show);
                editor.putString("narration", nar);
                editor.apply();
            }
        });


        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new HeaderFragment())
                    .commit();
        } else {
            setTitle(savedInstanceState.getCharSequence(TITLE_TAG));
        }
        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                            setTitle(R.string.title_activity_settings);
                        }
                    }
                });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save current activity title so we can set it again after a configuration change
        outState.putCharSequence(TITLE_TAG, getTitle());
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (getSupportFragmentManager().popBackStackImmediate()) {
            return true;
        }
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
        // Instantiate the new Fragment
        final Bundle args = pref.getExtras();
        final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
                getClassLoader(),
                pref.getFragment());
        fragment.setArguments(args);
        fragment.setTargetFragment(caller, 0);
        // Replace the existing Fragment with the new Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings, fragment)
                .addToBackStack(null)
                .commit();
        setTitle(pref.getTitle());
        return true;
    }

    public static class HeaderFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.header_preferences, rootKey);
        }
    }

    public static class MessagesFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.messages_preferences, rootKey);
        }
    }

    public static class SyncFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.sync_preferences, rootKey);
        }
    }
}