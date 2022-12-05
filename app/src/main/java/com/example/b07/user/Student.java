package com.example.b07.user;

import androidx.annotation.NonNull;

import com.example.b07.course.Course;
import com.example.b07.course.Semester;
import com.example.b07.course.Session;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class Student extends User {
    public static Semester semester = new Semester(2022, Session.FALL);
    public static CourseType current = CourseType.TAKEN;

    protected Student(String name) {
        ref = FirebaseDatabase.getInstance().getReference("users").child(name);
        ref.child(key()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Spliterator<DataSnapshot> iter = snapshot.getChildren().spliterator();
                courses = StreamSupport.stream(iter, false).map(
                    child -> child.getValue(String.class)
                ).map(Course::from).collect(Collectors.toCollection(TreeSet::new));
                if (adapter != null) adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * key under student that contains courses
     */
    protected abstract String key();

    protected void update() {
        ref.updateChildren(Map.of(key(), stream().map(c -> c.code).collect(Collectors.toList())));
    }

    @Override
    public boolean add(Course course) {
        // add course to db
        boolean b = courses.add(course);
        update();
        return b;
    }

    @Override
    public boolean remove(Course course) {
        // remove course from db
        boolean b = courses.remove(course);
        update();
        return b;
    }


    public static Map<String, Set<Course>> timeline(Set<Course> taken, Set<Course> want, Semester current) {
        // don't want to mutate the fields
        Set<Course> t = new HashSet<>(taken);
        Set<Course> w = new HashSet<>(want);

        // This set will record necessary courses to form the timeline which missing in Set w.
        Set<Course> need;
        do {
            need = w.stream().flatMap(course -> course.prereqs.stream().filter(
                prereq -> !(t.contains(prereq) || w.contains(prereq))
            )).collect(Collectors.toSet());
            w.addAll(need);
        } while (!need.isEmpty());
        // After the loop, we added all necessary courses into want.

        Map<String, Set<Course>> output = new LinkedHashMap<>();

        // This loop go through courses in want and add those which can be taken in current semester
        // Then go to next semester and repeat. Until want is empty.
        while (!w.isEmpty()) {
            Set<Course> todo = new HashSet<>();
            for (Course course : w) {
                if (t.containsAll(course.prereqs) && course.sessions.contains(current.session)) {
                    todo.add(course);
                }
            }
            output.put(String.valueOf(current), todo);
            w.removeAll(todo);
            t.addAll(todo);
            current = current.next();
        }
        return output;
    }


    public static void main(String[] args) {
        Course a08 = new Course("CSCA08", Session.FW, new HashSet<>());
        Course a67 = new Course("CSCA67", Session.FW, new HashSet<>());

        Course a48 = new Course("CSCA48", Session.WS, new HashSet<>(List.of(a08)));
        Course b07 = new Course("CSCB07", Session.FS, new HashSet<>(List.of(a48)));
        Course b09 = new Course("CSCB09", Session.WS, new HashSet<>(List.of(a48)));

        Course b36 = new Course("CSCB36", Session.FS, new HashSet<>(List.of(a48, a67)));
        Course b63 = new Course("CSCB63", Session.WS, new HashSet<>(List.of(b36)));

        Course c24 = new Course("CSCC24", Session.WS, new HashSet<>(List.of(b07, b09)));
        Course c63 = new Course("CSCC63", Session.FW, new HashSet<>(List.of(b63, b36)));

        System.out.println("Test taken nothing");
        Set<Course> taken = new HashSet<>();
        Set<Course> want = new HashSet<>(List.of(c24, a08, a48, b07, b36, a67));
        Map<String, Set<Course>> output = timeline(taken, want, new Semester(2022, Session.FALL));
        System.out.println("\tTaken: " + taken);
        System.out.println("\tTimeline: " + output);

        System.out.println("Test taken something");
        taken = new HashSet<>(List.of(a08, a48, a67, b36));
        want = new HashSet<>(List.of(b63, b09, b07, c24, c63));
        output = timeline(taken, want, new Semester(2022, Session.WINTER));
        System.out.println("\tTaken: " + taken);
        System.out.println("\tTimeline: " + output);

        System.out.println("Test taken nothing and only one course in want list");
        taken = new HashSet<>();
        want = new HashSet<>(List.of(c63));
        output = timeline(taken, want, new Semester(2022, Session.SUMMER));
        System.out.println("\tTaken: " + taken);
        System.out.println("\tTimeline: " + output);
    }
}

