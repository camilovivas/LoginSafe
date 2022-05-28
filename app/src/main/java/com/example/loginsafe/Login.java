package com.example.loginsafe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

public class Login extends AppCompatActivity {
    //cuando se lanza esta, se le pasa el parametro si es user o admin

    private TextInputEditText user, password;
    private Button login;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        type = getIntent().getExtras().getString("type");
        user = findViewById(R.id.userTx);
        password = findViewById(R.id.passTx);
        login = findViewById(R.id.loginBtn);
    }
}