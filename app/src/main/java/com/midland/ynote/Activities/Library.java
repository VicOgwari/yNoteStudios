package com.midland.ynote.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.midland.ynote.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.midland.ynote.Adapters.FragmentsSwitchAdapter;
import com.midland.ynote.MainActivity;

public class Library extends AppCompatActivity {

    private FragmentsSwitchAdapter adapter;
    private TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        Toolbar toolbar = findViewById(R.id.libToolbar);
        setSupportActionBar(toolbar);
        initTabsNStuff();
    }


    private void initTabsNStuff() {
        String flag;
        if (getIntent().getStringExtra("Flag") != null){
            flag = getIntent().getStringExtra("Flag");
        }
        tabs = findViewById(R.id.tabs);
        ImageView add = findViewById(R.id.add);
        ImageView home = findViewById(R.id.home);
//        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager()
//                , FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, tabs.getTabCount());
        final ViewPager viewPager = findViewById(R.id.view_pager);
//        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                tabs.getTabAt(position).select();
                super.onPageSelected(position);
            }
        });

        FloatingActionButton fab = findViewById(R.id.addANew);
        fab.setVisibility(View.GONE);

        home.bringToFront();
        add.bringToFront();

        home.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            Library.this.finish();
        });

        add.setOnClickListener(v -> {
            switch (tabs.getSelectedTabPosition()) {
                case 0:
                    if (ContextCompat.checkSelfPermission(Library.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(Library.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(Library.this, Manifest.permission.CAMERA)
                                && ActivityCompat.shouldShowRequestPermissionRationale(Library.this, Manifest.permission.RECORD_AUDIO)) {
                            Snackbar.make(findViewById(R.id.rootLayout1), "Permission", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("ENABLE", v1 -> ActivityCompat.requestPermissions(Library.this, new String[]{
                                            Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
                                    }, 7)).show();
                        } else {
                            ActivityCompat.requestPermissions(Library.this, new String[]{
                                    Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
                            }, 7);
                        }
                    } else {
                        Intent intent = new Intent(Library.this, PhotoDoc.class);
                        intent.putExtra("newDoc", "newDoc");
                        startActivity(intent);
                    }

                    break;

                case 1:
                    if (ContextCompat.checkSelfPermission(Library.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(Library.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(Library.this, Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getParent(), Manifest.permission.CAMERA)
                                && ActivityCompat.shouldShowRequestPermissionRationale(getParent(), Manifest.permission.RECORD_AUDIO)) {
                            Snackbar.make(findViewById(R.id.rootLayout1), "Permission", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("ENABLE", v1 -> ActivityCompat.requestPermissions(getParent(), new String[]{
                                            Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
                                    }, 7)).show();
                        } else {
                            ActivityCompat.requestPermissions(getParent(), new String[]{
                                    Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
                            }, 7);
                        }
                    } else {
                        startActivity(new Intent(Library.this, SourceDocList.class));
                    }

                    break;

                case 2:
                    Intent intent1 = new Intent(getApplicationContext(), Schools.class);
                    startActivity(intent1);
                    break;
            }
        });


        adapter = new FragmentsSwitchAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, tabs.getTabCount(), "Library");

        viewPager.setAdapter(adapter);

        if (getIntent().getStringExtra("bitmaps") != null){
            viewPager.setCurrentItem(1);
        }

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 7){
            Intent intent = new Intent(Library.this, PhotoDoc.class);
            intent.putExtra("newDoc", "newDoc");
            startActivity(intent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.disclaimer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.disclaimer:
                break;

            default:
                return false;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Library.this, MainActivity.class));
        this.finish();
    }


    //    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 7){
//            startActivity(new Intent(Library.this, SourceDocList.class));
//
//        }
//    }
}