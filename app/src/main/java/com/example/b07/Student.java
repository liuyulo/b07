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

public class Student extends User{
    private static final String TAG = "User";
    private static Student instance;
    private static final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/student");

    private Student(String name) {
        super();
        this.name = name;
        this.privileged = false;
    }

    public static Student getInstance(){
        if(instance == null) instance = new Student("");
        return instance;
    }

    /**
     *
     * @param name
     * @param password
     * @return Student typed instance
     */
    public static Student signup(String name, String password){
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
     * add course c into the current database
     * @param c
     * @return true if and only course does not exists and succesfully added
     */
    @Override
    public boolean add(Course c){
        // add course to `users[name].courses`
        // assume c is in `courses'

        //init isin to check if course already exists
        Student.instance.isin = false;
        DatabaseReference cRef = ref.child(name).child("course");
        ArrayList<String> courses= new ArrayList<String>();
        cRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "add: adding the course into the database");

                // count the number of courses in the course node and insert the new course at the end
                int cnt = 0;
                for (DataSnapshot ds: snapshot.getChildren()) {
                    if (ds.child(String.valueOf(cnt)).getChildren().toString().equals(c.code)) {
                        Student.instance.isin = true;
                    }
                    cnt++;
                }
                if (!Student.instance.isin) {
                    cRef.child(String.valueOf(cnt)).setValue(c.code);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "add: failed to add course");
            }
        });
        if (Student.instance.isin) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean remove(Course c){
        // remove course from `users[name].courses`


        return false;
    }
}

