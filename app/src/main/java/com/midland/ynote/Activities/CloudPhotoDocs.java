package com.midland.ynote.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.midland.ynote.Adapters.FragmentsSwitchAdapter;
import com.midland.ynote.Fragments.PhotoDocs;
import com.midland.ynote.Fragments.PhotoDocsComments;
import com.midland.ynote.MainActivity;
import com.midland.ynote.R;
import com.midland.ynote.Utilities.AdMob;

import java.util.ArrayList;

public class CloudPhotoDocs extends AppCompatActivity {

    private FragmentsSwitchAdapter adapter;
    private TabLayout tabs;
    private ArrayList<String> narrations;
    private ArrayList<String> descriptions;
    private ArrayList<String> cloudPictures;
    private String fileName;
    String publisherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_photo_docs);
        initTabsNStuff();
        AdMob.Companion.runRewardAd(getApplicationContext(), this, "");

//        OkHttpClient client = new OkHttpClient().newBuilder().build();
//        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(mediaType, {
//                "ShortCode": 174379,
//                "ResponseType": "Completed",
//                "ConfirmationURL": "https://mydomain.com/confirmation",
//                "ValidationURL":"https://mydomain.com/validation"
//        })
//
//        Request request = new Request.Builder()
//                .url("https://sandbox.safaricom.co.ke/mpesa/c2b/v1/registerurl")
//                .method("POST", body)
//                .addHeader("Content-Type", "application/json")
//                .addHeader("Authorization", "Bearer 0JrPoCISlBhDadAOeiXrApqwuN6v")
//                .build();
//        try {
//            Response response = client.newCall(request).execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getStringExtra("notification") != null) {
            fileName = intent.getStringExtra("notification").split("_-_")[6];
            publisherId = intent.getStringExtra("notification").split("_-_")[5];

            tabs = findViewById(R.id.tabs);
            final ViewPager viewPager = findViewById(R.id.view_pager);
            viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    tabs.getTabAt(position).select();
                    super.onPageSelected(position);
                }
            });
            adapter = new FragmentsSwitchAdapter(getSupportFragmentManager(),
                    FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, tabs.getTabCount(), CloudPhotoDocs.this, fileName, "Pictorials",
                    narrations, descriptions, cloudPictures, publisherId);

            viewPager.setAdapter(adapter);
        }

    }

    @Override
    public void onBackPressed() {
        if (PhotoDocs.Companion.getTouchIV() != null && PhotoDocs.Companion.getTouchIV().getVisibility() == View.VISIBLE) {
            PhotoDocs.Companion.getTouchIV().setVisibility(View.GONE);
        } else if (PhotoDocsComments.Companion.getRatingRel().getVisibility() == View.VISIBLE) {
            PhotoDocsComments.Companion.getRatingRel().setVisibility(View.GONE);
        } else if (PhotoDocsComments.Companion.getImageRel().getVisibility() == View.VISIBLE) {
            PhotoDocsComments.Companion.getImageRel().setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    private void initTabsNStuff() {
        narrations = new ArrayList<>();
        descriptions = new ArrayList<>();
        cloudPictures = new ArrayList<>();

        Intent intent = getIntent();

        Toolbar toolbar = findViewById(R.id.libToolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setTitle(fileName);
        }

        if (intent.getStringArrayListExtra("pictures") != null) {
            fileName = intent.getStringExtra("fileName");
            publisherId = intent.getStringExtra("userID");
            cloudPictures = intent.getStringArrayListExtra("pictures");
            narrations = intent.getStringArrayListExtra("narrations");
            descriptions = intent.getStringArrayListExtra("descriptions");
        } else if (intent.getStringExtra("notification") != null) {
            fileName = intent.getStringExtra("notification").split("_-_")[6];
            publisherId = intent.getStringExtra("notification").split("_-_")[5];

        }
        tabs = findViewById(R.id.tabs);
        ImageView add = findViewById(R.id.add);
        ImageView home = findViewById(R.id.home);

        final ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tabs.getTabAt(position).select();
                super.onPageSelected(position);
            }
        });


        home.bringToFront();
        add.bringToFront();

        home.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            CloudPhotoDocs.this.finish();
        });


        adapter = new FragmentsSwitchAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, tabs.getTabCount(), CloudPhotoDocs.this, fileName, "Pictorials",
                narrations, descriptions, cloudPictures, publisherId);

        viewPager.setAdapter(adapter);

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
}