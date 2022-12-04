package com.example.b07.user;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Objects;

public class Account {
    public String name;
    public static Account instance;
    public boolean privileged;
    public static final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
    public static Welcome welcome;
    public static SendToast toast;

    public interface SendToast {
        void run(String message);
    }

    public interface Welcome {
        void run(String name);
    }

    private Account() {
    }

    public static Account getInstance() {
        if (instance == null) instance = new Account();
        return instance;
    }

    @NonNull
    @Override
    public String toString() {
        return "Account{name='" + name + "'" + ", privileged=" + privileged + '}';
    }

    //move to model
    public static void login(String name, String encrypted) {
        instance.name = name;
        DatabaseReference user = ref.child(name);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object u = snapshot.getValue();
                // check existence
                if (u == null) {
                    toast.run("User " + name + " does not exist!");
                    return;
                }
                // check password
                String hash = snapshot.child("passwd").getValue(String.class);
                if (!Objects.equals(encrypted, hash)) {
                    toast.run("Incorrect password!");
                    return;
                }

                // check privileged
                instance.privileged = Boolean.TRUE.equals(snapshot.child("privileged").getValue(Boolean.class));
                welcome.run(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * Sign up as student
     */
    public static void register(String name, String encrypted) {
        instance.name = name;
        instance.privileged = false;
        DatabaseReference user = ref.child(name);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    toast.run("User " + name + " already exists!");
                    return;
                }
                user.updateChildren(Map.of(
                    "passwd", encrypted, "privileged", false, "courses", Map.of()
                ));
                welcome.run(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
