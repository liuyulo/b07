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

import java.util.Date;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // read listener
        DatabaseReference courses = FirebaseDatabase.getInstance().getReference("courses");
        courses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object s = snapshot.getValue();
                assert s != null;
                Log.i("firebase", s.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("firebase", "no", error.toException());
            }
        });
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // read listener and live update ui on db change
        TextView tv = view.findViewById(R.id.textview_first);
        DatabaseReference now = FirebaseDatabase.getInstance().getReference("now");
        now.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long n = (long) snapshot.getValue();
                Log.i("firebase", String.valueOf(n));
                tv.setText(String.valueOf(n));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.buttonFirst.setOnClickListener(
            view1 -> {
                // set value
                Date date = new Date();
//                Calendar c = Calendar.getInstance();
//                c.setTime(date);
//                now.setValue(c.get(Calendar.MONTH));
                Log.i("firebase", String.valueOf(date.getTime()));
                now.setValue(date.getTime());
                NavHostFragment.findNavController(FirstFragment.this).navigate(
                    R.id.action_FirstFragment_to_SecondFragment
                );
            }
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}