package com.example.b07;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Student extends User {
    private static final String TAG = "Student";

    private Student(String name) {
        super(name);
    }

    public static Student getInstance() {
        // if new account has been created/modified
        if (instance == null || !Objects.equals(instance.name, Account.name)) {
            instance = new Student(Account.name);
        }
        return (Student) instance;
    }

    @Override
    protected void listen() {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "listen: starting to update user data");

                // update course list for user
                DataSnapshot courses = snapshot.child("course");
                if (courses.exists()) {
                    Log.d(TAG, "listen: updating User.courses");
                    Spliterator<DataSnapshot> iter = courses.getChildren().spliterator();
                    // recursively add courses to cache
                    User.instance.courses = StreamSupport.stream(iter, false).map(
                        child -> Course.from(child.getValue(String.class))
                    ).collect(Collectors.toSet());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "listen: update unsuccessful, error(s) happened");
            }
        });
    }

    protected void update() {
        ref.updateChildren(Map.of("courses", courses.stream().map(c -> c.code).collect(Collectors.toList())));
    }

    @Override
    public boolean add(Course course) {
        boolean success = courses.add(course);
        if (success) update();
        return success;
    }

    @Override
    public boolean remove(Course course) {
        boolean success = courses.remove(course);
        if (success) update();
        return success;

    }
}

