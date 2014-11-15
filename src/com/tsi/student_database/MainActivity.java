package com.tsi.student_database;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import com.tsi.student_database.adapters.MainListAdapter;
import com.tsi.student_database.models.Student;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ListView main_list = (ListView) findViewById(R.id.main_list);
        List<Student> studentList = new ArrayList<Student>();
        for (int i = 0; i < 10; i++) {
            studentList.add(new Student("firstname" + i, "lastname" + i, "personalcode"));
        }
        main_list.setAdapter(new MainListAdapter(this, studentList));
    }
}
