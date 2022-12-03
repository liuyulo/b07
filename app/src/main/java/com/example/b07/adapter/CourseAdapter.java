package com.example.b07.adapter;


import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07.course.Course;
import com.example.b07.databinding.FragmentCourseBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

class CourseHolder extends RecyclerView.ViewHolder {
    public final TextView code;
    public final TextView prereq;
    public final TextView title;
    public final Button action;

    public CourseHolder(FragmentCourseBinding b) {
        super(b.getRoot());
        code = b.courseCode;
        title = b.courseTitle;
        prereq = b.coursePrereqs;
        action = b.courseAction;
    }
}


public class CourseAdapter extends RecyclerView.Adapter<CourseHolder> {
    GetCourse courses;
    String button;
    CourseAction action;

    public CourseAdapter(GetCourse courses) {
        this.courses = courses;
        button = "x";
        action = c -> {
        };
    }

    public CourseAdapter(GetCourse courses, String s, CourseAction a) {
        this.courses = courses;
        this.button = s;
        this.action = a;
    }

    private Set<Course> courses() {
        return courses.get();
    }

    @NonNull
    @Override
    public CourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CourseHolder(FragmentCourseBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CourseHolder holder, int position) {
        Course course = new ArrayList<>(this.courses()).get(position);
        holder.code.setText(course.code);
        holder.title.setText(course.name);
        List<String> pres = course.prereqs.stream().map(c -> c.code.toUpperCase(Locale.ROOT)).collect(Collectors.toList());
        holder.prereq.setText("Prerequisites: " + String.join(", ", pres));
        holder.action.setText(button);
        holder.action.setOnClickListener(v -> action.run(course));
    }

    @Override
    public int getItemCount() {
        return this.courses().size();
    }
}
