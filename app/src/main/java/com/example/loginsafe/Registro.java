package com.example.loginsafe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.loginsafe.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

public class Registro extends AppCompatActivity {

    private TextInputEditText username, password, repassword;
    private Button registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        username = findViewById(R.id.usernameTx);
        password = findViewById(R.id.passwordTx);
        repassword = findViewById(R.id.repasswordTx);
        registro = findViewById(R.id.registroBtn);

        registro.setOnClickListener(e->{
            String name = username.getText().toString();
            String pass = password.getText().toString();
            String repass = repassword.getText().toString();
            if(pass.equals(repass)){
                //TODO poner el hash
                User user = new User(name, pass, 0L);
                FirebaseFirestore.getInstance().collection("users").document(name).set(user);
            }
            else{
                Toast.makeText(this,"las contrasñeas no son iguales",Toast.LENGTH_LONG).show();
            }
        });

    }
}