package com.example.b07;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07.adapter.CourseAdapter;
import com.example.b07.course.Course;
import com.example.b07.user.Account;
import com.example.b07.user.Admin;

import java.util.TreeSet;

public class AdminFragment extends Fragment {
    Admin a;

    public AdminFragment() {
        a = Admin.getInstance();
        a.adapter = new CourseAdapter(() -> new TreeSet<>(Course.cache.values()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin, container, false);
        ((TextView) view.findViewById(R.id.admin_name)).setText(Account.name);
        RecyclerView recycler = view.findViewById(R.id.courses_all);
        recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycler.setAdapter(a.adapter);
        return view;
    }
}