package com.example.loginsafe;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserView extends RecyclerView.ViewHolder {

    private TextView name;
    private Button delete, clean;

    public UserView(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.nameRow);
        delete = itemView.findViewById(R.id.deleteBtnRow);
        clean = itemView.findViewById(R.id.cleanBtnRow);
    }

    public TextView getName() {
        return name;
    }

    public Button getDelete() {
        return delete;
    }

    public Button getClean() {
        return clean;
    }

}
