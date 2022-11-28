package com.example.b07;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07.databinding.FragmentCourseBinding;

import java.util.Map;

class CourseHolder extends RecyclerView.ViewHolder {
    public final TextView code;

    public CourseHolder(FragmentCourseBinding b) {
        super(b.getRoot());
        code = b.courseCode;
    }
}

public class StudentFragment extends Fragment {

    Student s;
    private static final String TAG = "Student";
    private static final Map<Integer, Integer> nav = Map.of(
        R.id.button_timeline, R.id.action_Student_to_Timeline,
        R.id.button_taken, R.id.action_Student_to_Taken
    );


    public StudentFragment() {
        s = Student.getInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TextView) view.findViewById(R.id.student_name)).setText(s.name);
        // tmp code for testing
        Course d01 = Course.from("cscd01");
        view.findViewById(R.id.test_want).setOnClickListener(v -> {
            if (s.wants.contains(d01)) {
                s.unwant(d01);
            } else {
                s.want(d01);
            }
        });

        nav.forEach((button, action) -> view.findViewById(button).setOnClickListener(
            v -> NavHostFragment.findNavController(StudentFragment.this).navigate(action)
        ));
    }
}