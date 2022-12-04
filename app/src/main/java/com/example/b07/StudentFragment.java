package com.example.b07;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07.user.Account;
import com.example.b07.user.CourseType;
import com.example.b07.user.Student;
import com.example.b07.user.Taken;
import com.example.b07.user.Want;

import java.util.Map;

public class StudentFragment extends Fragment {

    private static final String TAG = "Student";
    private final Taken t = Taken.getInstance();
    private final Want w = Want.getInstance();
    private static final Map<Integer, Integer> nav = Map.of(
        R.id.button_timeline, R.id.action_Student_to_Timeline,
        R.id.button_add, R.id.action_Student_to_Add
    );


    public StudentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TextView) view.findViewById(R.id.student_name)).setText(Account.name);
        RecyclerView courses = view.findViewById(R.id.courses);
        courses.setLayoutManager(new LinearLayoutManager(view.getContext()));
        Button b = view.findViewById(R.id.switch_courses);
        b.setOnClickListener(v -> {
            Student.current = Student.current.next();
            setAdapter(view);
        });
        setAdapter(view);
        nav.forEach((button, action) -> view.findViewById(button).setOnClickListener(
            v -> NavHostFragment.findNavController(StudentFragment.this).navigate(action)
        ));
    }

    private void setAdapter(View view) {
        RecyclerView recycler = view.findViewById(R.id.courses);
        switch (Student.current) {
            case WISHLIST -> recycler.setAdapter(w.adapter);
            case TAKEN -> recycler.setAdapter(t.adapter);
        }
        ((Button) view.findViewById(R.id.button_add)).setText("add to " + Student.current.name());
        ((TextView) view.findViewById(R.id.courses_title)).setText("courses " + Student.current.name());
        ((Button) view.findViewById(R.id.switch_courses)).setText("view " + Student.current.next().name());
    }
}