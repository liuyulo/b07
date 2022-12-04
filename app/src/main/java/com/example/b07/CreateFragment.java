package com.example.b07;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.b07.course.Course;
import com.example.b07.course.Session;
import com.example.b07.databinding.FragmentCreateBinding;
import com.example.b07.user.Admin;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CreateFragment extends Fragment {
    final Admin a = Admin.getInstance();
    private FragmentCreateBinding b;
    Course course = new Course("", Set.of(), Set.of());
    ArrayAdapter<String> adapter;
    Map<CheckBox, Session> sessions;
    final Integer spinner = android.R.layout.simple_spinner_dropdown_item;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentCreateBinding.inflate(inflater, container, false);
        sessions = Map.of(b.fall, Session.FALL, b.winter, Session.WINTER, b.summer, Session.SUMMER);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // assume Course.cache is complete
        b.code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String t = s.toString();
                if (!t.matches("\\w+")) {
                    b.code.setError("Course code must be alphanumeric");
                    return;
                }
                if (Course.cache.containsKey(t)) {
                    exists(course = Course.from(t));
                    b.create.setVisibility(View.GONE);
                    b.update.setVisibility(View.VISIBLE);
                    b.delete.setVisibility(View.VISIBLE);
                } else {
                    b.create.setVisibility(View.VISIBLE);
                    b.update.setVisibility(View.GONE);
                    b.delete.setVisibility(View.GONE);
                }
            }
        });
        b.prerequisite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String[] t = s.toString().split(" ");
                List.of(b.create, b.update).forEach(button -> button.setEnabled(true));
                for (var code : t) {
                    if (!Course.cache.containsKey(code)) {
                        b.prerequisite.setError(String.format("%s does not exist in database!", code));
                        List.of(b.create, b.update).forEach(button -> button.setEnabled(false));
                        return;
                    }
                }
            }
        });
        List<String> codes = a.stream().map(c -> c.code).collect(Collectors.toList());
        b.code.setAdapter(new ArrayAdapter<>(getContext(), spinner, codes));

        adapter = new ArrayAdapter<>(getContext(), spinner, codes.stream().filter(c -> !c.equals(course.code)).collect(Collectors.toList()));
        b.prerequisite.setTokenizer(new SpaceTokenizer());
        b.prerequisite.setAdapter(adapter);

        b.create.setOnClickListener(this::create);
    }

    private void exists(Course course) {
        // given the courses exists, autofill its info
        b.name.setText(course.name);
        for (Map.Entry<CheckBox, Session> entry : sessions.entrySet()) {
            entry.getKey().setChecked(course.sessions.contains(entry.getValue()));
        }
        String prereqs = course.prereqs.stream().map(c -> c.code).collect(Collectors.joining(" "));
        b.prerequisite.setText(prereqs);
        adapter.notifyDataSetChanged();
    }

    private void create(View view) {
        // create new course if it has sessions
        Set<Session> sessions = this.sessions.entrySet().stream().filter(
            e -> e.getKey().isChecked()
        ).map(Map.Entry::getValue).collect(Collectors.toSet());
        if (sessions.size() == 0) {
            Toast.makeText(getContext(), "Please select at least one session!", Toast.LENGTH_SHORT).show();
            return;
        }

        String code = b.code.getText().toString();
        String name = b.name.getText().toString();
        Set<Course> prereqs = Arrays.stream(b.prerequisite.getText().toString().split(" ")).map(Course::from).collect(Collectors.toSet());
        Course course = new Course(code, name, sessions, prereqs);
        a.add(course);
        Course.cache.put(code, course);
        Toast.makeText(getContext(), "Course " + code + " created!", Toast.LENGTH_SHORT).show();
    }
}