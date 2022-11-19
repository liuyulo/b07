package com.example.b07;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Course {
    String code;
    Set<Session> sessions;
    Set<String> prereqs;

    public Course(String code, Set<Session> sessions, Set<String> prereqs) {
        this.code = code;
        this.sessions = sessions;
        this.prereqs = prereqs;
    }

    @Override
    public String toString() {
        return code;
    }
}

class Semester {
    int year;
    Session session;

    public Semester(int year, Session session) {
        this.year = year;
        this.session = session;
    }

    public Semester next() {
        return switch(session){
            case WINTER -> new Semester(year, Session.SUMMER);
            case SUMMER -> new Semester(year, Session.FALL);
            case FALL -> new Semester(year + 1, Session.WINTER);
        };
    }

    @Override
    public String toString() {
        return year + " " + session;
    }
}

enum Session {
    WINTER("Winter"), SUMMER("Summer"), FALL("Fall");
    public static final Set<Session> FW = new HashSet<>(Arrays.asList(Session.FALL, Session.WINTER));
    public static final Set<Session> FS = new HashSet<>(Arrays.asList(Session.FALL, Session.SUMMER));
    public static final Set<Session> WS = new HashSet<>(Arrays.asList(Session.WINTER, Session.SUMMER));
    private final String name;

    Session(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

public class Driver {
    /**
     * Generate timeline
     *
     * @param t       courses taken
     * @param w       courses want to take ( t ⋂ w = ∅)
     * @param current the semester to start with
     * @return generated timeline
     */
    public static Map<String, Set<Course>> generate(Set<String> t, Set<Course> w, Semester current) {
        // don't want to mutate the input
        Set<String> taken = new HashSet<>(t);
        Set<Course> want = new HashSet<>(w);

        int scount = 0;
        Map<String, Set<Course>> output = new LinkedHashMap<>();
        // todo while(!want.isEmpty())
        // i.e. loop until all courses are added, not just for 4 semesters
        while (scount < 4) {
            Set<Course> toTake = new HashSet<>();
            for (Course course : want) {
                if (taken.containsAll(course.prereqs) && course.sessions.contains(current.session)) {
                    toTake.add(course);
                }
            }
            output.put(String.valueOf(current), toTake);
            want.removeAll(toTake);
            for (Course course : toTake) {
                taken.add(course.code);
            }
            current = current.next();
            scount++;
        }
        return output;
    }

    public static void main(String[] args) {
        Course a08 = new Course("CSCA08", Session.FW, new HashSet<>());
        Course a67 = new Course("CSCA67", Session.FW, new HashSet<>());

        Course a48 = new Course("CSCA48", Session.WS, new HashSet<>(List.of(a08.code)));
        Course b07 = new Course("CSCB07", Session.FS, new HashSet<>(List.of(a48.code)));
        Course b09 = new Course("CSCB09", Session.WS, new HashSet<>(List.of(a48.code)));

        Course b36 = new Course("CSCB36", Session.FS, new HashSet<>(List.of(a48.code, a67.code)));
        Course b63 = new Course("CSCB63", Session.WS, new HashSet<>(List.of(b36.code)));

        Course c24 = new Course("CSCC24", Session.WS, new HashSet<>(List.of(b07.code, b09.code)));
        Course c63 = new Course("CSCC63", Session.FW, new HashSet<>(List.of(b63.code, b36.code)));

        System.out.println("Test taken nothing");
        Set<String> taken = new HashSet<>();
        Set<Course> want = new HashSet<>(List.of(c24, a08, a48, b07, b36, a67));
        Map<String, Set<Course>> output = generate(taken, want, new Semester(2022, Session.FALL));
        System.out.println("\tTaken: " + taken);
        System.out.println("\tTimeline: " + output);

        System.out.println("Test taken something");
        taken = Stream.of(a08, a48, a67, b36).map(c -> c.code).collect(Collectors.toSet());
        want = new HashSet<>(List.of(b63, b09, b07, c24, c63));
        output = generate(taken, want, new Semester(2022, Session.WINTER));
        System.out.println("\tTaken: " + taken);
        System.out.println("\tTimeline: " + output);

        /*
        todo test taken missing
        with taken = {}, want = { c63 }, current = 2022 Summer
        should generate one of the following 8 possibilities
            22f a08 a67    or     22f a08
            23w a48               22w a67 a48

            23s b36        or     23s (nothing)
            23f (nothing)         23f b36

            23w b63        or     23w (nothing)
            23s (nothing)         23s b63

            24f c63
         */
    }
}
