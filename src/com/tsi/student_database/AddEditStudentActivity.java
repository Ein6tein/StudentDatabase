package com.tsi.student_database;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.tsi.student_database.models.Message;
import com.tsi.student_database.models.Student;
import de.greenrobot.event.EventBus;

public class AddEditStudentActivity extends Activity implements View.OnClickListener {

    private EditText firstNameEdit;
    private EditText lastNameEdit;
    private EditText personalCodeEdit;

    private int position = -1;
    private boolean isNewStudent = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_student_activity);

        firstNameEdit = (EditText) findViewById(R.id.first_name_add);
        lastNameEdit = (EditText) findViewById(R.id.last_name_add);
        personalCodeEdit = (EditText) findViewById(R.id.personal_code_add);
        Button cancel = (Button) findViewById(R.id.cancel);
        Button ok = (Button) findViewById(R.id.ok);

        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isNewStudent = false;
            position = extras.getInt(MainActivity.POSITION);
            firstNameEdit.setText(extras.getString(MainActivity.FIRST_NAME));
            lastNameEdit.setText(extras.getString(MainActivity.LAST_NAME));
            personalCodeEdit.setText(extras.getString(MainActivity.PERSONAL_CODE));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok: {
                if (isNewStudent) {
                    EventBus.getDefault().post(
                            new Message(
                                    new Student(
                                            firstNameEdit.getText().toString(),
                                            lastNameEdit.getText().toString(),
                                            personalCodeEdit.getText().toString()),
                                    true)
                    );
                } else {
                    setActivityResult();
                }
                finish();
                break;
            }
            case R.id.cancel: {
                finish();
                break;
            }
        }
    }

    private void setActivityResult() {
        Bundle data = new Bundle();
        data.putInt(MainActivity.POSITION, position);
        data.putString(MainActivity.FIRST_NAME, firstNameEdit.getText().toString());
        data.putString(MainActivity.LAST_NAME, lastNameEdit.getText().toString());
        data.putString(MainActivity.PERSONAL_CODE, personalCodeEdit.getText().toString());
        Intent i = new Intent();
        i.putExtras(data);
        setResult(RESULT_OK, i);
    }
}
