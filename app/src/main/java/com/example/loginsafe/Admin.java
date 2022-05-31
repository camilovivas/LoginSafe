package com.example.loginsafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }
}