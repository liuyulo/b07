package com.example.b07;

import android.util.Log;

import androidx.annotation.NonNull;

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

public class Account {
    private static Account instance;
    public String name;
    public boolean privileged;
    public boolean exists;
    public boolean isin;
    private static final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

    private Account(String name) {
        this.name = name;
    }

    private Account(String name, boolean privileged) {
        this.name = name;
        this.privileged = privileged;
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
     * Please login/signup before getInstance
     *
     * @return
     */
    public static Account getInstance() {
        // only happens when getInstance called before login/signup
        if (instance == null) instance = new Account("", false);
        return instance;
    }

    public static Account login(String name, String password) {
        Account.instance = new Account(name);
        DatabaseReference ref = Account.ref.child("admin").child(name);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object u = snapshot.getValue();
                // check existence
                if (u == null) return;
                Account.instance.exists = true;
                // check password
                String hash = snapshot.child("passwd").getValue(String.class);
                if (!Objects.equals(sha256(password), hash)) return;

                Account.instance.isin = true;
                // check privileged
                Account.instance.privileged = Boolean.TRUE.equals(snapshot.child("privileged").getValue(Boolean.class));
                Log.i("Account", name + " logged in");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Account", "login cancelled");
            }
        });
        return instance;
    }

    /**
     * Sign up as student
     *
     * @param name
     * @param password
     * @return
     */
    public static Account signup(String name, String password) {
        instance = new Account(name);
        instance.exists = instance.isin = true;
        instance.privileged = false;
        DatabaseReference ref = Account.ref.child(name);
        ref.updateChildren(Map.of(
            "passwd", sha256(password), "privileged", false, "courses", Map.of()
        ));
        return instance;
    }

    @NonNull
    @Override
    public String toString() {
        return "Account{name='" + name + "'" + ", privileged=" + privileged + '}';
    }

}
