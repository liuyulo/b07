package com.example.b07;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Map;
import java.util.Set;

/**
 * A fragment representing a list of Items.
 */
public class TimelineFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TimelineFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TimelineFragment newInstance(int columnCount) {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView recyclerView) {
            Context context = view.getContext();
            Set<Course> want = Set.of(Course.from("cscc24"));
            Semester cur = new Semester(2022, Session.FALL);
            Map<String, Set<Course>> todo = Student.timeline(Set.of(), want, cur);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new TimelineViewAdapter(todo));
        }
        return view;
    }
}