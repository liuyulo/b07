package com.example.b07;

import static com.example.b07.Account.sha256;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

class Admin extends User {
    private static final String TAG = "Admin";
    private static final DatabaseReference cref = FirebaseDatabase.getInstance().getReference("courses");

    private Admin(String name) {
        super(name);
    }

    @Override
    protected void listen() {
        cref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "update Admin courses");

                // update course list for user
                if (snapshot.exists()) {
                    Spliterator<DataSnapshot> iter = snapshot.getChildren().spliterator();
                    // recursively add courses to cache
                    User.instance.courses = StreamSupport.stream(iter, false).map(
                        child -> Course.from(child.getKey())
                    ).collect(Collectors.toSet());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "listen: update unsuccessful, error(s) happened");
            }
        });
    }

    public static Admin getInstance() {
        // if new account has been created/modified
        if (instance == null || !Objects.equals(instance.name, Account.name)) {
            instance = new Admin(Account.name);
        }
        return (Admin) instance;
    }


    private boolean isprereq(Course c, DataSnapshot ds) {
        DataSnapshot prereqs = ds.child("prereqs");
        Set<Course> pre = null;
        if (prereqs.exists()) {
            Spliterator<DataSnapshot> iter = ds.child("prereqs").getChildren().spliterator();
            // recursively add courses to cache
            pre = StreamSupport.stream(iter, false).map(
                child -> Course.from(child.getValue(String.class))
            ).collect(Collectors.toSet());
        }

        if (pre.isEmpty() || !pre.contains(c.code)) {
            return true;
        }

        return false;
    }

    /**
     * @param course
     * @return false if course has prereqs not in db ohr course already in db
     */
    @Override
    public boolean add(Course course) {
        if (courses.contains(course) || !courses.containsAll(course.prereqs)) return false;
        cref.child(course.code).updateChildren(Map.of(
            "prereqs", course.prereqs.stream().map(c -> c.code).collect(Collectors.toList()),
            "sessions", course.sessions.stream().map(Session::toString).collect(Collectors.toList())
        ));
        Log.d(TAG, "add " + course);
        return courses.add(course); // i.e. return true
    }

    /**
     * @param course
     * @return true if and only if course is not prereq of other course in database and is
     * successfully removed
     */
    @Override
    public boolean remove(Course course) {
        // course is prereq of other course in db => return false
        if (!courses.contains(course) || courses.stream().anyMatch(c -> course.prereqs.contains(c))) {
            return false;
        }
        Log.d(TAG, "remove " + course);
        cref.child(course.code).removeValue();
        return courses.remove(course);
    }
}