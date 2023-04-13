package com.midland.ynote.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.midland.ynote.Activities.UserProfile2;
import com.midland.ynote.Objects.User;
import com.midland.ynote.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserVH> {

    private Context context;
    private ArrayList<User> users;
    private String flag;
    FirebaseUser firebaseUser;

    public UsersAdapter(Context context, ArrayList<User> users, String flag) {
        this.context = context;
        this.users = users;
        this.flag = flag;
    }

    @NonNull
    @Override
    public UserVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserVH(LayoutInflater.from(context).inflate(R.layout.user_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final UserVH holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final User user = users.get(holder.getAbsoluteAdapterPosition());

        if (flag.equals("search")){
            holder.follow_btn.setVisibility(View.VISIBLE);
            holder.alias.setText(user.getAliasName());
            holder.fullName.setText(user.getFullName());
            holder.idHolder.setText(user.getId());
            Glide.with(context).load(user.getImageUrl()).into(holder.image_profile);

            if (user.getId().equals(firebaseUser.getUid())){
                holder.follow_btn.setVisibility(View.GONE);
            }
            isFollowing(user.getId(), holder.follow_btn);

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
//                editor.putString("profileID", user.getId());
//                editor.apply();
//
//                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag,
//                        new ProfileFragment()).commit();
//            }
//        });


            holder.follow_btn.setOnClickListener(v -> {
                if (holder.follow_btn.getText().toString().equals("Add to library")){
                    FirebaseDatabase.getInstance().getReference()
                            .child("Follow").child(firebaseUser.getUid()).child("Coaches")
                            .child(user.getId()).setValue(true);

                    FirebaseDatabase.getInstance().getReference()
                            .child("Follow").child(firebaseUser.getUid()).child("Students")
                            .child(user.getId()).setValue(true);

                }else {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Follow").child(firebaseUser.getUid()).child("Coaches")
                            .child(user.getId()).removeValue();

                    FirebaseDatabase.getInstance().getReference()
                            .child("Follow").child(firebaseUser.getUid()).child("Students")
                            .child(user.getId()).removeValue();
                }
            });

        }else if (flag.equals("follow")){
            holder.alias.setText(user.getFullName());
            holder.fullName.setText(user.getInstitution());
            holder.coachesCount.setText(String.format("%s Coaches", user.getCoaches()));
            holder.studCount.setText(String.format("%s Students", user.getStudents()));
            holder.idHolder.setText(user.getId());
            Glide.with(context).load(user.getProfileUrl()).thumbnail((float) 0.9).into(holder.image_profile);
            holder.follow_btn.setVisibility(View.GONE);


        }

        holder.tapBtn.bringToFront();
        holder.tapBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserProfile2.class);
            intent.putExtra("userID", holder.idHolder.getText().toString());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UserVH extends RecyclerView.ViewHolder{
        ImageView image_profile;
        TextView alias, fullName, coachesCount, studCount, idHolder;
        Button follow_btn, tapBtn;

        public UserVH(@NonNull View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.imageProfile);
            alias = itemView.findViewById(R.id.aliasName);
            idHolder = itemView.findViewById(R.id.idHolder);
            coachesCount = itemView.findViewById(R.id.coachesCount);
            studCount = itemView.findViewById(R.id.studCount);
            fullName = itemView.findViewById(R.id.fullName);
            follow_btn = itemView.findViewById(R.id.btnFollow);
            tapBtn = itemView.findViewById(R.id.tapBtn);
        }
    }

    private void isFollowing(final String userID, final Button button){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("Coaches");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(userID).exists()){
                    button.setText("Remove from library");
                }else {
                    button.setText("Add to library");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
