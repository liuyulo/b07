package com.example.b07;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.b07.course.Course;
import com.example.b07.course.Session;
import com.example.b07.user.Admin;

import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateCourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateCourseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Set<Session> sessions;

    public CreateCourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateCourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateCourseFragment newInstance(String param1, String param2) {
        CreateCourseFragment fragment = new CreateCourseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public void onCheckboxClicked(View view) {

        Context context = getContext();
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.checkBox:
                if (checked)
                sessions.add(Session.FALL);
            else
                break;

            case R.id.checkBox2:
                if (checked)
                    sessions.add(Session.WINTER);
                else
                    break;

            case R.id.checkBox3:
                if (checked)
                    sessions.add(Session.SUMMER);
                else
                    break;
        }

        if (sessions.isEmpty())
            Toast.makeText(context, "Please select at least one offering session", Toast.LENGTH_SHORT).show();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       // Course(String code, Set<Session> sessions, Set<Course> prereqs) {
          //  this.code = code;
          //  this.sessions = sessions;
         //   this.prereqs = prereqs;

        View view = inflater.inflate(R.layout.fragment_create_course, container, false);

        EditText course_code = view.findViewById(R.id.edit_course_code);
        String entered_course_code = course_code.getText().toString();

        EditText course_name = view.findViewById(R.id.edit_course_name);
        String entered_course_name = course_code.getText().toString();

        //get list of courses from firebase
        Set<Course> prereq = Set.of(Course.from("CSCA08"), Course.from("MATA31"));

        Course new_course = new Course(entered_course_code, sessions, prereq);
        Admin.getInstance().add(new_course);

        return view;
    }
}