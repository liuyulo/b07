package com.example.b07;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum Session {
    WINTER("Winter"), SUMMER("Summer"), FALL("Fall");
    // for debugging
    static final Set<Session> FW = new HashSet<>(Arrays.asList(Session.FALL, Session.WINTER));
    static final Set<Session> FS = new HashSet<>(Arrays.asList(Session.FALL, Session.SUMMER));
    static final Set<Session> WS = new HashSet<>(Arrays.asList(Session.WINTER, Session.SUMMER));

    private final String name;
    private static final Map<String, Session> map = new HashMap<>();

    static {
        for (Session s : values()) {
            map.put(s.name, s);
        }
    }

    Session(String name) {
        this.name = name;
    }

    public static Session from(String name) {
        return map.get(name);
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
