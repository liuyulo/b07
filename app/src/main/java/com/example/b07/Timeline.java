package com.example.b07;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Timeline {
    /**
     * Generate timeline
     *
     * @param t       courses taken
     * @param w       courses want to take (t ⋂ w = ∅)
     * @param current the semester to start with
     * @return generated timeline
     */
    public static Map<String, Set<Course>> generate(Set<Course> t, Set<Course> w, Semester current) {
        // don't want to mutate the input
        Set<Course> taken = new HashSet<>(t);
        Set<Course> want = new HashSet<>(w);

        // This set will record necessary courses to form the timeline which missing in Set w.
        Set<Course> need;
        do {
            need = want.stream().flatMap(course -> course.prereqs.stream().filter(
                prereq -> !(taken.contains(prereq) || want.contains(prereq))
            )).collect(Collectors.toSet());
            want.addAll(need);
        } while (!need.isEmpty());
        // After the loop, we added all necessary courses into want.

        Map<String, Set<Course>> output = new LinkedHashMap<>();

        // This loop go through courses in want and add those which can be taken in current semester
        // Then go to next semester and repeat. Until want is empty.
        while (!want.isEmpty()) {
            Set<Course> toTake = new HashSet<>();
            for (Course course : want) {
                if (taken.containsAll(course.prereqs) && course.sessions.contains(current.session)) {
                    toTake.add(course);
                }
            }
            output.put(String.valueOf(current), toTake);
            want.removeAll(toTake);
            taken.addAll(toTake);
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
        Map<String, Set<Course>> output = generate(taken, want, new Semester(2022, Session.FALL));
        System.out.println("\tTaken: " + taken);
        System.out.println("\tTimeline: " + output);

        System.out.println("Test taken something");
        taken = new HashSet<>(List.of(a08, a48, a67, b36));
        want = new HashSet<>(List.of(b63, b09, b07, c24, c63));
        output = generate(taken, want, new Semester(2022, Session.WINTER));
        System.out.println("\tTaken: " + taken);
        System.out.println("\tTimeline: " + output);

        System.out.println("Test taken nothing and only one course in want list");
        taken = new HashSet<>();
        want = new HashSet<>(List.of(c63));
        output = generate(taken, want, new Semester(2022, Session.SUMMER));
        System.out.println("\tTaken: " + taken);
        System.out.println("\tTimeline: " + output);
    }
}
