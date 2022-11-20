package com.example.b07;

import androidx.annotation.NonNull;

public class Semester {
    int year;
    Session session;

    public Semester(int year, Session session) {
        this.year = year;
        this.session = session;
    }

    public Semester next() {
        return switch (session) {
            case WINTER -> new Semester(year, Session.SUMMER);
            case SUMMER -> new Semester(year, Session.FALL);
            case FALL -> new Semester(year + 1, Session.WINTER);
        };
    }

    @NonNull
    @Override
    public String toString() {
        return year + " " + session;
    }
}
