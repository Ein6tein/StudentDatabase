package com.tsi.student_database.models;

import java.util.Date;

public class Student {

    private int id;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String personalCode;
    private int studentCode;
    private String mobilePhone;
    private String email;
    private int faculty;
    private int programme;
    private int level;
    private String group;
    private Address address;

    public Student() {
    }

    public Student(int id, String firstName, String lastName, Date birthDate,
                   String personalCode, int studentCode, String mobilePhone,
                   String email, int faculty, int programme,
                   int level, String group, Address address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.personalCode = personalCode;
        this.studentCode = studentCode;
        this.mobilePhone = mobilePhone;
        this.email = email;
        this.faculty = faculty;
        this.programme = programme;
        this.level = level;
        this.group = group;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPersonalCode() {
        return personalCode;
    }

    public void setPersonalCode(String personalCode) {
        this.personalCode = personalCode;
    }

    public int getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(int studentCode) {
        this.studentCode = studentCode;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getFaculty() {
        return faculty;
    }

    public void setFaculty(int faculty) {
        this.faculty = faculty;
    }

    public int getProgramme() {
        return programme;
    }

    public void setProgramme(int programme) {
        this.programme = programme;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
