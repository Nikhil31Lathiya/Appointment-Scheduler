package com.example.mad_project;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.MyViewHolder> {

    private List<AppointmentModel> appointmentList;
    DatabaseHelper db;

    Context context;

    public AppointmentAdapter(List<AppointmentModel> appointmentList, DatabaseHelper db, Context context) {
        this.appointmentList = appointmentList;
        this.db = db;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_item_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AppointmentModel appointment = appointmentList.get(position);
        holder.firstName.setText(appointment.getFirstName());
        holder.lastName.setText(appointment.getLastName());
        holder.email.setText(appointment.getEmail());
        holder.date.setText(appointment.getDate());
        holder.time.setText(appointment.getTime());
        holder.subject.setText(appointment.getSubject());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    int recid = appointmentList.get(position).getId();

                    new AlertDialog.Builder(context)
                            .setTitle("Delete Appointment")
                            .setMessage("Are you sure you want to delete this appointment?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (db.deleteAppointment(recid)) {
                                        appointmentList.remove(position);
                                        notifyItemRemoved(position);
                                        Toast.makeText(context, "Deleted.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Delete failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
            }
        });


        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_update_appointment, null);
                builder.setView(dialogView);

                EditText updateFirstName = dialogView.findViewById(R.id.updateFirstName);
                EditText updateLastName = dialogView.findViewById(R.id.updateLastName);
                EditText updateEmail = dialogView.findViewById(R.id.updateEmail);
                EditText updateDate = dialogView.findViewById(R.id.updateDate);
                EditText updateTime = dialogView.findViewById(R.id.updateTime);
                EditText updateSubject = dialogView.findViewById(R.id.updateSubject);
                Button updateButton = dialogView.findViewById(R.id.updateButton);

                updateFirstName.setText(appointment.getFirstName());
                updateLastName.setText(appointment.getLastName());
                updateEmail.setText(appointment.getEmail());
                updateDate.setText(appointment.getDate());
                updateTime.setText(appointment.getTime());
                updateSubject.setText(appointment.getSubject());


                AlertDialog dialog = builder.create();
                dialog.show();


                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String newFirstName = updateFirstName.getText().toString();
                        String newLastName = updateLastName.getText().toString();
                        String newEmail = updateEmail.getText().toString();
                        String newDate = updateDate.getText().toString();
                        String newTime = updateTime.getText().toString();
                        String newSubject = updateSubject.getText().toString();


                        boolean isUpdated = db.updateAppointment(appointment.getId(), newFirstName, newLastName, newEmail, newDate, newTime, newSubject);
                        if (isUpdated) {

                            appointmentList.set(position, new AppointmentModel(appointment.getId(), newFirstName, newLastName, newEmail, newDate, newTime, newSubject));
                            notifyItemChanged(position);
                            Toast.makeText(context, "Appointment Updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Update Failed", Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();
                    }
                });
            }
        });

    }
    @Override
    public int getItemCount() {
        return appointmentList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView firstName, lastName, email, date, time, subject;
        Button delete,update;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.firstName1);
            lastName = itemView.findViewById(R.id.lastName1);
            email = itemView.findViewById(R.id.email1);
            date = itemView.findViewById(R.id.date1);
            time = itemView.findViewById(R.id.time1);
            subject = itemView.findViewById(R.id.subject1);
            delete = itemView.findViewById(R.id.delete);
            update = itemView.findViewById(R.id.update);
        }
    }
}
