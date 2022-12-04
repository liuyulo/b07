package com.example.b07;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class WantFragment extends Fragment {

    Student s = Student.getInstance();
    private Button btnInsert;
    private Button btnDelete;
    private EditText editTextInsert;
    private EditText editTextDelete;

    public WantFragment() {
        s.adapter = new CourseAdapter(() -> s.wants);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_want, container, false);
        RecyclerView want = view.findViewById(R.id.want);
        want.setLayoutManager(new LinearLayoutManager(view.getContext()));
        want.setAdapter(s.adapter);

        btnInsert = view.findViewById(R.id.btn_insert);
        btnDelete = view.findViewById(R.id.btn_delete);
        editTextInsert = view.findViewById(R.id.edittext_insert);
        editTextDelete = view.findViewById(R.id.edittext_delete);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mInsert = editTextInsert.getText().toString();
                if (mInsert.isEmpty() || mInsert.matches("")){
                    return;
                }

                s.add(Course.from(mInsert));
                editTextInsert.getText().clear();
                s.adapter.notifyDataSetChanged();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mDelete = editTextDelete.getText().toString();
                if (mDelete.isEmpty() || mDelete.matches("")){
                    return;
                }

                s.remove(Course.from(mDelete));
                editTextDelete.getText().clear();
                s.adapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}
