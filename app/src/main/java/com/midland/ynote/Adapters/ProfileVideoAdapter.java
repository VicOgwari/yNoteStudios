package com.midland.ynote.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.midland.ynote.R;

import java.util.ArrayList;

public class ProfileVideoAdapter extends RecyclerView.Adapter<ProfileVideoAdapter.ProfVidVH> {

    private Context c;
    private ArrayList<String> allVideos;

    public ProfileVideoAdapter(Context c, ArrayList<String> allVideos) {
        this.c = c;
        this.allVideos = allVideos;
    }

    @NonNull
    @Override
    public ProfVidVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.vid_object, parent, false);
        return new ProfVidVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfVidVH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return allVideos.size();
    }

    public static class ProfVidVH extends RecyclerView.ViewHolder{
        public ProfVidVH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
