package com.example.b07.adapter;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07.course.Course;
import com.example.b07.user.Student;
import com.example.b07.user.Taken;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

class CourseHolder extends RecyclerView.ViewHolder {
    public final TextView code;
    public final TextView prereq;
    public final TextView title;
    public Button deleteCross;

    public CourseHolder(com.example.b07.databinding.FragmentCourseBinding b) {
        super(b.getRoot());
        code = b.courseCode;
        title = b.courseTitle;
        prereq = b.coursePrereqs;
        deleteCross = b.deleteCross;

        deleteCross.setOnClickListener(v -> {
            Student s = Taken.getInstance();
            s.remove(Course.from(code.toString()));
        });
    }
}


public class CourseAdapter extends RecyclerView.Adapter<CourseHolder> {
    GetCourse courses;

    public CourseAdapter(GetCourse courses) {
        this.courses = courses;
    }

    private Set<Course> courses() {
        return this.courses.get();
    }

    @NonNull
    @Override
    public CourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CourseHolder(com.example.b07.databinding.FragmentCourseBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CourseHolder holder, int position) {
        Log.d("CourseAdapter", String.valueOf(this.courses()));
        Course course = new ArrayList<>(this.courses()).get(position);
        holder.code.setText(course.code);
        holder.title.setText(course.name);
        List<String> pres = course.prereqs.stream().map(c -> c.code.toUpperCase(Locale.ROOT)).collect(Collectors.toList());
        holder.prereq.setText("Prerequisites: " + String.join(", ", pres));
    }

    @Override
    public int getItemCount() {
        return this.courses().size();
    }
}
