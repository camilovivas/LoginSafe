package com.example.loginsafe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginsafe.model.User;

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
        holder.getName().setText(user);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
