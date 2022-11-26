package com.example.b07;

import static com.example.b07.Account.sha256;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

class Admin extends User{
    private static final String TAG = "AdminClass";
    private static Admin instance;
    private static final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

    private Admin(String name) {
        super();
        this.name = name;
        this.privileged = true;
        super.listen();
    }

    public static Admin getInstance(){
        if(instance == null) instance = new Admin("");
        return instance;
    }

    public static Admin signup(String name, String password){
        instance = new Admin(name);
        instance.exists = instance.isin = true;
        instance.privileged = false;
        DatabaseReference ref = Admin.ref.child(name);
        ref.updateChildren(Map.of(
                "passwd", sha256(password), "privileged", false, "courses", Map.of()
        ));
        return instance;
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

    @Override
    public boolean add(Course c){
        /*
        add course to `courses`
        c has prereq that is not in db => return false
         */

        final DatabaseReference cRef = FirebaseDatabase.getInstance().getReference("course");

        // init isin
        Admin.instance.isin = true;

        cRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "add: check if added course has prerequisite that is not in database");
                for (Course prereq: c.prereqs) {
                    // if added course has prerequisite that is not in database
                    if (!snapshot.hasChild(prereq.code)) {
                        Admin.instance.isin = false;
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "add: errors happened");
            }
        });

        if (Admin.instance.isin) {
            cRef.updateChildren(Map.of(
                    "prereqs", c.prereqs, "sessions", c.sessions
            ));
            return true;
        }
        else {
            return false;
        }
    }

    /**
     *
     * @param c
     * @return true if and only if course is not prereq of other course in database and is
     *                  successfully removed
     */
    @Override
    public boolean remove(Course c){
        /*
        remove from `courses`
        c is prereq of other course in db => return false
         */

        // init isin
        Admin.instance.isin = false;
        final DatabaseReference cRef = FirebaseDatabase.getInstance().getReference("course");

        // check if course is a prerequisite of other course
        cRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    if (isprereq(c, ds)) {
                        Admin.instance.isin = true;
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Course", "Admin.remove(): errors happened", error.toException());
            }
        });

        if (Admin.instance.isin) {
            return false;
        }
        else {
            cRef.child(c.code).removeValue();
            return true;
        }

    }
}