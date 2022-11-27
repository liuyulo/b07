package com.example.b07;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Student extends User {
    private static final String TAG = "Student";

    private Student(String name) {
        super(name);
        ref.child("courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "Student courses changed");
                // update course list for user
                Spliterator<DataSnapshot> iter = snapshot.getChildren().spliterator();
                // recursively add courses to cache
                Student.instance.courses = StreamSupport.stream(iter, false).map(
                    child -> Course.from(child.getValue(String.class))
                ).collect(Collectors.toSet());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "listen: update unsuccessful, error(s) happened");
            }
        });
    }

    public static Student getInstance() {
        // if new account has been created/modified
        if (instance == null || !Objects.equals(instance.name, Account.name)) {
            instance = new Student(Account.name);
        }
        return (Student) instance;
    }

    protected void update(Set<Course> courses) {
        ref.updateChildren(Map.of("courses", courses.stream().map(c -> c.code).collect(Collectors.toList())));
    }

    @Override
    public boolean add(Course course) {
        // add course to db
        if (courses.contains(course)) return false;
        update(Stream.concat(courses.stream(), Stream.of(course)).collect(Collectors.toSet()));
        return true;
    }

    @Override
    public boolean remove(Course course) {
        // remove course from db
        if (!courses.contains(course)) return false;
        update(courses.stream().filter(c -> !c.equals(course)).collect(Collectors.toSet()));
        return true;
    }
}

