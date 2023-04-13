package com.midland.ynote.Activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.midland.ynote.Adapters.UsersAdapter;
import com.midland.ynote.Objects.User;
import com.midland.ynote.R;

import java.util.ArrayList;

public class SearchUsers extends AppCompatActivity {

    private RecyclerView usersRV;
    private UsersAdapter usersAdapter;
    private ArrayList<User> users;
    private EditText searchUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);

        usersRV = findViewById(R.id.searchResultsRV);
        usersRV.setHasFixedSize(true);
        usersRV.setLayoutManager(new LinearLayoutManager(SearchUsers.this, RecyclerView.VERTICAL, false));
        users = new ArrayList<>();
        usersAdapter = new UsersAdapter(SearchUsers.this, users, "search");
        searchUser = findViewById(R.id.searchEditText);

        readUsers();
        searchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void searchUsers(final String s){
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username").startAt(s).endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot d : snapshot.getChildren()){
                    User user = d.getValue(User.class);
                    users.add(user);
                }
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readUsers(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (searchUser.getText().toString().trim().equals("")){
                    users.clear();
                    for (DataSnapshot d : snapshot.getChildren()){
                        User user = d.getValue(User.class);
                        users.add(user);
                    }
                    usersAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}