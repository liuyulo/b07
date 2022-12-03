package com.example.b07.user;

import androidx.recyclerview.widget.RecyclerView;

import com.example.b07.course.Course;
import com.google.firebase.database.DatabaseReference;

import java.util.Set;
import java.util.TreeSet;

public abstract class User {
    public Set<Course> courses;
    /**
     * adapter that updates courses in view
     */
    public RecyclerView.Adapter<?> adapter;
    /**
     * ref pointing to list of courses
     */
    protected DatabaseReference ref;

    public User() {
        this.courses = new TreeSet<>();
    }

    public abstract boolean add(Course course);

    public abstract boolean remove(Course course);
}
