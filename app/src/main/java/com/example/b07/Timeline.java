package com.example.b07;

import java.util.*;
import java.util.stream.Collectors;

class Cours {
    String code;
    Set<Session> sessions;
    Set<Cours> prereqs;

    public Cours(String code, Set<Session> sessions, Set<Cours> prereqs){
        this.code = code;
        this.sessions = sessions;
        this.prereqs = prereqs;
    }

    @Override
    public String toString(){
        return code;
    }
}

public class Timeline{
    /**
     * Generate timeline
     *
     * @param t       courses taken
     * @param w       courses want to take (t ⋂ w = ∅)
     * @param current the semester to start with
     * @return generated timeline
     */
    public static Map<String, Set<Cours>> generate(Set<Cours> t, Set<Cours> w, Semester current){
        // don't want to mutate the input
        Set<Cours> taken = new HashSet<>(t);
        Set<Cours> want = new HashSet<>(w);

        // This set will record necessary courses to form the timeline which missing in Set w.
        Set<Cours> need;
        do{
            need = want.stream().flatMap(course -> course.prereqs.stream().filter(
                prereq -> !(taken.contains(prereq) || want.contains(prereq))
            )).collect(Collectors.toSet());
            want.addAll(need);
        }while(!need.isEmpty());
        // After the loop, we added all necessary courses into want.

        Map<String, Set<Cours>> output = new LinkedHashMap<>();

        // This loop go through courses in want and add those which can be taken in current semester
        // Then go to next semester and repeat. Until want is empty.
        while(!want.isEmpty()){
            Set<Cours> toTake = new HashSet<>();
            for(Cours course : want){
                if(taken.containsAll(course.prereqs) && course.sessions.contains(current.session)){
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

    public static void main(String[] args){
        Cours a08 = new Cours("CSCA08", Session.FW, new HashSet<>());
        Cours a67 = new Cours("CSCA67", Session.FW, new HashSet<>());

        Cours a48 = new Cours("CSCA48", Session.WS, new HashSet<>(List.of(a08)));
        Cours b07 = new Cours("CSCB07", Session.FS, new HashSet<>(List.of(a48)));
        Cours b09 = new Cours("CSCB09", Session.WS, new HashSet<>(List.of(a48)));

        Cours b36 = new Cours("CSCB36", Session.FS, new HashSet<>(List.of(a48, a67)));
        Cours b63 = new Cours("CSCB63", Session.WS, new HashSet<>(List.of(b36)));

        Cours c24 = new Cours("CSCC24", Session.WS, new HashSet<>(List.of(b07, b09)));
        Cours c63 = new Cours("CSCC63", Session.FW, new HashSet<>(List.of(b63, b36)));

        System.out.println("Test taken nothing");
        Set<Cours> taken = new HashSet<>();
        Set<Cours> want = new HashSet<>(List.of(c24, a08, a48, b07, b36, a67));
        Map<String, Set<Cours>> output = generate(taken, want, new Semester(2022, Session.FALL));
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
