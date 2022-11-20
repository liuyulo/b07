package com.example.b07;

public class CourseAdapter {
    public static String arrayToString(String [] courses) {
        // a string that separates each course by comma
        String S = "";
        for (int i = 0; i < courses.length; i++) {
            S = S + courses[i];
        }

        return S;
    }

    public static String [] stringToArray( String courses) {
        return courses.split(",");
    }
}
