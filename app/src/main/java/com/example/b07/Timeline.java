import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Timeline {
	public static Map<String, Set<Course>> generate(Set<Course> t, Set<Course> w, Semester current){

        Set<Course> taken = new HashSet<>(t);
        Set<Course> want = new HashSet<>(w);

        Set<Course> missing = new HashSet<>();
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
        
        Map<String, Set<Course>> output = new LinkedHashMap<>();

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
}
