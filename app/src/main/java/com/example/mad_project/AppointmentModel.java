package com.example.mad_project;

public class AppointmentModel {

    private int id;
    private String firstName, lastName, email, date, time, subject;



    public AppointmentModel(int id,String firstName, String lastName, String email, String date, String time, String subject) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.date = date;
        this.time = time;
        this.subject = subject;

    }


    public int getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getSubject() {
        return subject;
    }

}