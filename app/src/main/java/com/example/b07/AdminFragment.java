package com.example.b07;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07.user.Account;
import com.example.b07.user.Admin;

public class AdminFragment extends Fragment {
    final Admin a = Admin.getInstance();
    final Account acc = Account.getInstance();

    public AdminFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TextView) view.findViewById(R.id.admin_name)).setText(acc.name);
        RecyclerView recycler = view.findViewById(R.id.courses_all);
        recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycler.setAdapter(a.adapter);

        view.findViewById(R.id.create_course).setOnClickListener(
            v -> NavHostFragment.findNavController(AdminFragment.this).navigate(R.id.action_Admin_to_Create)
        );
    }
}