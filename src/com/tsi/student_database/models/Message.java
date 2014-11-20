package com.tsi.student_database.models;

public class Message {

    private Student student;

    private boolean add;

    public Message(Student student, boolean add) {
        this.student = student;
        this.add = add;
    }

    public Student getStudent() {
        return student;
    }

    public boolean isAdd() {
        return add;
    }
}