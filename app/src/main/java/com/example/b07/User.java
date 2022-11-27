package com.example.b07;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashSet;
import java.util.Set;

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
