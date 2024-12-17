package com.example.mad_project;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class appointment extends AppCompatActivity {

    private EditText firstName, lastName, email, mobile, date, time, subject, address;
    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        // Initialize views
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        subject = findViewById(R.id.subject);
        address = findViewById(R.id.address);

        Button bookAppointmentBtn = findViewById(R.id.bookAppointmentBtn);
        Button viewBookedAppointmentsBtn = findViewById(R.id.viewBookedAppointmentsBtn);

        dbHelper = new DatabaseHelper(this);

        date.setOnClickListener(v -> showDatePicker());

        time.setOnClickListener(v -> showTimePicker());

        bookAppointmentBtn.setOnClickListener(v -> bookAppointment());

        viewBookedAppointmentsBtn.setOnClickListener(v -> viewBookedAppointments());

    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            // month start from 1
            String formattedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
            date.setText(formattedDate);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, selectedHour, selectedMinute) -> {
            String formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
            time.setText(formattedTime);
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private void bookAppointment() {
        String fName = firstName.getText().toString();
        String lName = lastName.getText().toString();
        String emailText = email.getText().toString();
        String mobileText = mobile.getText().toString();
        String addressText = address.getText().toString();
        String dateText = date.getText().toString();
        String timeText = time.getText().toString();
        String subjectText = subject.getText().toString();


        if (fName.isEmpty() || lName.isEmpty() || emailText.isEmpty() || mobileText.isEmpty() || addressText.isEmpty()
              || dateText.isEmpty() || timeText.isEmpty() || subjectText.isEmpty()) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isInserted = dbHelper.addAppointment(fName, lName, emailText, mobileText,addressText, dateText, timeText, subjectText, userId);

        if (isInserted) {
            Toast.makeText(this, "Appointment Booked Successfully!", Toast.LENGTH_SHORT).show();
            clear();
        } else {
            Toast.makeText(this, "Failed to Book Appointment.", Toast.LENGTH_SHORT).show();
        }
    }

    private void viewBookedAppointments() {
         Intent intent = new Intent(this, viewappointment.class);
         startActivity(intent);
        Toast.makeText(this, "View Booked Appointments Clicked!", Toast.LENGTH_SHORT).show();
    }

    public void clear() {
        firstName.setText("");
        lastName.setText("");
        email.setText("");
        mobile.setText("");
        date.setText("");
        time.setText("");
        subject.setText("");
        address.setText("");
    }

}
