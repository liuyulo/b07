package com.example.b07.user;

import java.util.Objects;

public class Taken extends Student {
    private static Taken instance;

    private Taken(String name) {
        super(name);
    }

    public static Taken getInstance() {
        String name = Account.getInstance().name;
        if (instance == null || !Objects.equals(Student.name, name)) instance = new Taken(name);
        return instance;
    }

    @Override
    protected String key() {
        return "taken";
    }
}
