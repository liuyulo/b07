package com.example.b07;

import androidx.fragment.app.Fragment;

import com.example.b07.user.Account;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginPresenter {

    Fragment view;
    Account model;

    public static final int N = 8;
    public static final String PASSWORD_TOO_SHORT = String.format("Password must be at least %d characters", N);
    public static final String USERNAME_EMPTY = "Username cannot be empty";
    public static final String USERNAME_STARTS_WHITESPACE = "Username cannot start with whitespace";
    public static final String USERNAME_ENDS_WHITESPACE = "Username cannot end with whitespace";

    public LoginPresenter(Fragment view, Account model) {
        this.view = view;
        this.model = model;
    }

    /**
     * @return error message if invalid, else null
     */
     String checkPassword(String password) {
        if (password.length() < N) return PASSWORD_TOO_SHORT;
        return null;
    }

    /**
     * @return error message if invalid, else null
     */
     String checkUsername(String username) {
        if (username.trim().length() == 0) return USERNAME_EMPTY;
        if (username.startsWith(" ")) return USERNAME_STARTS_WHITESPACE;
        if (username.endsWith(" ")) return USERNAME_ENDS_WHITESPACE;
        return null;
    }

    /**
     * Encrypt to sha256
     */
    public  String sha256(String message) {
        // My IDE: Unhandled exception: NoSuchAlgorithmException
        // Me: trust me bro
        return encrypt(message, "SHA-256");
    }

    protected  String encrypt(String message, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] bytes = digest.digest(message.getBytes(StandardCharsets.UTF_8));
            return String.format("%064x", new BigInteger(1, bytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.err.println("yo wdym you dont know what sha256 is");
        }
        return "";
    }
}

