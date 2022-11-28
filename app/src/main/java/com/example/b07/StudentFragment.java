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

class CourseViewHolder extends RecyclerView.ViewHolder {
    public final TextView code;

    public CourseViewHolder(FragmentCourseBinding b) {
        super(b.getRoot());
        code = b.courseCode;
    }
}

public class StudentFragment extends Fragment {

    Student s;
    private static final String TAG = "Student";


    public StudentFragment() {
        s = Student.getInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //        RecyclerView taken = view.findViewById(R.id.taken);
//        taken.setLayoutManager(new LinearLayoutManager(view.getContext()));
//        taken.setAdapter(s.adapter);
        return inflater.inflate(R.layout.fragment_student, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TextView) view.findViewById(R.id.student_name)).setText(s.name);
        Course a48 = Course.from("csca48");
        Course d01 = Course.from("cscd01");
        view.findViewById(R.id.test_take).setOnClickListener(v -> {
            if (s.courses.contains(a48)) {
                s.remove(a48);
            } else {
                s.add(a48);
            }
        });
        view.findViewById(R.id.test_want).setOnClickListener(v -> {
            if (s.wants.contains(d01)) {
                s.unwant(d01);
            } else {
                s.want(d01);
            }
        });
        view.findViewById(R.id.button_timeline).setOnClickListener(
            v -> NavHostFragment.findNavController(StudentFragment.this).navigate(
                R.id.action_Student_to_Timeline
            )
        );
        view.findViewById(R.id.button_taken).setOnClickListener(
            v -> NavHostFragment.findNavController(StudentFragment.this).navigate(
                R.id.action_Student_to_Taken
            )
        );
    }
}