package com.example.b07;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07.databinding.FragmentTimelineBinding;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.Holder> {
    public static class Holder extends RecyclerView.ViewHolder {
        public final TextView semester;
        public final TextView courses;

        public Holder(FragmentTimelineBinding binding) {
            super(binding.getRoot());
            semester = binding.timelineSemester;
            courses = binding.timelineCourses;
        }
    }

    private static class Item {
        public final String session;
        public final List<String> codes;

        public Item(String session, Set<Course> courses) {
            this.session = session;
            this.codes = courses.stream().map(c -> c.code).collect(Collectors.toList());
        }

        @NonNull
        @Override
        public String toString() {
            return "Item{session='" + session + '\'' + ", codes=" + codes + '}';
        }
    }

    private final List<Item> courses;

    public TimelineAdapter(Map<String, Set<Course>> timeline) {
        courses = timeline.entrySet().stream().map(
            e -> new Item(e.getKey(), e.getValue())
        ).collect(Collectors.toList());
        Log.d("Timeline", String.valueOf(courses));
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(FragmentTimelineBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        // set strings
        holder.semester.setText(courses.get(position).session);
        List<String> codes = courses.get(position).codes.stream().map(
            c -> c.toUpperCase(Locale.ROOT)
        ).collect(Collectors.toList());
        holder.courses.setText(codes.size() > 0 ? String.join(", ", codes) : "None");
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

}

/**
 * A fragment representing a list of Items.
 */
public class TimelineFragment extends Fragment {

    public TimelineFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView recycler) {
            Context context = view.getContext();
            recycler.setLayoutManager(new LinearLayoutManager(context));
            recycler.setAdapter(Student.getInstance().tadapter);
        }
        return view;
    }
}