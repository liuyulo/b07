package com.example.b07;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Course {
    public String code;
    public Set<Session> sessions;
    public Set<Course> prereqs;
//    private static final Map<String, Course> cache = new HashMap<>();

    public Course(String code, Set<Session> sessions, Set<Course> prereqs) {
        this.code = code;
        this.sessions = sessions;
        this.prereqs = prereqs;
    }

    public static Course from(String code) {
        code = code.toLowerCase(Locale.ROOT);
//        if (cache.containsKey(code)) return cache.get(code);
        final Course course = new Course(code, new HashSet<>(), new HashSet<>());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("courses").child(code);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot sessions = snapshot.child("sessions");
                if (sessions.exists()) {
                    Spliterator<DataSnapshot> iter = sessions.getChildren().spliterator();
                    course.sessions = StreamSupport.stream(iter, false).map(
                        child -> Session.from(child.getValue(String.class))
                    ).collect(Collectors.toSet());
                }
                DataSnapshot prereqs = snapshot.child("prereqs");
                if (prereqs.exists()) {
                    Spliterator<DataSnapshot> iter = snapshot.child("prereqs").getChildren().spliterator();
                    course.prereqs = StreamSupport.stream(iter, false).map(
                        child -> Course.from(child.getValue(String.class))
                    ).collect(Collectors.toSet());
                }
                Log.i("Course", String.valueOf(course));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Course", course.code, error.toException());
            }
        });
        Log.i("Course", String.valueOf(course));
        return course;
//        return cache.put(code, course);
    }

    // Overriding Equals() by comparing the two object's code field
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(code, ((Course) o).code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @NonNull
    @Override
    public String toString() {
        return "Course{" +
            "code='" + code + '\'' +
            ", sessions=" + sessions +
            ", prereqs=" + prereqs.stream().map(c -> c.code).collect(Collectors.toSet()) +
            '}';
    }
}
