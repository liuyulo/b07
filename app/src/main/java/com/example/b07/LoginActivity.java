package com.example.b07;

import static com.example.b07.User.login;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

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

        btnLogin.setOnClickListener(v -> {
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
            login(userName, password);
        });
    }
}