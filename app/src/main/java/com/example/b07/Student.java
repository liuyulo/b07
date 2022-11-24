package com.example.b07;

import static com.example.b07.Account.sha256;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class Student extends User{
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

    @Override
    public boolean add(Course c){
        // add course to `users[name].courses`
        // assume c is in `courses'

        return false;
    }

    @Override
    public boolean remove(Course c){
        // remove course from `users[name].courses`
        return false;
    }
}

