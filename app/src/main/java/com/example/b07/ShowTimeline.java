package com.example.b07;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.b07.databinding.ActivityShowTimelineBinding;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ShowTimeline extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityShowTimelineBinding binding;

    public void tableTimeline(Map<String, Set<Course>> generated){
        TableLayout tablelayout = (TableLayout) findViewById(R.id.table_main); //create a table
        TableRow firstrow = new TableRow(this);
        TextView session = new TextView(this);
        session.setText("Session");
        session.setBackgroundColor(Color.LTGRAY);
        session.setTextColor(Color.BLACK);
        firstrow.addView(session);
        TextView course = new TextView(this);
        course.setText("Course");
        course.setBackgroundColor(Color.LTGRAY);
        course.setTextColor(Color.BLACK);
        firstrow.addView(course);
        tablelayout.addView(firstrow); //created the first row of table,
        //containing text "Session" and "Course"
        for(int i=0; i<generated.size(); i++){
            TableRow row = new TableRow(this);
            TextView tv = new TextView(this);
            tv.setText(""+generated.keySet().toArray()[i]); // tv shows the session
            tv.setBackgroundColor(Color.WHITE);
            tv.setTextColor(Color.BLACK);
            row.addView(tv);
            TextView tv2 = new TextView(this);
            tv2.setText(""+generated.values().toArray()[i]);// tv2 shows the courses
            tv2.setBackgroundColor(Color.WHITE);
            tv2.setTextColor(Color.BLACK);
            row.addView(tv2);
            tablelayout.addView(row);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_timeline);
        //get the set of courses taken and the set of courses want
        Set<Course> taken = new HashSet<>();
        Course a08 = new Course("CSCA08", Session.FW, new HashSet<>());
        Course a67 = new Course("CSCA67", Session.FW, new HashSet<>());

        Course a48 = new Course("CSCA48", Session.WS, new HashSet<>(List.of(a08)));
        Course b07 = new Course("CSCB07", Session.FS, new HashSet<>(List.of(a48)));
        Course b09 = new Course("CSCB09", Session.WS, new HashSet<>(List.of(a48)));

        Course b36 = new Course("CSCB36", Session.FS, new HashSet<>(List.of(a48, a67)));
        Course b63 = new Course("CSCB63", Session.WS, new HashSet<>(List.of(b36)));

        Course c24 = new Course("CSCC24", Session.WS, new HashSet<>(List.of(b07, b09)));
        Course c63 = new Course("CSCC63", Session.FW, new HashSet<>(List.of(b63, b36)));

        Set<Course> want = new HashSet<>(List.of(c24));
        Semester cur = new Semester(2022, Session.FALL);

        //get the starting semester
        //then generate the timeline using generate() method
        Map<String, Set<Course>> ge = Timeline.generate(taken, want, cur);
        //call tableTimeline(generated)
        tableTimeline(ge);
    }
}