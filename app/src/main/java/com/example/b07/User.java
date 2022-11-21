package com.example.b07;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Objects;

public final class User {
    private static final User instance = new User();
    public String name;
    public boolean privileged;
    public boolean exists;
    public boolean isin;

    private User() {
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

    public static User getInstance() {
        return instance;
    }

    public static User login(String name, String password) {
        User.instance.name = name;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(name);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object user = snapshot.getValue();
                // check existence
                if (user == null) {
                    User.instance.exists = false;
                    return;
                }
                User.instance.exists = true;

                // check password
                String hash = snapshot.child("passwd").getValue(String.class);
                if (!Objects.equals(sha256(password), hash)) {
                    User.instance.isin = false;
                    return;
                }
                User.instance.isin = true;
                User.instance.privileged = Boolean.TRUE.equals(snapshot.child("privileged").getValue(Boolean.class));
                Log.i("User", name + " logged in");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("User", "login cancelled");
            }
        });
        return instance;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{name='" + name + "'" + ", privileged=" + privileged + '}';
    }

    // similar to getManager
    public static User signup(String username, String password, boolean privileged) throws NoSuchAlgorithmException {
        /*
        1. encrypt password to sha256
        2. add new user to database (privileged is always false)
        3. return the created user
         */
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        boolean isUserExist = user.isUserExists(username);
//
//        if (isUserExist) {
//            return null;
//        }
//
//        MessageDigest digest = MessageDigest.getInstance("SHA-256");
//        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

//        UserReference.

        return null;
    }
}
