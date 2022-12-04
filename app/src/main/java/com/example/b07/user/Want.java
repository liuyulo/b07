package com.example.b07.user;

import java.util.Objects;

public class Want extends Student {
    private static Want instance;

    private Want(String name) {
        super(name);
    }

    public static Want getInstance() {
        String name = Account.name;
        if (instance == null || !Objects.equals(Student.name, name)) instance = new Want(name);
        return instance;
    }

    @Override
    protected String key() {
        return "wants";
    }
}
