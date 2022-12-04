package com.example.b07.user;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Objects;

public class Account {
    public static String name;
    public static boolean privileged;
    public static Callback onNotExist;
    public static Welcome welcome;
    public static SendToast toast;

    interface SendToast {
        void run(String message);
    }

    interface Welcome{
        void welcome(String name);
    }

    interface Callback{
        void run();
    }

    @NonNull
    @Override
    public String toString() {
        return "Account{name='" + name + "'" + ", privileged=" + privileged + '}';
    }

    //move to model
    public static void login(String name, String password) {
        Account.name = name;
        DatabaseReference ref = LoginFragment.ref.child(name);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object u = snapshot.getValue();
                // check existence
                if (u == null) {
                    String dne = "User " + name + " does not exist!";
                    toast.run(dne);
                    return;
                }
                // check password
                String hash = snapshot.child("passwd").getValue(String.class);
                if (!Objects.equals(LoginPresenter.sha256(password), hash)) {
                    String incorrect = "Incorrect password!";
                    toast.run(incorrect);
                    return;
                }

                // check privileged
                Account.privileged = Boolean.TRUE.equals(snapshot.child("privileged").getValue(Boolean.class));
                welcome.welcome(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Account", "login cancelled");
            }
        });
    }
    //move to model
    /**
     * Sign up as student
     */
    public static void register(String name, String password) {
        Account.name = name;
        Account.privileged = false;
        DatabaseReference ref = LoginFragment.ref.child(name);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String dne = "User " + name + " already exists!";
                    toast.run(dne);
                    return;
                }
                ref.updateChildren(Map.of(
                        "passwd", LoginPresenter.sha256(password), "privileged", false, "courses", Map.of()
                ));
                welcome.welcome(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
