package com.example.loginsafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginsafe.util.KeyManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Calendar;

public class User extends AppCompatActivity {
    private TextView fecha;
    private com.example.loginsafe.model.User user;
    private Button logoutBtn, changePass;
    private TextInputEditText  pass, repass;
    private KeyManager myKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        fecha = findViewById(R.id.fechaTx);
        myKey = new KeyManager();
        logoutBtn = findViewById(R.id.logoutBtn);
        changePass = findViewById(R.id.changeBtn);
        pass = findViewById(R.id.passTx);
        repass = findViewById(R.id.repassTx);
        logoutBtn.setOnClickListener(this :: logout);
        changePass.setOnClickListener(this :: change);

        user = (com.example.loginsafe.model.User) getIntent().getExtras().getSerializable("user");
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(user.getLastCheckInTime());
        fecha.setText(time.getTime().toString());
    }

    public void logout(View view){
        Calendar time = Calendar.getInstance();
        long milis = time.getTimeInMillis();
        FirebaseFirestore.getInstance().collection("users").document(user.getUserName()).update("lastCheckInTime", milis);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public  void change(View view){
            String pass1 = pass.getText().toString();
            String pass2 = repass.getText().toString();
            if(pass1.equals(pass2)){
                try {
                    String hash = myKey.generatePassword(pass1);
                    FirebaseFirestore.getInstance().collection("users").document(user.getUserName()).update("password", hash).addOnCompleteListener(task -> {
                        Toast.makeText(this, "contraseña cambiada", Toast.LENGTH_LONG).show();
                });
                } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(this, "las contraseñas no son iguales",Toast.LENGTH_LONG).show();
            }
    }
}