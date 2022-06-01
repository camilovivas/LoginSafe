package com.example.loginsafe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

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

    public void login() {
        String username = user.getText().toString();
        String pass = password.getText().toString();
        if (type.equals("usuario")) {
            Query query = FirebaseFirestore.getInstance().collection("users").whereEqualTo("userName",username);
            query.get().addOnCompleteListener(
                    task->{
                        if(task.getResult().size() != 0){//si encuentra el usuario
                            //comparo contraseñas y abro la pestaña de usuarios
                        }
                        else{
                            Toast.makeText(this,"usuario incorrecto",Toast.LENGTH_LONG).show();
                        }
                    }
            );
        }
        else{
            Query query = FirebaseFirestore.getInstance().collection("admins").whereEqualTo("user",username);
            query.get().addOnCompleteListener(
                    task->{
                        if(task.getResult().size() != 0){//si encuentra el usuario
                            //comparo contraseñas y abro la pestaña de usuarios
                        }
                        else{
                            Toast.makeText(this,"usuario incorrecto",Toast.LENGTH_LONG).show();
                        }
                    }
            );
        }

        }

    }
}