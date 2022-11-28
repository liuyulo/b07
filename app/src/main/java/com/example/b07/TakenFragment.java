package com.example.b07;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07.databinding.FragmentCourseBinding;

import java.util.ArrayList;

public class TakenFragment extends Fragment {

    Student s = Student.getInstance();

    public class Adapter extends RecyclerView.Adapter<CourseHolder> {
        @NonNull
        @Override
        public CourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new CourseHolder(FragmentCourseBinding.inflate(inflater, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull CourseHolder holder, int position) {
            ArrayList<Course> courses = new ArrayList<>(s.courses);
            holder.code.setText(courses.get(position).code);
        }

        @Override
        public int getItemCount() {
            return s.courses.size();
        }
    }

    public TakenFragment() {
        s.adapter = new Adapter();
    }


    @Override
    public void onResume() {
        ((AppCompatActivity) getActivity()).findViewById(R.id.test_take).setVisibility(View.VISIBLE);
        super.onResume();
    }

    @Override
    public void onStop() {
        ((AppCompatActivity) getActivity()).findViewById(R.id.test_take).setVisibility(View.GONE);
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_taken, container, false);
        RecyclerView taken = view.findViewById(R.id.taken);
        taken.setLayoutManager(new LinearLayoutManager(view.getContext()));
        taken.setAdapter(s.adapter);
        return view;
    }
}