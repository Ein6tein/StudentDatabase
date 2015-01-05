package com.tsi.student_database;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.tsi.student_database.adapters.FacultySpinnerAdapter;
import com.tsi.student_database.adapters.ProgrammeOrLevelSpinnerAdapter;
import com.tsi.student_database.database.DatabaseHelper;
import com.tsi.student_database.models.Address;
import com.tsi.student_database.models.Student;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddEditActivity extends Activity implements View.OnClickListener,
        AlertDialog.OnClickListener {

    public static final String SHOULD_NOT_BE_EMPTY = "Should not be empty";
    public static final String SHOULD_NOT_CONTAIN_ANY_DIGITS = "Should not contain any digits";
    public static final String SHOULD_BE_OF_PATTERN = "Should be of pattern ######-#####";
    private static final String DATE_FORMAT = "dd MMM yyyy";
    public static final String PLEASE_CHECK_THE_DATE = "Please, check the date";

    private EditText firstNameEdit;
    private EditText lastNameEdit;
    private EditText birthDateEdit;
    private EditText personalCodeEdit;
    private EditText studentCodeEdit;
    private EditText mobilePhoneEdit;
    private EditText emailEdit;
    private EditText groupCodeEdit;

    private EditText countryEdit;
    private EditText stateOrProvinceEdit;
    private EditText cityOrTownEdit;
    private EditText zipCodeEdit;
    private EditText streetEdit;
    private EditText flatNumberEdit;

    private Spinner facultySpinner;
    private Spinner programmeSpinner;
    private Spinner levelSpinner;

    private DateFormat df;

    private boolean isNewStudent = true;

    private Date selectedDate;

    private ProgrammeOrLevelSpinnerAdapter programmeSpinnerAdapter;
    private int studentId;
    private Student student;

    private AlertDialog exitDialog;
    private AlertDialog deleteDialog;
    private ScrollView parentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_student_activity);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        df = new SimpleDateFormat(DATE_FORMAT);

        setupViews();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isNewStudent = false;
            studentId = extras.getInt(MainActivity.STUDENT_ID);
            showContent();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_edit_activity_actions, menu);
        if (isNewStudent) {
            menu.getItem(menu.size() - 1).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete: {
                exitDialog = null;
                showDeleteDialog();
                return true;
            }
            case android.R.id.home: {
                deleteDialog = null;
                showExitDialog();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void setupViews() {
        parentView = (ScrollView) findViewById(R.id.parent_view);

        firstNameEdit = (EditText) findViewById(R.id.first_name);
        lastNameEdit = (EditText) findViewById(R.id.last_name);
        birthDateEdit = (EditText) findViewById(R.id.birth_date);
        personalCodeEdit = (EditText) findViewById(R.id.personal_code);
        studentCodeEdit = (EditText) findViewById(R.id.student_code);
        mobilePhoneEdit = (EditText) findViewById(R.id.mobile_phone);
        emailEdit = (EditText) findViewById(R.id.email);
        facultySpinner = (Spinner) findViewById(R.id.faculty);
        programmeSpinner = (Spinner) findViewById(R.id.programme);
        levelSpinner = (Spinner) findViewById(R.id.level);
        groupCodeEdit = (EditText) findViewById(R.id.group);

        countryEdit = (EditText) findViewById(R.id.country);
        stateOrProvinceEdit = (EditText) findViewById(R.id.state_or_province);
        cityOrTownEdit = (EditText) findViewById(R.id.town);
        zipCodeEdit = (EditText) findViewById(R.id.zip_code);
        streetEdit = (EditText) findViewById(R.id.street);
        flatNumberEdit = (EditText) findViewById(R.id.flat);

        birthDateEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    final Calendar c = Calendar.getInstance();
                    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            birthDateEdit.setText(df.format(c.getTime()));

                            int yearNow = c.get(Calendar.YEAR);
                            int monthNow = c.get(Calendar.MONTH);
                            int dayNow = c.get(Calendar.DAY_OF_MONTH);

                            if (year > yearNow
                                    || (year == yearNow && monthOfYear > monthNow)
                                    || (year == yearNow && monthOfYear == monthNow && dayOfMonth > dayNow)) {
                                birthDateEdit.setError(PLEASE_CHECK_THE_DATE);
                            } else {
                                Calendar c = Calendar.getInstance();
                                c.set(year, monthOfYear, dayOfMonth);
                                selectedDate = c.getTime();
                                birthDateEdit.setText(df.format(c.getTime()));
                                birthDateEdit.setError(null);
                            }
                        }
                    };
                    DatePickerDialog d = new DatePickerDialog(AddEditActivity.this, listener,
                            c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                    d.show();
                }
            }
        });

        facultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    programmeSpinner.setVisibility(View.GONE);
                    levelSpinner.setVisibility(View.GONE);
                } else {
                    String[] array = null;
                    switch (position) {
                        case 1: {
                            array = getResources().getStringArray(R.array.programmes_csat);
                            break;
                        }
                        case 2: {
                            array = getResources().getStringArray(R.array.programmes_mae);
                            break;
                        }
                        case 3: {
                            array = getResources().getStringArray(R.array.programmes_tal);
                            break;
                        }
                    }
                    programmeSpinnerAdapter = new ProgrammeOrLevelSpinnerAdapter(
                            AddEditActivity.this, array, R.string.select_programme);
                    programmeSpinner.setAdapter(programmeSpinnerAdapter);
                    programmeSpinner.setVisibility(View.VISIBLE);
                    programmeSpinner.setSelection(0);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });

        programmeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    levelSpinner.setVisibility(View.GONE);
                } else {
                    levelSpinner.setVisibility(View.VISIBLE);
                    levelSpinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });

        facultySpinner.setAdapter(new FacultySpinnerAdapter(this));
        String[] array = getResources().getStringArray(R.array.levels);
        levelSpinner.setAdapter(new ProgrammeOrLevelSpinnerAdapter(this, array, R.string.select_level));

        Button cancel = (Button) findViewById(R.id.cancel);
        Button ok = (Button) findViewById(R.id.ok);

        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    private void showContent() {
        student = DatabaseHelper.getInstance(this).getStudentFromDatabase(studentId);
        firstNameEdit.setText(student.getFirstName());
        lastNameEdit.setText(student.getLastName());
        birthDateEdit.setText(df.format(student.getBirthDate()));
        personalCodeEdit.setText(student.getPersonalCode());
        studentCodeEdit.setText(String.valueOf(student.getStudentCode()));
        mobilePhoneEdit.setText(student.getMobilePhone());
        emailEdit.setText(student.getEmail());
        facultySpinner.setSelection(student.getFaculty());
        programmeSpinner.setSelection(student.getProgramme());
        levelSpinner.setSelection(student.getLevel());
        groupCodeEdit.setText(student.getGroup());

        Address address = student.getAddress();
        countryEdit.setText(address.getCountry());
        stateOrProvinceEdit.setText(address.getStateOrProvince());
        cityOrTownEdit.setText(address.getTown());
        zipCodeEdit.setText(address.getZipCode());
        streetEdit.setText(address.getStreet());
        flatNumberEdit.setText(String.valueOf(address.getFlat()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok: {
                if (isNewStudent) {
                    if (checkAllFields()) {
                        saveStudent();
                    } else {
                        break;
                    }
                } else {
                    if (checkAllFields()) {
                        updateStudent();
                    } else {
                        break;
                    }
                    setResult(RESULT_OK);
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

    private boolean checkAllFields() {
        if (!checkIsTextFieldValid(firstNameEdit)) {
            parentView.smoothScrollTo(0, (int) firstNameEdit.getY());
            return false;
        }
        if (!checkIsTextFieldValid(lastNameEdit)) {
            parentView.smoothScrollTo(0, (int) lastNameEdit.getY());
            return false;
        }
        if (birthDateEdit.getError() != null) {
            parentView.smoothScrollTo(0, (int) birthDateEdit.getY());
            return false;
        }
        if (!checkIsFieldValid(birthDateEdit)) {
            parentView.smoothScrollTo(0, (int) birthDateEdit.getY());
            return false;
        }
        if (!checkIsPersonalCodeValid()) {
            parentView.smoothScrollTo(0, (int) personalCodeEdit.getY());
            return false;
        }
        if (!checkIsFieldValid(studentCodeEdit)) {
            parentView.smoothScrollTo(0, (int) studentCodeEdit.getY());
            return false;
        }
        if (!checkIsSpinnerValid(facultySpinner)) {
            parentView.smoothScrollTo(0, (int) facultySpinner.getY());
            return false;
        }
        if (!checkIsSpinnerValid(programmeSpinner)) {
            parentView.smoothScrollTo(0, (int) programmeSpinner.getY());
            return false;
        }
        if (!checkIsSpinnerValid(levelSpinner)) {
            parentView.smoothScrollTo(0, (int) levelSpinner.getY());
            return false;
        }
        if (!checkIsFieldValid(groupCodeEdit)) {
            parentView.smoothScrollTo(0, (int) groupCodeEdit.getY());
            return false;
        }
        if (!checkIsTextFieldValid(countryEdit)) {
            parentView.smoothScrollTo(0, (int) countryEdit.getY());
            return false;
        }
        if (!checkIsTextFieldValid(cityOrTownEdit)) {
            parentView.smoothScrollTo(0, (int) cityOrTownEdit.getY());
            return false;
        }
        if (!checkIsFieldValid(zipCodeEdit)) {
            parentView.smoothScrollTo(0, (int) zipCodeEdit.getY());
            return false;
        }
        if (!checkIsFieldValid(streetEdit)) {
            parentView.smoothScrollTo(0, (int) streetEdit.getY());
            return false;
        }
        return true;
    }

    private boolean checkIsTextFieldValid(EditText editText) {
        if (editText.getText() == null || editText.getText().toString().trim().isEmpty()) {
            editText.setError(SHOULD_NOT_BE_EMPTY);
            return false;
        } else {
            Pattern p = Pattern.compile("[0-9]");
            Matcher m = p.matcher(editText.getText().toString());
            if (m.find()) {
                editText.setError(SHOULD_NOT_CONTAIN_ANY_DIGITS);
                return false;
            } else {
                editText.setError(null);
            }
        }
        return true;
    }

    private boolean checkIsPersonalCodeValid() {
        if (personalCodeEdit.getText() == null || personalCodeEdit.getText().toString().trim().isEmpty()) {
            personalCodeEdit.setError(SHOULD_NOT_BE_EMPTY);
            return false;
        } else {
            Pattern p = Pattern.compile("[0-9]{6}-[0-9]{5}");
            Matcher m = p.matcher(personalCodeEdit.getText().toString());
            if (!m.find()) {
                personalCodeEdit.setError(SHOULD_BE_OF_PATTERN);
                return false;
            } else {
                personalCodeEdit.setError(null);
            }
        }
        return true;
    }

    private boolean checkIsFieldValid(EditText editText) {
        if (editText.getText() == null || editText.getText().toString().trim().isEmpty()) {
            editText.setError(SHOULD_NOT_BE_EMPTY);
            return false;
        } else {
            editText.setError(null);
        }
        return true;
    }

    private boolean checkIsSpinnerValid(Spinner spinner) {
        if (spinner.getSelectedItemPosition() == 0) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Error")
               .setMessage("Please, make sure You selected all fields correctly")
               .setCancelable(false)
               .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                   }
               });
            adb.create().show();
            return false;
        }
        return true;
    }

    private void saveStudent() {
        String flat = flatNumberEdit.getText().toString();
        if (flat.trim().isEmpty()) {
            flat = "-1";
        }
        Address address = new Address(-1,
                countryEdit.getText().toString(),
                stateOrProvinceEdit.getText().toString(),
                cityOrTownEdit.getText().toString(),
                streetEdit.getText().toString(),
                zipCodeEdit.getText().toString(),
                Integer.parseInt(flat)
        );
        Student student = new Student(-1,
                firstNameEdit.getText().toString(),
                lastNameEdit.getText().toString(),
                selectedDate,
                personalCodeEdit.getText().toString(),
                Integer.parseInt(studentCodeEdit.getText().toString()),
                mobilePhoneEdit.getText().toString(),
                emailEdit.getText().toString(),
                facultySpinner.getSelectedItemPosition(),
                programmeSpinner.getSelectedItemPosition(),
                levelSpinner.getSelectedItemPosition(),
                groupCodeEdit.getText().toString(),
                address
        );
        DatabaseHelper.getInstance(this).addStudentToDatabase(student);
    }

    private void updateStudent() {
        Address address = student.getAddress();
        address.setCountry(countryEdit.getText().toString());
        address.setStateOrProvince(stateOrProvinceEdit.getText().toString());
        address.setTown(cityOrTownEdit.getText().toString());
        address.setZipCode(zipCodeEdit.getText().toString());
        address.setStreet(streetEdit.getText().toString());
        address.setFlat(Integer.parseInt(flatNumberEdit.getText().toString()));

        student.setFirstName(firstNameEdit.getText().toString());
        student.setLastName(lastNameEdit.getText().toString());
        student.setBirthDate(selectedDate);
        student.setPersonalCode(personalCodeEdit.getText().toString());
        student.setStudentCode(Integer.parseInt(studentCodeEdit.getText().toString()));
        student.setMobilePhone(mobilePhoneEdit.getText().toString());
        student.setEmail(emailEdit.getText().toString());
        student.setFaculty(facultySpinner.getSelectedItemPosition());
        student.setProgramme(programmeSpinner.getSelectedItemPosition());
        student.setLevel(levelSpinner.getSelectedItemPosition());
        student.setGroup(groupCodeEdit.getText().toString());
        student.setAddress(address);
        DatabaseHelper.getInstance(this).updateExistingStudent(student);
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Are you sure?");
        builder.setMessage("All changes will be lost");
        exitDialog = builder.create();
        exitDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(android.R.string.cancel), this);
        exitDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(android.R.string.ok), this);
        exitDialog.show();
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

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case AlertDialog.BUTTON_POSITIVE: {
                if (deleteDialog != null) {
                    DatabaseHelper.getInstance(this).
                            removeStudentFromDatabase(studentId, student.getAddress().getId());
                    setResult(RESULT_OK);
                }
                onBackPressed();
                dialog.dismiss();
                break;
            }
            case AlertDialog.BUTTON_NEGATIVE: {
                dialog.cancel();
                break;
            }
        }
    }
}