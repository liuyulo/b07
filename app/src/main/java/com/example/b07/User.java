package com.example.b07;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class User {
    private static final String TAG = "User";
    private static User instance;
    public String name;
    public Set<Course> courses;

    public boolean isin;
    public boolean exists;
    public boolean privileged;

    public User() {
        courses = new HashSet<>();
        this.listen();
    }

    /**
     * Listen for realtime update for user file
     */
    protected void listen() {
        // check if user exists
        if (User.instance != null) {
            // assumed user with the username exists in our database
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(name);

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d(TAG, "listen: starting to update user data");

                    // update course list for user
                    DataSnapshot courses = snapshot.child("course");
                    if (courses.exists()) {
                        Log.d(TAG, "listen: updating User.courses");
                        Spliterator<DataSnapshot> iter = courses.getChildren().spliterator();
                        // init the local course list
                        User.instance.courses.clear();

                        // recursively add courses to cache
                        User.instance.courses = StreamSupport.stream(iter, false).map(
                                child -> Course.from(child.getValue(String.class))
                        ).collect(Collectors.toSet());
                    }

                    // hash function is non-invertible so unable to track the change of change of passwd

                    // update privileged field for user
                    DataSnapshot privileged = snapshot.child("privilege");
                    if (privileged.exists()) {
                        Log.d(TAG, "listen: updating User.privileged");
                        User.instance.privileged = privileged.getValue(Boolean.class);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, "listen: update unsuccessful, error(s) happened");
                }
            });
        }

        return;
    }

    public abstract boolean add(Course c);
    public abstract boolean remove(Course c);
}
