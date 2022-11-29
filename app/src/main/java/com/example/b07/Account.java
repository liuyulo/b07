package com.example.b07;

import androidx.annotation.NonNull;

public class Account {
    public static String name;
    public static boolean privileged;


    @NonNull
    @Override
    public String toString() {
        return "Account{name='" + name + "'" + ", privileged=" + privileged + '}';
    }

}
