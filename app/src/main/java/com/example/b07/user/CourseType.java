package com.example.b07.user;

public enum CourseType {
    TAKEN,
    WISHLIST;

    public CourseType next() {
        return switch (this) {
            case TAKEN -> WISHLIST;
            case WISHLIST -> TAKEN;
        };
    }
}
