package com.example.b07;

import static com.example.b07.User.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText inputUserName, inputPassword;
    private Button btnLogin;
    private TextView textRegister;

    protected void onCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        inputUserName = findViewById(R.id.login_userName);
        inputPassword = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.login_btn);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = inputUserName.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                if (userName.isEmpty()) {
                    inputUserName.setError("Email cannot be empty");
                    return;
                }
                if (password.isEmpty()) {
                    inputPassword.setError("Password cannot be empty");
                    return;
                }
                try {
                    login(userName, password);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}