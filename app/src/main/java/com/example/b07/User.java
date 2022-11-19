package com.example.b07;

import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.b07.databinding.FragmentSecondBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.ECField;
import java.util.ArrayList;

public final class User extends AppCompatActivity {
    // todo: should be singleton
    private static User user;
    private DatabaseReference mDatabase;

    public String username;
    private String password;
    public String [] courses;
    public boolean privileged;

    private User(String username, String password, String [] courses, boolean privileged){
        this.username = username;
        this.password = password;
        this.courses = courses;
        this.privileged = privileged;
    }

    boolean isUserExists(String username) {
        DatabaseReference UserReference = FirebaseDatabase.getInstance().getReference().child("users");

        return
    }


    public static User login(String username, String password) throws NoSuchAlgorithmException {
        DatabaseReference UserReference = FirebaseDatabase.getInstance().getReference().child("users");
        boolean isUserExist = user.isUserExists(username);

        if (isUserExist) {
            String desiredPassword = UserReference.child(username + password).toString();

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            if (desiredPassword.equals(hash.toString())) {

                user = new User(username, password, );
            }
        }

        return null;
    }

    // similar to getManager
    public static User signup(String username, String password, boolean privileged) throws NoSuchAlgorithmException {
        /*
        1. encrypt password to sha256
        2. add new user to database (privileged is always false)
        3. return the created user
         */
        DatabaseReference UserReference = FirebaseDatabase.getInstance().getReference().child("users");
        boolean isUserExist = user.isUserExists(username);

        if (isUserExist) {
            return null;
        }

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

        UserReference.

        return user;
    }
}
