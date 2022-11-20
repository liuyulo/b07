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
import java.util.Set;

public class Course {
    public String code;
    public Set<String> sessions;
    public Set<Course> prereqs;
    private DatabaseReference mDatabase;

    public Course(String code) {
        // Setting up the data fields
        this.code = code;
        this.sessions = new HashSet<String>();
        this.prereqs = new HashSet<Course>();
        mDatabase = FirebaseDatabase.getInstance().getReference("courses");

        // Getting the course's sessions from Firewall and updating the field
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("sessions", "updating sessions");
                for(DataSnapshot child: snapshot.child("sessions").getChildren()) {
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
                for(DataSnapshot child: snapshot.child("prereqs").getChildren()) {
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
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Course other = (Course) obj;
        return (this.code == other.code);
    }

    @Override
    public int hashCode() {
        int hashVal = 7;
		for (int i = 0; i < this.code.length(); i++) {
		    hashVal = hashVal*31 + this.code.charAt(i);
		}
        return hashVal;
    }

    @Override
    public String toString() {
        return "Course: code - " + this.code;
    }
}
