package com.example.b07.course;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Course implements Comparable<Course> {
    public String code;
    public String name;
    public Set<Session> sessions;
    public Set<Course> prereqs;
    public static Map<String, Course> cache = new HashMap<>();
    protected static RecyclerView.Adapter<?> adapter;

    public Course(String code, Set<Session> sessions, Set<Course> prereqs) {
        this.code = code;
        this.name = "";
        this.sessions = sessions;
        this.prereqs = prereqs;
    }

    /**
     * Get course from the database (or cache)
     *
     * @param code course code (case insensitive)
     * @return Course
     */
    public static Course from(String code) {
        // firebase contains code in lowercase
        code = code.toLowerCase(Locale.ROOT);
        // if cache hit
        if (cache.containsKey(code)) return cache.get(code);

        final Course course = new Course(code, new HashSet<>(), new HashSet<>());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("courses").child(code);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Spliterator<DataSnapshot> sessions = snapshot.child("sessions").getChildren().spliterator();
                course.sessions = StreamSupport.stream(sessions, false).map(
                    child -> Session.from(child.getValue(String.class))
                ).collect(Collectors.toSet());

                Spliterator<DataSnapshot> prereqs = snapshot.child("prereqs").getChildren().spliterator();
                course.prereqs = StreamSupport.stream(prereqs, false).map(
                    child -> Course.from(child.getValue(String.class))
                ).collect(Collectors.toSet());

                course.name = snapshot.child("name").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Course", course.code, error.toException());
            }
        });
        cache.put(code, course);
        return course;
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


    @Override
    public int compareTo(Course course) {
        return this.code.compareTo(course.code);
    }
}
