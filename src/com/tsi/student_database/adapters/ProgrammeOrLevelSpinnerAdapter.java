package com.tsi.student_database.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.tsi.student_database.R;

public class ProgrammeOrLevelSpinnerAdapter extends BaseAdapter {

    private String[] values;
    private Context context;
    private String firstRow;

    public ProgrammeOrLevelSpinnerAdapter(Context context, String[] values, int firstRowId) {
        this.context = context;
        this.values = values;
        firstRow = context.getResources().getString(firstRowId);
    }

    @Override
    public int getCount() {
        return values.length + 1;
    }

    @Override
    public String getItem(int position) {
        if (position == 0) {
            return firstRow;
        }
        return values[position - 1];
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
        text.setText(getItem(position));
        return view;
    }
}