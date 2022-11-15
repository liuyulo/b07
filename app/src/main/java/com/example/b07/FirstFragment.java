package com.example.b07;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.b07.databinding.FragmentFirstBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // read listener
        DatabaseReference courses  = FirebaseDatabase.getInstance().getReference("courses");
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

        // set value
        DatabaseReference now = FirebaseDatabase.getInstance().getReference("now");
        Date date = new Date();
//        Calendar c = Calendar.getInstance();
//        c.setTime(date);
//        now.setValue(c.get(Calendar.MONTH));
        now.setValue(date.getTime());

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(
            view1 -> NavHostFragment.findNavController(FirstFragment.this).navigate(
                R.id.action_FirstFragment_to_SecondFragment
            )
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}