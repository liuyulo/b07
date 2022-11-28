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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07.databinding.FragmentCourseBinding;

import java.util.ArrayList;

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

    private class TakenAdapter extends RecyclerView.Adapter<CourseViewHolder> {
        @NonNull
        @Override
        public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new CourseViewHolder(FragmentCourseBinding.inflate(inflater, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
            var courses = new ArrayList<>(s.courses);
            holder.code.setText(courses.get(position).code);
        }

        @Override
        public int getItemCount() {
            return s.courses.size();
        }
    }


    public StudentFragment() {
        s = Student.getInstance();
        s.adapter = new TakenAdapter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student, container, false);
        RecyclerView taken = view.findViewById(R.id.taken);
        taken.setLayoutManager(new LinearLayoutManager(view.getContext()));
        taken.setAdapter(s.adapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TextView) view.findViewById(R.id.student_name)).setText(s.name);
        Course a08 = Course.from("csca08");
        Course d01 = Course.from("cscd01");
        view.findViewById(R.id.test_take).setOnClickListener(v -> {
            if (s.courses.contains(a08)) {
                s.remove(a08);
            } else {
                s.add(a08);
            }
        });
        view.findViewById(R.id.test_want).setOnClickListener(v -> {
            if (s.wants.contains(d01)) {
                s.unwant(d01);
            } else {
                s.want(d01);
            }
        });
        view.findViewById(R.id.timeline).setOnClickListener(
            v -> NavHostFragment.findNavController(StudentFragment.this).navigate(
                R.id.action_Student_to_Timeline
            )
        );
    }
}