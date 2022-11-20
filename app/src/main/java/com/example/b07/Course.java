package com.example.b07;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Course {
    public String code;
    public Set<String> sessions;
    public Set<Course> prereqs;

    public Course(String code) {
        // Setting up the data fields
        this.code = code;
        this.sessions = new HashSet<>();
        this.prereqs = new HashSet<>();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("courses");

        // Getting the course's sessions from Firewall and updating the field
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("sessions", "updating sessions");
                for (DataSnapshot child : snapshot.child("sessions").getChildren()) {
                    String s = child.getValue(String.class);
                    sessions.add(s);
                    Log.i("sessions", s);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("firebase", "no", error.toException());
            }
        });

        // Getting the course's prereqs from Firewall and updating the field
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("prereqs", "updating prereqs");
                for (DataSnapshot child : snapshot.child("prereqs").getChildren()) {
                    Course c = child.getValue(Course.class);
                    prereqs.add(c);
                    Log.i("prereqs", c.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("firebase", "no", error.toException());
            }
        });
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
