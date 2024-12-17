package com.example.mad_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class viewappointment extends AppCompatActivity {



    private EditText appointmentIdInput;
    private Button searchButton;
    private Button logout;
    private TextView appointmentDetails;
    private DatabaseHelper dbHelper;

    private RecyclerView recyc;
    private DatabaseHelper db;
    private List<AppointmentModel> appointmentList;
    private AppointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewappointment);

        appointmentIdInput = findViewById(R.id.appointmentIdInput);
        searchButton = findViewById(R.id.searchButton);
        logout = findViewById(R.id.logout);
        appointmentDetails = findViewById(R.id.appointmentDetails);
        dbHelper = new DatabaseHelper(this);

        recyc = findViewById(R.id.recyc1);
        recyc.setLayoutManager(new LinearLayoutManager(this));
        db = new DatabaseHelper(this);
        appointmentList = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);

        fetchAppointments(userId);

        adapter = new AppointmentAdapter(appointmentList,dbHelper,this);
        recyc.setAdapter(adapter);



        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appointmentIdStr = appointmentIdInput.getText().toString().trim();
                if (appointmentIdStr.isEmpty()) {
                    Toast.makeText(viewappointment.this, "Please enter an appointment ID", Toast.LENGTH_SHORT).show();
                } else {
                    int appointmentId = Integer.parseInt(appointmentIdStr);
                    filterAppointmentsById(appointmentId);
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewappointment.this, register.class);
                startActivity(intent);
                Toast.makeText(viewappointment.this, "Logged Out!!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
    private void fetchAppointments(int userId) {
        Cursor cursor = db.getAppointmentsByUserId(userId);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int _appid = cursor.getInt(cursor.getColumnIndexOrThrow("appointment_id"));
                String firstName = cursor.getString(cursor.getColumnIndex("first_name"));
                String lastName = cursor.getString(cursor.getColumnIndex("last_name"));
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String subject = cursor.getString(cursor.getColumnIndex("subject"));

                appointmentList.add(new AppointmentModel(_appid, firstName, lastName, email, date, time, subject));
            } while (cursor.moveToNext());
            cursor.close();
            // adapter.notifyDataSetChanged();
        } else {
            if (cursor != null) {
                cursor.close();
            }
            Toast.makeText(this, "No appointments found", Toast.LENGTH_SHORT).show();
        }
    }


    private void filterAppointmentsById(int appointmentId) {
        Cursor cursor = dbHelper.getAppointmentById(appointmentId);
        if (cursor != null && cursor.moveToFirst()) {
            appointmentList.clear();
            do {
                int _appid = cursor.getInt(cursor.getColumnIndexOrThrow("appointment_id"));
                String firstName = cursor.getString(cursor.getColumnIndex("first_name"));
                String lastName = cursor.getString(cursor.getColumnIndex("last_name"));
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String subject = cursor.getString(cursor.getColumnIndex("subject"));

                appointmentList.add(new AppointmentModel(_appid, firstName, lastName, email, date, time, subject));
            } while (cursor.moveToNext());
            cursor.close();
            adapter.notifyDataSetChanged();
        } else {
            if (cursor != null) {
                cursor.close();
            }
            Toast.makeText(this, "No appointment found with ID: " + appointmentId, Toast.LENGTH_SHORT).show();
        }
    }

}
