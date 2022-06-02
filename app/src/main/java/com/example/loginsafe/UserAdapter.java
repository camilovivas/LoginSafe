package com.example.loginsafe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginsafe.model.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserView> {

    private ArrayList<User> users;


    public UserAdapter() {
        users = new ArrayList<>();
    }

    @NonNull
    @Override
    public UserView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.userrow, parent, false);
        UserView userView = new UserView(row);
        return userView;
    }

    @Override
    public void onBindViewHolder(@NonNull UserView holder, int position) {
        User user = users.get(position);
        holder.getName().setText(user.getUserName());
        holder.getClean().setOnClickListener(e->{
            FirebaseFirestore.getInstance().collection("users").document(user.getUserName()).update("password", "");
        });

        holder.getDelete().setOnClickListener(e->{
            FirebaseFirestore.getInstance().collection("users").document(user.getUserName()).delete();
        });

    }

    public void addUser(User user){
        users.add(user);
        notifyItemInserted(users.size());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
