package com.example.b07;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding b;

    private String username() {
        return b.username.getText().toString();
    }

    private String password() {
        return b.password.getText().toString();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Admin.getInstance();

        Account acc = Account.getInstance();
        acc.toast = message -> Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        acc.welcome = name -> {
            acc.toast.run("Welcome " + name + "!");
            // update last seen
            OffsetDateTime now = OffsetDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
            acc.ref.child(name).child("last").setValue(formatter.format(now).replace('T', ' ').replaceFirst("\\.[0-9]+", ""));

            // go to main activity
            int nav = acc.privileged ? R.id.action_Login_to_Admin : R.id.action_Login_to_Student;
            NavHostFragment.findNavController(this).navigate(nav);
        };
        LoginPresenter presenter = new LoginPresenter(this, Account.getInstance());
        TextWatcher callback = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String name = presenter.checkUsername(username());
                String pass = presenter.checkPassword(password());
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
                Account.login(username(), password());
            }
            return false;
        });

        b.asStudent.setOnClickListener(v -> Account.login("student", presenter.sha256("password")));
        b.asAdmin.setOnClickListener(v -> Account.login("admin", presenter.sha256("password")));
        b.login.setOnClickListener(v -> Account.login(username(), presenter.sha256(password())));
        b.register.setOnClickListener(v -> Account.register(username(), presenter.sha256(password())));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
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

}