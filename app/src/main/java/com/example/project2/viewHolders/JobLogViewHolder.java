package com.example.project2.viewHolders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.EditEntryActivity;
import com.example.project2.R;
import com.example.project2.database.entities.JobLog;

import java.time.format.DateTimeFormatter;

/**
 * @author Lesly Villanueva
 * <br>COURSE: CST 338 - Software Design
 * <br>DATE: 12/1/2025
 * <br>ASSIGNMENT: Project 02
 * Description: ViewHolder represents a single job application item within the RecyclerView. It's used by the RecyclerView to bind data.
 * Edit button will send users to the edit activities page and delete button will delete an application entry.
 */
public class JobLogViewHolder extends RecyclerView.ViewHolder {
    private final TextView companyTextView, roleTextView, statusTextView, dateTextView, reminderTextView;
    private final Button editBtn, deleteBtn;

    public JobLogViewHolder(View jobLogView){
        super(jobLogView);
        companyTextView = jobLogView.findViewById(R.id.companyTextViewAppSummary);
        roleTextView = jobLogView.findViewById(R.id.roleTextViewAppSummary);
        statusTextView = jobLogView.findViewById(R.id.statusTextViewAppSummary);
        dateTextView = jobLogView.findViewById(R.id.dateAppliedTextViewAppSummary);
        reminderTextView = jobLogView.findViewById(R.id.reminderTextViewAppSummary);
        editBtn = jobLogView.findViewById(R.id.btnEditAppSummary);
        deleteBtn = jobLogView.findViewById(R.id.btnDeleteAppSummary);
    }

    public void bind(JobLog job, DateTimeFormatter dateFmt, JobLogViewModel viewModel, androidx.lifecycle.LifecycleOwner lifecycleOwner) {
        companyTextView.setText("Company: " + safe(job.getCompany()));
        roleTextView.setText("Role: " + safe(job.getPosition()));

        String status = job.getStatus();
        statusTextView.setText("Status: " + safe(status));

        dateTextView.setText("Date: " + safe(job.getDateApplied().toLocalDate().format(dateFmt)));

        //default hide reminder
        reminderTextView.setVisibility(View.GONE);

        //observe reminder for this job.applicaiton id
        viewModel.getReminderForJob(job.getId()).observe(lifecycleOwner, reminder -> {
            if (reminder != null){
                reminderTextView.setVisibility(View.VISIBLE);
                DateTimeFormatter reminderFmt = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a");
                String text = "Reminder: " + reminder.getNote()
                        + " at " + reminder.getTime().format(reminderFmt);
                reminderTextView.setText(text);
            } else {
                reminderTextView.setVisibility(View.GONE);
            }
        });


        editBtn.setOnClickListener(view -> {
            Context context = itemView.getContext();
            SharedPreferences prefs = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
            int userId = prefs.getInt("userId", -1);
            int jobId = job.getId();
            Intent intent = EditEntryActivity.editEntryIntentFactory(context, userId, jobId);
            context.startActivity(intent);
        });

        deleteBtn.setOnClickListener(view -> {
            viewModel.delete(job);
        });
    }

    private String safe(String text) {
        if(text == null){
            return "";
        } else{
            return text;
        }
    }
}
