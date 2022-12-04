package com.example.b07;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.b07.databinding.FragmentLoginBinding;
import com.example.b07.user.Account;
import com.example.b07.user.Admin;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

public class LoginFragment extends Fragment {

    public static final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

    private FragmentLoginBinding b;

    private String username() {
        return b.username.getText().toString();
    }

    private String password() {
        return b.password.getText().toString();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentLoginBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Admin.getInstance();
        // check username and password validity
        TextWatcher callback = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String name = checkUsername(username());
                String pass = checkPassword(password());
                boolean enabled = name == null && pass == null;
                if (name != null) b.username.setError(name);
                if (pass != null) b.password.setError(pass);
                b.login.setEnabled(enabled);
                b.register.setEnabled(enabled);
            }
        };
        b.username.addTextChangedListener(callback);
        b.password.addTextChangedListener(callback);
        b.password.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login(username(), password());
            }
            return false;
        });

        b.asStudent.setOnClickListener(v -> login("student", "password"));
        b.asAdmin.setOnClickListener(v -> login("admin", "password"));
        b.login.setOnClickListener(v -> login(username(), password()));
        b.register.setOnClickListener(v -> register(username(), password()));
    }


    private String checkPassword(String password) {
        final int n = 8;
        if (password.length() < n) {
            return "Password must be at least " + n + " characters";
        }
        return null;
    }

    private String checkUsername(String username) {
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

    /**
     * welcome user to the next fragment
     */
    public void welcome(String name) {
        Context context = getContext();
        Toast.makeText(context, "Welcome " + name + "!", Toast.LENGTH_SHORT).show();
        // update last seen
        OffsetDateTime now = OffsetDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        ref.child(name).child("last").setValue(formatter.format(now).replace('T', ' ').replaceFirst("\\.[0-9]+", ""));

        // go to main activity
        int nav = Account.privileged ? R.id.action_Login_to_Admin : R.id.action_Login_to_Student;
        NavHostFragment.findNavController(LoginFragment.this).navigate(nav);
    }

    public void login(String name, String password) {
        Account.name = name;
        DatabaseReference ref = LoginFragment.ref.child(name);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object u = snapshot.getValue();
                Context context = getContext();
                // check existence
                if (u == null) {
                    String dne = "User " + name + " does not exist!";
                    Toast.makeText(context, dne, Toast.LENGTH_LONG).show();
                    return;
                }
                // check password
                String hash = snapshot.child("passwd").getValue(String.class);
                if (!Objects.equals(sha256(password), hash)) {
                    String incorrect = "Incorrect password!";
                    Toast.makeText(context, incorrect, Toast.LENGTH_LONG).show();
                    return;
                }

                // check privileged
                Account.privileged = Boolean.TRUE.equals(snapshot.child("privileged").getValue(Boolean.class));
                welcome(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Account", "login cancelled");
            }
        });
    }

    /**
     * Sign up as student
     */
    public void register(String name, String password) {
        Account.name = name;
        Account.privileged = false;
        DatabaseReference ref = LoginFragment.ref.child(name);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String dne = "User " + name + " already exists!";
                    Toast.makeText(getContext(), dne, Toast.LENGTH_LONG).show();
                    return;
                }
                ref.updateChildren(Map.of(
                    "passwd", sha256(password), "privileged", false, "courses", Map.of()
                ));
                welcome(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }
}