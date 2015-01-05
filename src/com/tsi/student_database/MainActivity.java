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
import com.tsi.student_database.database.DatabaseHelper;
import com.tsi.student_database.models.Message;
import com.tsi.student_database.models.Student;
import de.greenrobot.event.EventBus;

import java.util.List;

public class MainActivity extends Activity implements Dialog.OnClickListener {

    public static final int REQUEST_CODE = 1234;

    public static final String STUDENT_ID = "student_id";

    private ListView mainList;

    private MainListAdapter adapter;

    private int selectedOption;
    private int selectedPosition;

    private AlertDialog choiceDialog;
    private AlertDialog deleteDialog;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mainList = (ListView) findViewById(R.id.main_list);

        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;
                deleteDialog = null;
                showChoiceDialog();
            }
        });

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        showContent();
    }

    private void showContent() {
        List<Student> students = DatabaseHelper.getInstance(this).getAllStudentsFromDatabase();
        adapter = new MainListAdapter(this, students);
        mainList.setAdapter(adapter);
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
                Intent i = new Intent(this, AddEditActivity.class);
                startActivity(i);
                return true;
            }
            /*case R.id.action_search: {
                return true;
            }*/
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
                    showContent();
                    Toast.makeText(this, "Changes saved successfully!", Toast.LENGTH_SHORT).show();
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
                if (choiceDialog != null) {
                    if (selectedOption == R.id.dialog_edit) {
                        edit(selectedPosition);
                    } else if (selectedOption == R.id.dialog_delete) {
                        choiceDialog = null;
                        showDeleteDialog();
                    }
                } else if (deleteDialog != null) {
                    Student student = adapter.getItem(selectedPosition);
                    DatabaseHelper.getInstance(this).
                            removeStudentFromDatabase(student.getId(), student.getAddress().getId());
                    showContent();
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
        Intent i = new Intent(this, AddEditActivity.class);
        Student student = adapter.getItem(position);
        Bundle extras = new Bundle();
        extras.putInt(STUDENT_ID, student.getId());
        i.putExtras(extras);
        startActivityForResult(i, REQUEST_CODE);
    }

    private void showChoiceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Select action");
        LayoutInflater inflater = getLayoutInflater();
        View dialogContent = inflater.inflate(R.layout.edit_delete_dialog, null);
        builder.setView(dialogContent);
        choiceDialog = builder.create();
        choiceDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(android.R.string.cancel), this);
        choiceDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(android.R.string.ok), this);
        RadioGroup radioGroup = (RadioGroup) dialogContent.findViewById(R.id.dialog_radiogroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                choiceDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                selectedOption = checkedId;
            }
        });
        choiceDialog.show();
        choiceDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Are you sure?");
        builder.setMessage("All data will be deleted");
        deleteDialog = builder.create();
        deleteDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(android.R.string.cancel), this);
        deleteDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(android.R.string.ok), this);
        deleteDialog.show();
    }
}
