package com.example.b07;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.b07.databinding.FragmentFirstBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        User u = User.login("admin", "password");
        TextView tv = (TextView) view.findViewById(R.id.textview_first);
        DatabaseReference user = FirebaseDatabase.getInstance().getReference("users");
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (u == null) {
                    tv.setText("User not found");
                } else {
                    tv.setText(u.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Add log when refactoring for warning
            }
        });


        //TextView.setText

        /*
        
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
         */

        view.findViewById(R.id.test).setOnClickListener(v -> {


            // test your features here
            // and click the TEST button to trigger
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}