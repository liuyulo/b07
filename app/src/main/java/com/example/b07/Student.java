package com.example.b07;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Student extends User {
    private static final String TAG = "Student";
    public Set<Course> wants = new HashSet<>();
    private static final String TAKEN = "taken";
    private static final String WANTS = "wants";
    public static Semester semester = new Semester(2022, Session.FALL);

    private Student(String name) {
        super(name);
        ref.child(WANTS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // update course list for user
                Spliterator<DataSnapshot> iter = snapshot.getChildren().spliterator();
                // recursively add courses to cache
                Student.getInstance().wants = StreamSupport.stream(iter, false).map(
                    child -> Course.from(child.getValue(String.class))
                ).collect(Collectors.toSet());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        ref.child(TAKEN).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "Student taken changed");
                // update course list for user
                Spliterator<DataSnapshot> iter = snapshot.getChildren().spliterator();
                if (adapter != null) adapter.notifyDataSetChanged();
                // recursively add courses to cache
                Student.instance.courses = StreamSupport.stream(iter, false).map(
                    child -> Course.from(child.getValue(String.class))
                ).collect(Collectors.toSet());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public static Student getInstance() {
        // if new account has been created/modified
        if (instance == null || !Objects.equals(instance.name, Account.name)) {
            instance = new Student(Account.name);
        }
        return (Student) instance;
    }

    private List<String> codes(Set<Course> courses) {
        return courses.stream().map(c -> c.code).collect(Collectors.toList());
    }

    private void updateTaken(Set<Course> courses) {
        ref.updateChildren(Map.of(TAKEN, codes(courses)));
    }

    private void updateWants(Set<Course> courses) {
        ref.updateChildren(Map.of(WANTS, codes(courses)));
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

    /**
     * generate timeline
     */
    public Map<String, Set<Course>> timeline() {
        return Student.timeline(courses, wants, semester);
    }

    /**
     * add course to wish list
     */
    public boolean want(Course course) {
        if (wants.contains(course)) return false;
        updateWants(Stream.concat(wants.stream(), Stream.of(course)).collect(Collectors.toSet()));
        return true;
    }

    public boolean unwant(Course course) {
        // remove course from db
        if (!wants.contains(course)) return false;
        updateTaken(wants.stream().filter(c -> !c.equals(course)).collect(Collectors.toSet()));
        return true;
    }

    @Override
    public boolean add(Course course) {
        // add course to db
        if (courses.contains(course)) return false;
        updateTaken(Stream.concat(courses.stream(), Stream.of(course)).collect(Collectors.toSet()));
        return true;
    }

    @Override
    public boolean remove(Course course) {
        // remove course from db
        if (!courses.contains(course)) return false;
        updateTaken(courses.stream().filter(c -> !c.equals(course)).collect(Collectors.toSet()));
        return true;
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

