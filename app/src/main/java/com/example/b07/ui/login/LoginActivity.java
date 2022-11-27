package com.example.b07.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.b07.Account;
import com.example.b07.MainActivity;
import com.example.b07.R;
import com.example.b07.databinding.ActivityLoginBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    public static final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
            .get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final Button registerButton = binding.register;

        loginViewModel.getLoginFormState().observe(this, state -> {
            if (state == null) {
                return;
            }
            boolean valid = state.isDataValid();
            loginButton.setEnabled(valid);
            registerButton.setEnabled(valid);
            if (state.getUsernameError() != null) {
                usernameEditText.setError(getString(state.getUsernameError()));
            }
            if (state.getPasswordError() != null) {
                passwordEditText.setError(getString(state.getPasswordError()));
            }
        });

        loginViewModel.getResult().observe(this, res -> {
            if (res == null) {
                return;
            }
            if (res.getError() != null) {
                showLoginFailed(res.getError());
            }
            if (res.getSuccess() != null) {
                updateUiWithUser(res.getSuccess());
            }
            setResult(Activity.RESULT_OK);

            //Complete and destroy login activity once successful
            finish();
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString());
            }
            return false;
        });

        binding.asStudent.setOnClickListener(v -> login("student", "student"));
        binding.asAdmin.setOnClickListener(v -> login("admin", "password"));
        loginButton.setOnClickListener(v -> {

            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            login(username, password);
            Log.d("Login", username + " " + password);
//            loginViewModel.login(username, password);
        });
    }

    /**
     * Encrypt to sha256
     *
     * @param message
     * @return
     */
    public static String sha256(String message) {
        // My IDE: Unhandled exception: NoSuchAlgorithmException
        // Me: trust me bro
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(message.getBytes(StandardCharsets.UTF_8));
            return String.format("%064x", new BigInteger(1, bytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.err.println("yo wdym you dont know what sha256 is");
        }
        return "";
    }

    public void login(String name, String password) {
        Account.name = name;
        DatabaseReference ref = LoginActivity.ref.child(name);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object u = snapshot.getValue();
                Context context = getApplicationContext();
                // check existence
                if (u == null) {
                    String dne = "User " + name + " does not exist!";
                    Toast.makeText(context, dne, Toast.LENGTH_LONG).show();
                    return;
                }
                // check password
                String hash = snapshot.child("passwd").getValue(String.class);
                if (!Objects.equals(sha256(password), hash)) {
                    String incorrect = "Incorrect password!";
                    Toast.makeText(context, incorrect, Toast.LENGTH_LONG).show();
                    return;
                }

                // check privileged
                Account.privileged = Boolean.TRUE.equals(snapshot.child("privileged").getValue(Boolean.class));
                Toast.makeText(context, "Welcome " + name + "!", Toast.LENGTH_SHORT).show();
                // go to main activity
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Account", "login cancelled");
            }
        });
    }

    /**
     * Sign up as student
     *
     * @param name
     * @param password
     * @return
     */
    public static void signup(String name, String password) {
        Account.name = name;
        Account.privileged = false;
        DatabaseReference ref = LoginActivity.ref.child(name);
        ref.updateChildren(Map.of(
            "passwd", sha256(password), "privileged", false, "courses", Map.of()
        ));
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}