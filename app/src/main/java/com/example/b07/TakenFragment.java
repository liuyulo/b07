package com.example.b07;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07.databinding.FragmentCourseBinding;

import java.util.ArrayList;

public class TakenFragment extends Fragment {


    private class TakenAdapter extends RecyclerView.Adapter<CourseViewHolder> {
        @NonNull
        @Override
        public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new CourseViewHolder(FragmentCourseBinding.inflate(inflater, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
            ArrayList<Course> courses = new ArrayList<>(Student.getInstance().courses);
            holder.code.setText(courses.get(position).code);
        }

        @Override
        public int getItemCount() {
            return Student.getInstance().courses.size();
        }
    }

    public TakenFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_taken, container, false);
        RecyclerView taken = view.findViewById(R.id.taken);
        taken.setLayoutManager(new LinearLayoutManager(view.getContext()));
        taken.setAdapter(new TakenAdapter());
        return view;
    }
}