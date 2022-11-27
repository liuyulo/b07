package com.example.b07;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07.databinding.FragmentTimelineBinding;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TimelineViewAdapter extends RecyclerView.Adapter<TimelineViewAdapter.ViewHolder> {

    public static class TimelineItem {
        public final String session;
        public final List<String> codes;

        public TimelineItem(String session, Set<Course> courses) {
            this.session = session;
            this.codes = courses.stream().map(c -> c.code).collect(Collectors.toList());
        }
    }

    private final List<TimelineItem> courses;

    public TimelineViewAdapter(Map<String, Set<Course>> timeline) {
        courses = timeline.entrySet().stream().map(
            e -> new TimelineItem(e.getKey(), e.getValue())
        ).collect(Collectors.toList());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentTimelineBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // set strings
        holder.item = courses.get(position);
        holder.id.setText(courses.get(position).session);
        List<String> codes = courses.get(position).codes.stream().map(
            c -> c.toUpperCase(Locale.ROOT)
        ).collect(Collectors.toList());
        holder.content.setText(codes.size() > 0 ? String.join(", ", codes) : "None");
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView id;
        public final TextView content;
        public TimelineItem item;

        public ViewHolder(FragmentTimelineBinding binding) {
            super(binding.getRoot());
            id = binding.itemNumber;
            content = binding.content;
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + content.getText() + "'";
        }
    }
}