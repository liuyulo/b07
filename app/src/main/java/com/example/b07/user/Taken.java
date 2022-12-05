package com.example.b07.user;

import java.util.Objects;

public class Taken extends Student {
    private static Taken instance;
    private static String name;

    private Taken(String n) {
        super(n);
        name = n;
    }

    public static Taken getInstance() {
        String n = Account.getInstance().name;
        if (instance == null || !Objects.equals(name, n)) instance = new Taken(n);
        return instance;
    }

    @Override
    protected String key() {
        return "taken";
    }
}
