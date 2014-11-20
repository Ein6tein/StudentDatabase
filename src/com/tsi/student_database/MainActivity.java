package com.tsi.student_database;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.tsi.student_database.adapters.MainListAdapter;
import com.tsi.student_database.models.Message;
import com.tsi.student_database.models.Student;
import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements Dialog.OnClickListener {

    public static final int REQUEST_CODE = 1234;

    public static final String POSITION = "postion";

    public static final String FIRST_NAME = "first_name";

    public static final String LAST_NAME = "last_name";

    public static final String PERSONAL_CODE = "personal_code";

    private MainListAdapter adapter;

    private int selectedOption;

    private int selectedPosition;

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
        adapter = new MainListAdapter(this, studentList);
        main_list.setAdapter(adapter);
        main_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;
                AlertDialog dialog = getDialog();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }
        });

        EventBus.getDefault().register(this);
    }

    private AlertDialog getDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Select action");
        LayoutInflater inflater = getLayoutInflater();
        View dialogContent = inflater.inflate(R.layout.edit_delete_dialog, null);
        builder.setView(dialogContent);
        final AlertDialog dialog = builder.create();
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(android.R.string.cancel), this);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(android.R.string.ok), this);
        RadioGroup radioGroup = (RadioGroup) dialogContent.findViewById(R.id.dialog_radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                selectedOption = checkedId;
            }
        });
        return dialog;
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add: {
                Intent i = new Intent(this, AddEditStudentActivity.class);
                startActivity(i);
                return true;
            }
            case R.id.action_search: {
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_OK: {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        int position = extras.getInt(POSITION, -1);
                        if (position > -1) {
                            Student student = adapter.getItem(position);
                            student.setFirstName(extras.getString(FIRST_NAME));
                            student.setLastName(extras.getString(LAST_NAME));
                            student.setPersonalCode(extras.getString(PERSONAL_CODE));
                            adapter.notifyDataSetChanged();
                            Toast.makeText(this, "Changes saved successfully!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                case RESULT_CANCELED: {
                    break;
                }
            }
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case AlertDialog.BUTTON_POSITIVE: {
                if (selectedOption == R.id.dialog_edit) {
                    edit(selectedPosition);
                } else if (selectedOption == R.id.dialog_delete) {
                    delete(selectedPosition);
                }
                dialog.dismiss();
                break;
            }
            case AlertDialog.BUTTON_NEGATIVE: {
                dialog.cancel();
                break;
            }
        }
    }

    public void onEvent(Message message) {
        if (message != null && message.isAdd()) {
            adapter.add(message.getStudent());
        }
    }

    public void edit(int position) {
        Intent i = new Intent(this, AddEditStudentActivity.class);
        Student student = adapter.getItem(position);
        Bundle extras = new Bundle();
        extras.putInt(POSITION, position);
        extras.putString(FIRST_NAME, student.getFirstName());
        extras.putString(LAST_NAME, student.getLastName());
        extras.putString(PERSONAL_CODE, student.getPersonalCode());
        i.putExtras(extras);
        startActivityForResult(i, REQUEST_CODE);
    }

    public void delete(int position) {
        adapter.delete(position);
        Toast.makeText(this, "Deleted successfully!", Toast.LENGTH_SHORT).show();
    }
}
