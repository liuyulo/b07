package com.example.b07;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07.adapter.CourseAdapter;
import com.example.b07.user.Admin;
import com.example.b07.user.Student;
import com.example.b07.user.Taken;
import com.example.b07.user.Want;

import java.util.Locale;
import java.util.stream.Collectors;

public class AddFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String title = Student.current.name();
        title = title.substring(0, 1).toUpperCase(Locale.ROOT) + title.substring(1).toLowerCase(Locale.ROOT);
        getActivity().setTitle("Add Courses to " + title);
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Admin a = Admin.getInstance();
        Want w = Want.getInstance();
        Taken t = Taken.getInstance();
        CourseAdapter adapter = new CourseAdapter(
            () -> switch (Student.current) {
                case TAKEN -> a.stream().filter(c -> !t.contains(c)).collect(Collectors.toSet());
                case WISHLIST -> a.stream().filter(c -> !w.contains(c)).collect(Collectors.toSet());
            },
            "+",
            course -> switch (Student.current) {
                case TAKEN -> t.add(course) | w.remove(course);
                case WISHLIST -> t.remove(course) | w.add(course);
            }
        );
        RecyclerView recycler = view.findViewById(R.id.please);
        recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycler.setAdapter(adapter);
    }
}