package com.example.b07;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TakenFragment extends Fragment {

    Student s = Student.getInstance();
    private Button btnInsert;
    private Button btnDelete;
    private EditText editTextInsert;
    private EditText editTextDelete;


    public TakenFragment() {
        s.adapter = new CourseAdapter(() -> s.courses);
    }


    @Override
    public void onResume() {
        ((AppCompatActivity) getActivity()).findViewById(R.id.test_take).setVisibility(View.VISIBLE);
        super.onResume();
    }

    @Override
    public void onStop() {
        ((AppCompatActivity) getActivity()).findViewById(R.id.test_take).setVisibility(View.GONE);
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_taken, container, false);
        RecyclerView taken = view.findViewById(R.id.taken);
        taken.setHasFixedSize(true);
        taken.setLayoutManager(new LinearLayoutManager(view.getContext()));
        taken.setAdapter(s.adapter);

        btnInsert = view.findViewById(R.id.btn_insert);
        btnDelete = view.findViewById(R.id.btn_delete);
        editTextInsert = view.findViewById(R.id.edittext_insert);
        editTextDelete = view.findViewById(R.id.edittext_delete);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s.add(Course.from(editTextInsert.getText().toString()));
                s.adapter.notifyDataSetChanged();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s.remove(Course.from(editTextDelete.getText().toString()));
                s.adapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}