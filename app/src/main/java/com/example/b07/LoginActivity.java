package com.example.b07;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

final class U {
    final String username;
    final String password;

    public U(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

public class LoginActivity extends AppCompatActivity {

    public static final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

    private U user(ActivityLoginBinding b) {
        String username = b.username.getText().toString();
        String password = b.password.getText().toString();
        return new U(username, password);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding b = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        // check username and password validity
        TextWatcher callback = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                U u = user(b);
                String name = checkUsername(u.username);
                String pass = checkPassword(u.password);
                boolean enabled = name == null && pass == null;
                if (name != null) b.username.setError(name);
                if (pass != null) b.password.setError(pass);
                b.login.setEnabled(enabled);
                b.register.setEnabled(enabled);
            }
        };
        b.username.addTextChangedListener(callback);
        b.password.addTextChangedListener(callback);
        b.password.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                U u = user(b);
                login(u.username, u.password);
            }
            return false;
        });

        b.asStudent.setOnClickListener(v -> login("student", "student"));
        b.asAdmin.setOnClickListener(v -> login("admin", "password"));
        b.login.setOnClickListener(v -> {
            U u = user(b);
            login(u.username, u.password);
        });
        b.register.setOnClickListener(v -> {
            U u = user(b);
            register(u.username, u.password);
        });
    }

    private String checkPassword(String password) {
        final int n = 8;
        if (password.length() <= n) {
            return "Password must be over " + n + " characters";
        }
        return null;
    }

    private String checkUsername(String username) {
        if (username.trim().length() == 0) {
            return "Username cannot be empty or whitespace";
        } else if (username.startsWith(" ")) {
            return "Username cannot start with whitespace";
        } else if (username.endsWith(" ")) {
            return "Username cannot end with whitespace";
        }
        return null;
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

    /**
     * welcome user to MainActivity
     *
     * @param name
     */
    public void welcome(String name) {
        Context context = getApplicationContext();
        Toast.makeText(context, "Welcome " + name + "!", Toast.LENGTH_SHORT).show();
        // go to main activity
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
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
                welcome(name);
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
    public void register(String name, String password) {
        Account.name = name;
        Account.privileged = false;
        DatabaseReference ref = LoginActivity.ref.child(name);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String dne = "User " + name + " already exists!";
                    Toast.makeText(getApplicationContext(), dne, Toast.LENGTH_LONG).show();
                    return;
                }
                ref.updateChildren(Map.of(
                    "passwd", sha256(password), "privileged", false, "courses", Map.of()
                ));
                welcome(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}