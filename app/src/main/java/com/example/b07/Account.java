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
    public static String name;
    public static boolean privileged;
    public static boolean exists;
    public static boolean isin;
    private static final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

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

    public static void login(String name, String password) {
        Account.name = name;
        DatabaseReference ref = Account.ref.child("admin").child(name);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object u = snapshot.getValue();
                // check existence
                if (u == null) return;
                Account.exists = true;
                // check password
                String hash = snapshot.child("passwd").getValue(String.class);
                if (!Objects.equals(sha256(password), hash)) return;

                Account.isin = true;
                // check privileged
                Account.privileged = Boolean.TRUE.equals(snapshot.child("privileged").getValue(Boolean.class));
                Log.i("Account", name + " logged in");
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
        Account.exists = Account.isin = true;
        Account.privileged = false;
        DatabaseReference ref = Account.ref.child(name);
        ref.updateChildren(Map.of(
            "passwd", sha256(password), "privileged", false, "courses", Map.of()
        ));
    }

    @NonNull
    @Override
    public String toString() {
        return "Account{name='" + name + "'" + ", privileged=" + privileged + '}';
    }

}
