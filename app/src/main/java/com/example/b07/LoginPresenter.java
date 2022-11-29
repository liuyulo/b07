package com.example.b07;

import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class LoginPresenter {

    Fragment view;

    public LoginPresenter(Fragment view) {
        this.view = view;

        Account.toast = message ->{
            var context = view.getContext();
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        };

        Account.welcome = name -> {
            var context = view.getContext();
            Toast.makeText(context, "Welcome " + name + "!", Toast.LENGTH_SHORT).show();
            // update last seen
            OffsetDateTime now = OffsetDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
            LoginFragment.ref.child(name).child("last").setValue(formatter.format(now).replace('T', ' ').replaceFirst("\\.[0-9]+", ""));

            // go to main activity
            if (!Account.privileged) {
                NavHostFragment.findNavController(view).navigate(
                        R.id.action_Login_to_Student
                );
            }
        };
    }

    //move to presenter
    String checkPassword(String password) {
        final int n = 8;
        if (password.length() < n) {
            return "Password must be at least " + n + " characters";
        }
        return null;
    }
    //move to presenter
    String checkUsername(String username) {
        if (username.trim().length() == 0) {
            return "Username cannot be empty or whitespace";
        } else if (username.startsWith(" ")) {
            return "Username cannot start with whitespace";
        } else if (username.endsWith(" ")) {
            return "Username cannot end with whitespace";
        }
        return null;
    }

    /**
     * Encrypt to sha256
     */
    public static String sha256(String message) {
        // My IDE: Unhandled exception: NoSuchAlgorithmException
        // Me: trust me bro
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(message.getBytes(StandardCharsets.UTF_8));
            return String.format("%064x", new BigInteger(1, bytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.err.println("yo wdym you dont know what sha256 is");
        }
        return "";
    }
}

