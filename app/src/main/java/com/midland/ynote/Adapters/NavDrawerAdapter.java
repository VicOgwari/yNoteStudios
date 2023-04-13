package com.midland.ynote.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.midland.ynote.Activities.UserProfile2;
import com.midland.ynote.R;

import java.util.ArrayList;

public class NavDrawerAdapter extends RecyclerView.Adapter<NavDrawerAdapter.NavDrawerVH> {

    private String flag;
    private ArrayList<ArrayList<String>> navigationDetails;
    private ArrayList<String> navDetail;
    private Context c;

    public NavDrawerAdapter(String flag, ArrayList<ArrayList<String>> navigationDetails, Context c) {
        this.flag = flag;
        this.navigationDetails = navigationDetails;
        this.c = c;
    }

    @NonNull
    @Override
    public NavDrawerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        if (flag.equals("profiles")){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        }else if (flag.equals("notifications")){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notify_item, parent, false);
        }
        return new NavDrawerVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NavDrawerVH holder, int position) {
        navDetail = navigationDetails.get(position);
        if (flag.equals("profiles")){
            Glide.with(c).load(navDetail.get(0)).thumbnail((float) 0.9).placeholder(R.mipmap.ic_launcher_round).into(holder.userImage);
            holder.userAlias.setText(navDetail.get(1));
            holder.institution.setText(navDetail.get(2));
            holder.students.setText(navDetail.get(3));
            holder.coaches.setText(navDetail.get(4));

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(c, UserProfile2.class);
                intent.putExtra("UserID", navDetail.get(5));
                c.startActivity(intent);
            });

        }else if (flag.equals("notifications")){
            holder.title.setText(navDetail.get(0));
            holder.subTitle.setText(navDetail.get(1));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(c, "Behave appropriately!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class NavDrawerVH extends RecyclerView.ViewHolder{
        TextView userAlias, institution, students, coaches;
        ImageView userImage;

        TextView title, subTitle;
        public NavDrawerVH(@NonNull View itemView) {
            super(itemView);
             userImage = itemView.findViewById(R.id.userDisplayImage);
             userAlias = itemView.findViewById(R.id.aliasName);
             institution = itemView.findViewById(R.id.fullName);
             students = itemView.findViewById(R.id.studCount);
             coaches = itemView.findViewById(R.id.coachesCount);

             title = itemView.findViewById(R.id.title);
             subTitle = itemView.findViewById(R.id.subTitle);

        }
    }
}
