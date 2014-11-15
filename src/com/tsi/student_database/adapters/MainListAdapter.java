package com.tsi.student_database.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.tsi.student_database.R;
import com.tsi.student_database.models.Student;

import java.util.List;

public class MainListAdapter extends BaseAdapter {

    private Context context;
    private List<Student> studentList;

    public MainListAdapter(Context context, List<Student> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    @Override
    public int getCount() {
        return studentList.size();
    }

    @Override
    public Student getItem(int position) {
        return studentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.main_list_row, parent, false);
        }

        if (position % 2 == 0) {
            view.setBackgroundColor(context.getResources().getColor(R.color.even_row_bg));
        } else {
            view.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        }

        TextView idText = (TextView) view.findViewById(R.id.row_id);
        TextView firstNameText = (TextView) view.findViewById(R.id.first_name);
        TextView lastNameText = (TextView) view.findViewById(R.id.last_name);
        TextView personalCodeText = (TextView) view.findViewById(R.id.personal_code);

        Student student = getItem(position);
        idText.setText(String.valueOf(position + 1));
        firstNameText.setText(student.getFirstName());
        lastNameText.setText(student.getLastName());
        personalCodeText.setText(student.getPersonalCode());

        return view;
    }
}
