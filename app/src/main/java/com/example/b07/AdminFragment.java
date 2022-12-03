package com.example.b07;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;

public class AdminFragment extends Fragment {
    public AdminFragment() {
        Course.adapter = new CourseAdapter(() -> new HashSet<>(Course.cache.values()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin, container, false);
        ((TextView) view.findViewById(R.id.admin_name)).setText(Admin.getInstance().name);
        RecyclerView all = view.findViewById(R.id.courses_all);
        all.setLayoutManager(new LinearLayoutManager(view.getContext()));
        all.setAdapter(Course.adapter);
        return view;
    }
}