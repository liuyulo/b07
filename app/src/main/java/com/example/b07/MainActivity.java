package com.example.b07;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.b07.course.Course;
import com.example.b07.databinding.ActivityMainBinding;
import com.example.b07.user.Student;
import com.example.b07.user.Taken;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    //Get the courses from database, assign them to a list
    private List<String> codelist = Course.cache.values().stream().map(c -> c.code).
            collect(Collectors.toList());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //test codes. When Course.cache contain all course codes, we don't need these anymore
        codelist.add("cscb09");
        codelist.add("csca48");
        codelist.add("cscc24");
        //End of test codes.
        Collections.sort(codelist); //Sorting the items in spinner by codes
        codelist.add(0,"Taken"); //Add a header "Taken" to the spinner
        ActivityMainBinding b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_item, codelist);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        b.coursesSpinner.setAdapter(arrayAdapter);//spinner created
        //Setting up the listener
        b.coursesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    Course selected = Course.from(codelist.get(i));
                    Student s = Taken.getInstance();
                    if (s.courses.contains(selected)) {
                        Toast.makeText(getApplicationContext(), "Course exist in Taken List",
                                Toast.LENGTH_SHORT).show();
                        //If the course have existed in the list, show a toast
                    } else {
                        s.add(selected);
                        s.courses.add(selected);
                        //If not, add it to the list
                    }
                    //Make the spinner header always show "Taken" when not clicked
                    b.coursesSpinner.setSelection(0);
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