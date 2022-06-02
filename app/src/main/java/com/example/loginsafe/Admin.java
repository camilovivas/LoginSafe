package com.example.loginsafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.loginsafe.model.User;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Admin extends AppCompatActivity {
    private RecyclerView recyclerView;

    private LinearLayoutManager layoutManager;
    private UserAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        recyclerView = findViewById(R.id.recycler);
        layoutManager = new LinearLayoutManager(this);
        adapter = new UserAdapter();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        getUsers();
    }

    public void getUsers() {
        FirebaseFirestore.getInstance().collection("users").get().addOnCompleteListener(task -> {
            for (DocumentSnapshot ds : task.getResult()) {
                adapter.addUser(ds.toObject(User.class));
            }
        });
    }
}