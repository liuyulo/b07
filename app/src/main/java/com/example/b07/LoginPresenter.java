package com.example.b07;

import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.b07.user.Account;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

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
        Account.toast = message -> Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
        Account.welcome = name -> {
            Context context = view.getContext();
            Toast.makeText(context, "Welcome " + name + "!", Toast.LENGTH_SHORT).show();
            // update last seen
            OffsetDateTime now = OffsetDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
            Account.ref.child(name).child("last").setValue(formatter.format(now).replace('T', ' ').replaceFirst("\\.[0-9]+", ""));

            // go to main activity
            int nav = this.model.privileged ? R.id.action_Login_to_Admin : R.id.action_Login_to_Student;
            NavHostFragment.findNavController(view).navigate(nav);
        };
    }

    /**
     * @return error message if invalid, else null
     */
    static String checkPassword(String password) {
        if (password.length() < N) return PASSWORD_TOO_SHORT;
        return null;
    }

    /**
     * @return error message if invalid, else null
     */
    static String checkUsername(String username) {
        if (username.trim().length() == 0) return USERNAME_EMPTY;
        if(username.startsWith(" ")) return USERNAME_STARTS_WHITESPACE;
        if(username.endsWith(" ")) return USERNAME_ENDS_WHITESPACE;
        return null;
    }

    /**
     * Encrypt to sha256
     */
    public static String sha256(String message) {
        // My IDE: Unhandled exception: NoSuchAlgorithmException
        // Me: trust me bro
        return encrypt(message, "SHA-256");
    }

    protected static String encrypt(String message, String algorithm) {
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

