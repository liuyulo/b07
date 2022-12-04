package com.example.b07.user;

import androidx.recyclerview.widget.RecyclerView;

import com.example.b07.adapter.CourseAdapter;
import com.example.b07.course.Course;
import com.google.firebase.database.DatabaseReference;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

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
        this.adapter = new CourseAdapter(() -> courses, "x", this::remove);
    }

    public abstract boolean add(Course course);

    public abstract boolean remove(Course course);

    public Stream<Course> stream() {
        return courses.stream();
    }

    public boolean contains(Course course) {
        return courses.contains(course);
    }
}
