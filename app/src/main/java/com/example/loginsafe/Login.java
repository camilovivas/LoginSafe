package com.example.loginsafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.loginsafe.model.Admin;
import com.example.loginsafe.model.User;
import com.example.loginsafe.util.KeyManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Login extends AppCompatActivity {
    //cuando se lanza esta, se le pasa el parametro si es user o admin

    private TextInputEditText user, password;
    private Button loginBtn;
    private String type;
    private KeyManager myKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        type = getIntent().getExtras().getString("type");
        user = findViewById(R.id.userTx);
        password = findViewById(R.id.passTx);
        loginBtn = findViewById(R.id.loginBtn);
        myKey = new KeyManager();
        loginBtn.setOnClickListener(e -> {
            login();
        });
    }


    public void login() {
        String username = user.getText().toString();
        String pass = password.getText().toString();
        if (type.equals("usuario")) {
            loginUsuario(username, pass);
        } else {
            loginAdmin(username, pass);
        }

    }

    public void loginAdmin(String username, String pass) {
        Query query = FirebaseFirestore.getInstance().collection("admins").whereEqualTo("user", username);
        query.get().addOnCompleteListener(
                task -> {
                    if (task.getResult().size() != 0) {//si encuentra el usuario
                        Admin existingAdmin = null;
                        for (DocumentSnapshot ds : task.getResult()) {
                            existingAdmin = ds.toObject(Admin.class);
                        }
                        try {
                            boolean areEquals = myKey.validate(existingAdmin.getPassword(), pass);
                            //comparo contraseñas y abro la pestaña de admin
                            if (areEquals) {
                                Intent intent = new Intent(this, com.example.loginsafe.Admin.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(this, "contraeña incorrecta", Toast.LENGTH_LONG).show();
                            }
                        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(this, "usuario incorrecto", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void loginUsuario(String username, String pass) {
        Query query = FirebaseFirestore.getInstance().collection("users").whereEqualTo("userName", username);
        query.get().addOnCompleteListener(
                task -> {
                    if (task.getResult().size() != 0) {//si encuentra el usuario
                        User existingUser = null;
                        for (DocumentSnapshot ds : task.getResult()) {
                            existingUser = ds.toObject(com.example.loginsafe.model.User.class);
                        }
                        try {
                            boolean areEquals = myKey.validate(existingUser.getPassword(), pass);
                            //comparo contraseñas y abro la pestaña de usuarios
                            if (areEquals) {
                                Intent intent = new Intent(this, com.example.loginsafe.User.class);
                                intent.putExtra("user", existingUser);
                                startActivity(intent);
                            } else {
                                Toast.makeText(this, "contraeña incorrecta", Toast.LENGTH_LONG).show();
                            }
                        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(this, "usuario incorrecto", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

}


