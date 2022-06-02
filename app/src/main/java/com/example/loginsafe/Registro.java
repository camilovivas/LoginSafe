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

    public static final String pbkdf2Algorithm = "PBKDF2WithHmacSHA1";

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

    public void registrarUsuario(User user) {
        FirebaseFirestore.getInstance().collection("users").document(user.getUserName()).set(user);
        Toast.makeText(this, "usuario registrado", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }


    public String generatePassword(String pass)
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        return createHash(pass.toCharArray());
    }



    private String createHash(char[] password)
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        // Generating random salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[saltBytes];
        random.nextBytes(salt);

        // Hashing the password
        byte[] hash = pbkdf2(password, salt, pbkdf2Iterations, hashBytes);
        // format  for return iterations:salt:hash
        return pbkdf2Iterations + ":" + toHex(salt) + ":" +  toHex(hash);
    }

    public  boolean validatePassword(String password, String theHash)
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        return validatePassword(password.toCharArray(), theHash);
    }

    public  boolean validatePassword(char[] password, String theHash)
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        // separating the hash into its parameters
        String[] parameters = theHash.split(":");
        int iterations = Integer.parseInt(parameters[iterationIndex]);
        byte[] salt = fromHex(parameters[saltIndex]);
        byte[] hash = fromHex(parameters[pbkdf2Index]);
        // comparing hash of password provided, using the same salt,
        // iterations and hash length
        byte[] testHash = pbkdf2(password, salt, iterations, hash.length);
        // The password is correct if both hashes match.
        return slowEquals(hash, testHash);
    }

    private boolean slowEquals(byte[] a, byte[] b)
    {
        int comparison = a.length ^ b.length;
        for(int i = 0; i < a.length && i < b.length; i++)
            comparison |= a[i] ^ b[i];
        return comparison == 0;
    }

    private  byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory theSKF = SecretKeyFactory.getInstance(pbkdf2Algorithm);
        return theSKF.generateSecret(spec).getEncoded();
    }

    private byte[] fromHex(String hex)
    {
        byte[] binary = new byte[hex.length() / 2];
        for(int i = 0; i < binary.length; i++)
        {
            binary[i] = (byte)Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
        }
        return binary;
    }


    private String toHex(byte[] array)
    {
        BigInteger bigI = new BigInteger(1, array);
        String hex = bigI.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
            return String.format("%0" + paddingLength + "d", 0) + hex;
        else
            return hex;
    }








}