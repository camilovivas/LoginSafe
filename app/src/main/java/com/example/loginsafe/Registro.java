package com.example.loginsafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;


import com.example.loginsafe.model.User;
import com.example.loginsafe.util.KeyManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;



import java.security.SecureRandom;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import java.math.BigInteger;


import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Registro extends AppCompatActivity {

    private TextInputEditText username, password, repassword;
    private Button registro;
    private KeyManager myKey;

    private static final int saltBytes = 24;
    private static final int hashBytes = 24;
    private static final int pbkdf2Iterations = 3000;

    private static final int iterationIndex = 0;
    private static final int saltIndex = 1;
    private static final int pbkdf2Index = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        username = findViewById(R.id.usernameTx);
        password = findViewById(R.id.passwordTx);
        repassword = findViewById(R.id.repasswordTx);
        registro = findViewById(R.id.registroBtn);

        myKey = new KeyManager();
        registro.setOnClickListener(e -> {
                    String name = username.getText().toString();
                    String pass = password.getText().toString();
                    String repass = repassword.getText().toString();
                    if (pass.equals(repass)) {//si no son iguales las contraseñas
                        String passwordHash = null;
                        try {
                            passwordHash = myKey.generatePassword(pass);
                        } catch (NoSuchAlgorithmException | InvalidKeySpecException noSuchAlgorithmException) {
                            noSuchAlgorithmException.printStackTrace();
                        }
                        User user = new User(name, passwordHash, 0L);
                        Query query = FirebaseFirestore.getInstance().collection("users");
                        query.get().addOnCompleteListener(
                                task -> {
                                    if (task.getResult().isEmpty()) {// si la collectios esta vacia
                                        registrarUsuario(user);
                                    } else {
                                        Query query1 = FirebaseFirestore.getInstance().collection("users").whereEqualTo("userName", name);
                                        query1.get().addOnCompleteListener(
                                                task2 -> {
                                                    if (task2.getResult().size() == 0) {//si el usuario no existe
                                                        registrarUsuario(user);
                                                    } else {
                                                        Toast.makeText(this, "ese usuario ya existe", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                        );
                                    }

                                }
                        );
                    } else {
                        Toast.makeText(this, "las contrasñeas no son iguales", Toast.LENGTH_LONG).show();
                    }
                }
        );

    }


    public String generatePassword(String pass){
        return createHash(pass.toCharArray());


    public void registrarUsuario(User user) {
        FirebaseFirestore.getInstance().collection("users").document(user.getUserName()).set(user);
        Toast.makeText(this, "usuario registrado", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    private String createHash(char[] password) {
        // Generating random salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[saltBytes];
        random.nextBytes(salt);

        // Hashing the password
        byte[] hash = pbkdf2(password, salt, pbkdf2Iterations, hashBytes);
        // format  for return iterations:salt:hash
        return pbkdf2Iterations + ":" + toHex(salt) + ":" +  toHex(hash);
    }

    private  byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)

        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        return skf.generateSecret(spec).getEncoded();
    }

}