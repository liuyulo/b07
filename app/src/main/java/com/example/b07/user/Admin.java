package com.example.b07.user;

import androidx.annotation.NonNull;

import com.example.b07.adapter.CourseAdapter;
import com.example.b07.course.Course;
import com.example.b07.course.Session;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Spliterator;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Admin extends User {
    private static final String TAG = "Admin";
    public static Admin instance;

    private Admin() {
        adapter = new CourseAdapter(() -> courses, null, this::remove);
        ref = FirebaseDatabase.getInstance().getReference("courses");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Spliterator<DataSnapshot> iter = snapshot.getChildren().spliterator();
                // recursively add courses to cache
                courses = StreamSupport.stream(iter, false).map(
                    child -> Course.from(child.getKey())
                ).collect(Collectors.toCollection(TreeSet::new));
                if (adapter != null) adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public static Admin getInstance() {
        // if new account has been created/modified
        if (instance == null) instance = new Admin();
        return instance;
    }

    public boolean override(Course course) {
        // if prereqs are not in db
        if (!course.prereqs.isEmpty() && !courses.containsAll(course.prereqs)) return false;
        ref.child(course.code).updateChildren(Map.of(
            "prereqs", course.prereqs.stream().map(c -> c.code).collect(Collectors.toList()),
            "sessions", course.sessions.stream().map(Session::toString).collect(Collectors.toList()),
                "name", course.name
        ));
        return true;
    }

    /**
     * @return false if course has prereqs not in db ohr course already in db
     */
    @Override
    public boolean add(Course course) {
        if (courses.contains(course)) return false;
        return override(course);
    }

    public boolean update(Course course) {
        if (!courses.contains(course)) return false;
        return override(course);
    }

    /**
     * @return true if and only if course is not prereq of other course in database and is
     * successfully removed
     */
    @Override
    public boolean remove(Course course) {
        // course is prereq of other course in db => return false
        if (!courses.contains(course) || courses.stream().anyMatch(c -> c.prereqs.contains(course))) {
            return false;
        }
        ref.child(course.code).removeValue();
        return true;
    }
}