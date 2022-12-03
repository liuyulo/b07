package com.example.b07;


import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

class CourseHolder extends RecyclerView.ViewHolder {
    public final TextView code;
    public final TextView prereq;
    public final TextView title;

    public CourseHolder(com.example.b07.databinding.FragmentCourseBinding b) {
        super(b.getRoot());
        code = b.courseCode;
        title = b.courseTitle;
        prereq = b.coursePrereqs;
    }
}

interface GetCourse {
    Set<Course> get();
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
        Course course = new ArrayList<>(this.courses()).get(position);
        holder.code.setText(course.code);
        List<String> pres = course.prereqs.stream().map(c -> c.code.toUpperCase(Locale.ROOT)).collect(Collectors.toList());
        holder.prereq.setText("Prerequisites: " + String.join(", ", pres));
    }

    @Override
    public int getItemCount() {
        return this.courses().size();
    }
}
