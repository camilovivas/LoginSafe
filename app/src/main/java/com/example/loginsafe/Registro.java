package com.example.loginsafe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.loginsafe.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

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
            User user = new User(name, pass, 0L);//TODO esa pass se teine que cambiar por la hash
            if(pass.equals(repass)){//si no son iguales las contraseñas
                Query query = FirebaseFirestore.getInstance().collection("users");
                if(query.get().getResult().isEmpty()){// si la collectios esta vacia
                    FirebaseFirestore.getInstance().collection("users").document(name).set(user);
                }
                else{
                    Query query1 = FirebaseFirestore.getInstance().collection("users").whereEqualTo("userName", name);
                    query1.get().addOnCompleteListener(
                            task ->{
                                if(task.getResult().size() == 0){//si el usuario no existe
                                    FirebaseFirestore.getInstance().collection("users").document(name).set(user);
                                }
                                else{
                                    Toast.makeText(this,"ese usuario ya existe", Toast.LENGTH_LONG).show();
                                }
                            }
                    );
                }
            }
            else{
                Toast.makeText(this,"las contrasñeas no son iguales",Toast.LENGTH_LONG).show();
            }
        });

    }

    public String generatePassword(String pass){
        //TODO
        return null;
    }
}