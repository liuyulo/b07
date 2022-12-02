package com.example.b07;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.b07.databinding.ActivityMainBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private List<String> codelist = Course.cache.values().stream().map(c -> c.code).collect(Collectors.toList());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //test codes. When Course.cache contain all course codes, we don't need these anymore
        codelist.add("cscb09");
        codelist.add("csca48");
        codelist.add("cscc24");
        //End of test codes.
        Collections.sort(codelist);
        codelist.add(0,"Taken");
        ActivityMainBinding b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_item, codelist);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        b.coursesSpinner.setAdapter(arrayAdapter);
        b.coursesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    Course selected = Course.from(codelist.get(i));
                    Student s = Student.getInstance();
                    if (s.courses.contains(selected)) {
                        Toast.makeText(getApplicationContext(), "Course exist in Taken List",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        s.add(selected);
                        s.courses.add(selected);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        /*b.testTake.setOnClickListener(v -> {
            Course a48 = Course.from("csca48");
            Student s = Student.getInstance();
            if (s.courses.contains(a48)) {
                s.remove(a48);
            } else {
                s.add(a48);
            }
        })*/

        setSupportActionBar(b.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}