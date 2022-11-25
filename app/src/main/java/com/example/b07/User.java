package com.example.b07;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashSet;
import java.util.Set;

public abstract class User {
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

    protected void listen() {

    }

    public abstract boolean add(Course c);
    public abstract boolean remove(Course c);
}
