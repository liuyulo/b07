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
    protected static User instance;
    public String name;
    public Set<Course> courses;
    protected DatabaseReference ref;

    public User(String name) {
        this.name = name;
        this.ref = FirebaseDatabase.getInstance().getReference().child("users").child(name);
        this.courses = new HashSet<>();
    }

    @NonNull
    @Override
    public String toString() {
        return "User{name='" + name + '\'' + ", courses=" + courses + '}';
    }


    public abstract boolean add(Course course);

    public abstract boolean remove(Course course);
}
