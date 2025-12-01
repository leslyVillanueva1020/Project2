package com.example.project2.viewHolders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.R;
import com.example.project2.database.entities.JobLog;

import java.time.format.DateTimeFormatter;

/**
 * @author Lesly Villanueva
 * <br>COURSE: CST 338 - Software Design
 * <br>DATE: 12/1/2025
 * <br>ASSIGNMENT: Project 02
 * Description: ViewHolder represents a single job application item within the RecyclerView. It's used by the RecyclerView to bind data.
 */
public class JobLogViewHolder extends RecyclerView.ViewHolder {
    private final TextView companyTextView, roleTextView, statusTextView, dateTextView, reminderTextView;
    private final Button detailsBtn, editBtn, deleteBtn;

    public JobLogViewHolder(View jobLogView){
        super(jobLogView);
        companyTextView = jobLogView.findViewById(R.id.companyTextViewAppSummary);
        roleTextView = jobLogView.findViewById(R.id.roleTextViewAppSummary);
        statusTextView = jobLogView.findViewById(R.id.statusTextViewAppSummary);
        dateTextView = jobLogView.findViewById(R.id.dateAppliedTextViewAppSummary);
        reminderTextView = jobLogView.findViewById(R.id.reminderTextViewAppSummary);
        detailsBtn = jobLogView.findViewById(R.id.btnViewDetailAppSummary);
        editBtn = jobLogView.findViewById(R.id.btnEditAppSummary);
        deleteBtn = jobLogView.findViewById(R.id.btnDeleteAppSummary);
    }

    public void bind(JobLog job, DateTimeFormatter dateFmt) {
        companyTextView.setText("Company: " + safe(job.getCompany()));
        roleTextView.setText("Role: " + safe(job.getPosition()));

        String status = job.getStatus();
        statusTextView.setText("Status: " + safe(status));

        dateTextView.setText("Date: " + safe(job.getDateApplied().toLocalDate().format(dateFmt)));

        //TODO: IF THERE IS A REMINDER SHOW IT
        // if (job.getNextReminder() != null { set view to visible else gone }


        //TODO: do something with buttons here??? or somewhere else not sure
        detailsBtn.setOnClickListener(view -> {
            //just to see if button works
            Toast.makeText(view.getContext(), "Details of " + job.getCompany(), Toast.LENGTH_SHORT).show();
            //TODO: will later start a new DETAILS activity
            //something like:
            //intent i = new Intent(view.getContext(), ApplicationDetailsActivity.class);
            //i.putExtra("JOB_ID", job.getId());
            //v.getContext().startActivity(i);
        });

        editBtn.setOnClickListener(view -> {
            Toast.makeText(view.getContext(), "Edit " + job.getCompany(), Toast.LENGTH_SHORT).show();
            //TODO: will later start new EDIT activity
        });

        deleteBtn.setOnClickListener(view -> {
            Toast.makeText(view.getContext(), "Delete " + job.getCompany(), Toast.LENGTH_SHORT).show();
            //TODO: will later delete application
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
