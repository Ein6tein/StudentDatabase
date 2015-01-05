package com.tsi.student_database.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.tsi.student_database.models.Address;
import com.tsi.student_database.models.Student;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper dbHelperInstance;

    private static final String DATABASE_NAME = "Student_Database";
    private static final int DATABASE_VERSION = 1;

    private static final String STUDENT_TABLE_NAME = "students";
    private static final String ADDRESS_TABLE_NAME = "addresses";

    private static final String ID = "_id";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String BIRTH_DATE = "birth_date";
    private static final String PERSONAL_CODE = "personal_code";
    public static final String STUDENT_CODE = "student_code";
    private static final String MOBILE_PHONE = "mobile_phone";
    private static final String EMAIL = "email";
    private static final String ADDRESS_ID = "address_id";
    private static final String FACULTY = "faculty";
    private static final String PROGRAMME = "programme";
    private static final String LEVEL = "level";
    private static final String GROUP = "group_code";

    private static final String COUNTRY = "country";
    private static final String STATE_PROVINCE = "state_province";
    private static final String TOWN = "town";
    private static final String STREET = "street";
    private static final String ZIP_CODE = "zip_code";
    private static final String FLAT = "flat";

    private static final String STUDENT_TABLE = "CREATE TABLE " + STUDENT_TABLE_NAME + "("
            + ID + " integer primary key autoincrement, "
            + FIRST_NAME + " text not null, "
            + LAST_NAME + " text not null, "
            + BIRTH_DATE + " long not null, "
            + PERSONAL_CODE + " text not null, "
            + STUDENT_CODE + " integer not null, "
            + MOBILE_PHONE + " text, "
            + EMAIL + " text, "
            + FACULTY + " integer not null, "
            + PROGRAMME + " integer not null, "
            + LEVEL + " integer not null, "
            + GROUP + " text not null, "
            + ADDRESS_ID + " integer, "
            + "FOREIGN KEY (" + ADDRESS_ID + ") REFERENCES " + ADDRESS_TABLE_NAME + "(" + ID + ")"
            + ");";

    private static final String ADDRESS_TABLE = "CREATE TABLE " + ADDRESS_TABLE_NAME + "("
            + ID + " integer primary key autoincrement, "
            + COUNTRY + " text not null, "
            + STATE_PROVINCE + " text, "
            + TOWN + " text not null, "
            + ZIP_CODE + " text not null, "
            + STREET + " text not null, "
            + FLAT + " integer"
            + ");";

    protected DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(Context context) {
        if (dbHelperInstance == null) {
            dbHelperInstance = new DatabaseHelper(context);
        }
        return dbHelperInstance;
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(ADDRESS_TABLE);
        database.execSQL(STUDENT_TABLE);
    }

    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion){
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS ");
        onCreate(database);
    }

    public void addStudentToDatabase(Student student) {
        SQLiteDatabase database = dbHelperInstance.getWritableDatabase();
        Address address = student.getAddress();
        database.execSQL("INSERT INTO " + ADDRESS_TABLE_NAME
                + " VALUES("
                + "NULL,\""
                + address.getCountry() + "\","
                + (!address.getStateOrProvince().trim().isEmpty()
                     ? "\"" + address.getStateOrProvince() + "\"" : "NULL") + ",\""
                + address.getTown() + "\",\""
                + address.getZipCode() + "\",\""
                + address.getStreet() + "\","
                + (address.getFlat() != -1 ? address.getFlat() : "NULL")
                + ");");
        Cursor cursor = database.query(ADDRESS_TABLE_NAME, new String[]{ID}, ID, null, null, null, ID + " DESC", "1");
        cursor.moveToFirst();
        int addressId = cursor.getInt(0);
        database.execSQL("INSERT INTO " + STUDENT_TABLE_NAME
                + " VALUES("
                + "NULL,\""
                + student.getFirstName() + "\",\""
                + student.getLastName() + "\","
                + student.getBirthDate().getTime() + ",\""
                + student.getPersonalCode() + "\","
                + student.getStudentCode() + ","
                + (!student.getMobilePhone().trim().isEmpty() ? "\"" + student.getMobilePhone() + "\"" : "NULL") + ","
                + (!student.getEmail().trim().isEmpty() ? "\"" + student.getEmail() + "\"" : "NULL") + ","
                + student.getFaculty() + ","
                + student.getProgramme() + ","
                + student.getLevel() + ",\""
                + student.getGroup() + "\","
                + addressId
                + ");");
        database.close();
    }

    public void updateExistingStudent(Student student) {
        SQLiteDatabase database = dbHelperInstance.getWritableDatabase();
        Address address = student.getAddress();
        database.execSQL("UPDATE " + ADDRESS_TABLE_NAME
                + " SET "
                + COUNTRY + "=\"" + address.getCountry() + "\", "
                + STATE_PROVINCE + "=" + (!address.getStateOrProvince().trim().isEmpty()
                                        ? "\"" + address.getStateOrProvince() + "\"" : "NULL") + ", "
                + TOWN + "=\"" + address.getTown() + "\", "
                + ZIP_CODE + "=\"" + address.getZipCode() + "\", "
                + STREET + "=\"" + address.getStreet() + "\", "
                + FLAT + "=" + (address.getFlat() != -1 ? address.getFlat() : "NULL")
                + " WHERE " + ID + "=" + address.getId() + ";");
        database.execSQL("UPDATE " + STUDENT_TABLE_NAME
                + " SET "
                + FIRST_NAME + "=\"" + student.getFirstName() + "\", "
                + LAST_NAME + "=\"" + student.getLastName() + "\", "
                + BIRTH_DATE + "=" + student.getBirthDate().getTime() + ", "
                + PERSONAL_CODE + "=\"" + student.getPersonalCode() + "\", "
                + STUDENT_CODE + "=" + student.getStudentCode() + ", "
                + MOBILE_PHONE + "=" + (!student.getMobilePhone().trim().isEmpty()
                                          ? "\"" + student.getMobilePhone() + "\"" : "NULL") + ", "
                + EMAIL + "=" + (!student.getEmail().trim().isEmpty()
                                    ? "\"" + student.getEmail() + "\"" : "NULL") + ", "
                + FACULTY + "=" + student.getFaculty() + ", "
                + PROGRAMME + "=" + student.getProgramme() + ", "
                + LEVEL + "=" + student.getLevel() + ", "
                + GROUP + "=\"" + student.getGroup() + "\", "
                + ADDRESS_ID + "=" + address.getId()
                + " WHERE " + ID + "=" + student.getId() + ";");
        database.close();
    }

    public List<Student> getAllStudentsFromDatabase() {
        List<Student> list = new ArrayList<Student>();
        SQLiteDatabase database = dbHelperInstance.getReadableDatabase();
        Cursor query = database.query(STUDENT_TABLE_NAME, new String[]{
                ID, FIRST_NAME, LAST_NAME, BIRTH_DATE, PERSONAL_CODE, STUDENT_CODE,
                MOBILE_PHONE, EMAIL, FACULTY, PROGRAMME, LEVEL, GROUP, ADDRESS_ID
        }, null, null, null, null, null);
        while (query.moveToNext()) {
            list.add(getStudentFromCursor(database, query));
        }
        database.close();
        return list;
    }

    public Student getStudentFromDatabase(int id) {
        SQLiteDatabase database = dbHelperInstance.getReadableDatabase();
        Cursor query = database.query(STUDENT_TABLE_NAME, new String[]{
                ID, FIRST_NAME, LAST_NAME, BIRTH_DATE, PERSONAL_CODE, STUDENT_CODE,
                MOBILE_PHONE, EMAIL, FACULTY, PROGRAMME, LEVEL, GROUP, ADDRESS_ID
        }, ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (query.moveToFirst()) {
            Student student = getStudentFromCursor(database, query);
            database.close();
            return student;
        }
        return null;
    }

    private Student getStudentFromCursor(SQLiteDatabase database, Cursor query) {
        Student student = new Student(
                query.getInt(query.getColumnIndex(ID)),
                query.getString(query.getColumnIndex(FIRST_NAME)),
                query.getString(query.getColumnIndex(LAST_NAME)),
                null,
                query.getString(query.getColumnIndex(PERSONAL_CODE)),
                query.getInt(query.getColumnIndex(STUDENT_CODE)),
                query.getString(query.getColumnIndex(MOBILE_PHONE)),
                query.getString(query.getColumnIndex(EMAIL)),
                query.getInt(query.getColumnIndex(FACULTY)),
                query.getInt(query.getColumnIndex(PROGRAMME)),
                query.getInt(query.getColumnIndex(LEVEL)),
                query.getString(query.getColumnIndex(GROUP)),
                null);
        Date date = new Date(query.getLong(query.getColumnIndex(BIRTH_DATE)));
        student.setBirthDate(date);
        int addressId = query.getInt(query.getColumnIndex(ADDRESS_ID));
        Cursor addressQuery = database.query(ADDRESS_TABLE_NAME, new String[]{
                ID, COUNTRY, STATE_PROVINCE, TOWN, ZIP_CODE, STREET, FLAT
        }, ID + "=?", new String[]{String.valueOf(addressId)}, null, null, null);
        if (addressQuery.moveToFirst()) {
            Address address = new Address(
                    addressQuery.getInt(addressQuery.getColumnIndex(ID)),
                    addressQuery.getString(addressQuery.getColumnIndex(COUNTRY)),
                    addressQuery.getString(addressQuery.getColumnIndex(STATE_PROVINCE)),
                    addressQuery.getString(addressQuery.getColumnIndex(TOWN)),
                    addressQuery.getString(addressQuery.getColumnIndex(ZIP_CODE)),
                    addressQuery.getString(addressQuery.getColumnIndex(STREET)),
                    addressQuery.getInt(addressQuery.getColumnIndex(FLAT))
            );
            student.setAddress(address);
        }
        return student;
    }

    public void removeStudentFromDatabase(int studentId, int addressId) {
        SQLiteDatabase database = dbHelperInstance.getWritableDatabase();
        database.execSQL("DELETE FROM " + ADDRESS_TABLE_NAME + " WHERE " + ID + "=" + addressId);
        database.execSQL("DELETE FROM " + STUDENT_TABLE_NAME + " WHERE " + ID + "=" + studentId);
        database.close();
    }
}
