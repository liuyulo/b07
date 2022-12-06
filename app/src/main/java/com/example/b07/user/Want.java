package com.example.b07.user;

import java.util.Objects;

public class Want extends Student {
    public static Want instance;
    private static String name;

    private Want(String n) {
        super(n);
        name = n;
    }

    public static Want getInstance() {
        String n = Account.getInstance().name;
        if (instance == null || !Objects.equals(name, n)) instance = new Want(n);
        return instance;
    }

    @Override
    protected String key() {
        return "wants";
    }
}
