package com.example.loginsafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button admin, usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        admin = findViewById(R.id.adminBtn);
        usuario = findViewById(R.id.userBtn);
        admin.setOnClickListener(this);
        usuario.setOnClickListener(this);
    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.userBtn:
                Intent intent = new Intent(this,Login.class);
                String type = "usuario";
                intent.putExtra("type", type);
                startActivity(intent);
                break;
            case R.id.adminBtn:
                Intent intent2 = new Intent(this,Login.class);
                String type2 = "admin";
                intent2.putExtra("type", type2);
                startActivity(intent2);
                break;
        }
    }
}