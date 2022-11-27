package com.example.b07;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.b07.databinding.FragmentFirstBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private final String TAG = "FirstFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Account.login("student", "student");
//        Account.login("admin", "password");
        User u = Admin.getInstance();
        TextView t = view.findViewById(R.id.textview_first);
        // see values/strings.xml
        t.setText(getResources().getString(R.string.first_fragment_user, u.name));
        view.findViewById(R.id.button_timeline).setOnClickListener(
            v -> NavHostFragment.findNavController(FirstFragment.this).navigate(
                R.id.action_First_to_Timeline
            )
        );
        view.findViewById(R.id.button_student).setOnClickListener(
            v -> NavHostFragment.findNavController(FirstFragment.this).navigate(
                R.id.action_FirstFragment_to_Student
            )
        );
//        Course c = Course.from("CSCB07");
//        Course c = new Course("aaaa01", Session.FS, Set.of(Course.from("CSCB07")));
//        AtomicInteger count = new AtomicInteger();
//        view.findViewById(R.id.test).setOnClickListener(v -> {
//            if (count.get() % 2 == 0) {
//                Log.d(TAG, "add " + u.add(c));
//            } else {
//                Log.d(TAG, "remove " + u.remove(c));
//            }
//            Log.d(TAG, String.valueOf(u.courses.stream().map(o -> o.code).collect(Collectors.toSet())));
//            count.getAndIncrement();
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}