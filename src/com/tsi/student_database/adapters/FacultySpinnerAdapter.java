package com.tsi.student_database.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.tsi.student_database.R;

public class FacultySpinnerAdapter extends BaseAdapter {

    private String[] values;
    private Context context;

    public FacultySpinnerAdapter(Context context) {
        this.context = context;
        this.values = new String[4];
        values[0] = context.getString(R.string.select_faculty);
        values[1] = context.getString(R.string.faculty_1);
        values[2] = context.getString(R.string.faculty_2);
        values[3] = context.getString(R.string.faculty_3);
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public String getItem(int position) {
        return values[position];
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
            view = inflater.inflate(R.layout.profile_spinners_row, parent, false);
        }
        TextView text = (TextView) view.findViewById(R.id.spinner_row_text);
        int color;
        if (position == 0) {
            color = context.getResources().getColor(R.color.gray_text);
        } else {
            color = context.getResources().getColor(android.R.color.black);
        }
        text.setTextColor(color);
        text.setText(values[position]);
        return view;
    }
}