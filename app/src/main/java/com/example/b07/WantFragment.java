package com.example.b07;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.b07.adapter.CourseAdapter;
import com.example.b07.user.Student;
import com.example.b07.user.Want;

public class WantFragment extends Fragment {

    Student s = Want.getInstance();

    public WantFragment() {
        s.adapter = new CourseAdapter(() -> s.courses);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_want, container, false);
        RecyclerView want = view.findViewById(R.id.want);
        want.setLayoutManager(new LinearLayoutManager(view.getContext()));
        want.setAdapter(s.adapter);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).findViewById(R.id.t_wish).setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).findViewById(R.id.t_wish).setVisibility(View.VISIBLE);
    }
}
