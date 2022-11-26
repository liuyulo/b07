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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Student extends User {
    private static final String TAG = "User";
    private static Student instance;
    private static final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

    private Student(String name) {
        super();
        this.name = name;
        this.privileged = false;
        super.listen();
    }

    public static Student getInstance() {
        if (instance == null) instance = new Student("");
        return instance;
    }

    /**
     * @param name
     * @param password
     * @return Student typed instance
     */
    public static Student signup(String name, String password) {
        instance = new Student(name);
        instance.exists = instance.isin = true;
        instance.privileged = false;
        DatabaseReference ref = Student.ref.child(name);
        ref.updateChildren(Map.of(
            "passwd", sha256(password), "privileged", false, "courses", Map.of()
        ));
        return instance;
    }

    /**
     * add course c to "users/student/name/course"
     * Precondition: c is in 'courses'
     *
     * @param c
     * @return true if and only course does not exists and succesfully added
     */
    @Override
    public boolean add(Course c) {
        //init isin to check if course already exists
        Student.instance.isin = false;
        DatabaseReference cRef = ref.child(name).child("course");
        ArrayList<String> courses = new ArrayList<String>();
        cRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "add: trying to add to the course into the database");

                // count the number of courses in the course node and insert the new course at the end
                int cnt = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.child(String.valueOf(cnt)).getChildren().toString().equals(c.code)) {
                        Log.d(TAG, "add: course already exists, updating unsuccessful");
                        Student.instance.isin = true;
                    }
                    cnt++;
                }
                if (!Student.instance.isin) {
                    Log.d(TAG, "add: updating successful");
                    cRef.child(String.valueOf(cnt)).setValue(c.code);
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "add: error(s) happened, failed to add the course");
            }
        });

        Log.d(TAG, "add: quitting the method");
        if (Student.instance.isin) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * remove course from "users/student/course"
     *
     * @param c
     * @return true if and only if course c is in the list of courses and is successfully removed
     */
    @Override
    public boolean remove(Course c) {
        // remove course from `users[name].courses`
        Student.instance.isin = false;
        ArrayList<String> courses = new ArrayList<String>();
        final DatabaseReference cRef = ref.child(name).child("course");

        cRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "remove: trying to remove from the database");
                int cnt = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String code = ds.getValue().toString();
                    // if found the course then skip it, dont add it into the list of courses
                    if (code.equals(c.code)) {
                        Log.d(TAG, "remove: remove successful");
                        Student.instance.isin = true;
                        continue;
                    }
                    courses.add(code);

                    // delete the view course to enable the rearrangement of indices of courses later
                    cRef.child(String.valueOf(cnt)).removeValue();
                }
                if (!Student.instance.isin) {
                    Log.d(TAG, "remove: remove unsuccessful, course does not exists");
                }

                // update the course list
                Log.d(TAG, "remove: updating the course list");
                for (int i = 0; i < courses.size(); i++) {
                    cRef.child(String.valueOf(i)).setValue(courses.get(i));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "remove: Errors happened");
            }
        });


        Log.d(TAG, "remove: quitting the method");
        if (Student.instance.isin) {
            return true;
        } else {
            return false;
        }
    }
}

