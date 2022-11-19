import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Course{
    String code;
    Set<Session> sessions;
    Set<Course> prereqs;

    public Course(String code, Set<Session> sessions, Set<Course> prereqs){
        this.code = code;
        this.sessions = sessions;
        this.prereqs = prereqs;
    }

    @Override
    public String toString(){
        return code;
    }
}

class Semester{
    int year;
    Session session;

    public Semester(int year, Session session){
        this.year = year;
        this.session = session;
    }

    public Semester next(){
        return switch(session){
            case WINTER -> new Semester(year, Session.SUMMER);
            case SUMMER -> new Semester(year, Session.FALL);
            case FALL -> new Semester(year + 1, Session.WINTER);
        };
    }

    @Override
    public String toString(){
        return year + " " + session;
    }
}

enum Session{
    WINTER("Winter"), SUMMER("Summer"), FALL("Fall");
    public static final Set<Session> FW = new HashSet<>(Arrays.asList(Session.FALL, Session.WINTER));
    public static final Set<Session> FS = new HashSet<>(Arrays.asList(Session.FALL, Session.SUMMER));
    public static final Set<Session> WS = new HashSet<>(Arrays.asList(Session.WINTER, Session.SUMMER));
    private final String name;

    Session(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
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
    public static Map<String, Set<Course>> generate(Set<Course> t, Set<Course> w, Semester current){
        // don't want to mutate the input
        Set<Course> taken = new HashSet<>(t);
        Set<Course> want = new HashSet<>(w);

        Set<Course> missing = new HashSet<>();
        //This set will record necessary courses to form the timeline which missing in Set w.
        int counter = 1;
        while(counter>0) {
        	counter = 0;
        	for(Course course: want) {
        		for(Course inpre: course.prereqs)
        			if((!taken.contains(inpre)) && (!want.contains(inpre))) {
        				missing.add(inpre);
        				counter ++;
        			}
        	}
        	want.addAll(missing);
        }
        //After the loop, we added all neccessary courses into want.
        
        Map<String, Set<Course>> output = new LinkedHashMap<>();
        
        //This loop go through courses in want and add those which can be taken in current semester
        //Then go to next semester and repeat. Until want is empty.
        while(!want.isEmpty()){
            Set<Course> toTake = new HashSet<>();
            for(Course course : want){
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
        taken = new HashSet<>(List.of(a08,a48,a67,b36));
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
        System.out.println("Test taken nothing and only one course in want list");
        taken = new HashSet<>();
        want = new HashSet<>(List.of(c63));
        output = generate(taken, want, new Semester(2022, Session.SUMMER));
        System.out.println("\tTaken: " + taken);
        System.out.println("\tTimeline: " + output);
    }
}
