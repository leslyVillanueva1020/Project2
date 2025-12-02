package com.example.project2.viewHolders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.project2.R;
import com.example.project2.database.entities.JobLog;

import java.time.format.DateTimeFormatter;

/**
 * @author Lesly Villanueva
 * <br>COURSE: CST 338 - Software Design
 * <br>DATE: 12/1/2025
 * <br>ASSIGNMENT: Project 02
 * Description: RecyclerView adapter responsible for managing and displaying a list of job application entries.
 * It creates ViewHolder instances and binds JobLog data to the RecyclerView items.
 * It serves as the connection between the underlyinh job application data and the UI elements that present it.
 */
public class JobLogAdapter extends ListAdapter<JobLog, JobLogViewHolder> {

    private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("MMM d, yyyy");

    public JobLogAdapter(@NonNull DiffUtil.ItemCallback<JobLog> diffCallBack){
        super(diffCallBack);
    }

    @NonNull
    @Override
    public JobLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_application_summary,parent, false);
        return new JobLogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobLogViewHolder holder, int position){
        JobLog current = getItem(position);
        holder.bind(current, dateFmt);
    }

    public static class JobLogDiff extends DiffUtil.ItemCallback<JobLog>{
        @Override
        public boolean areItemsTheSame(@NonNull JobLog oldItem, @NonNull JobLog newItem){
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull JobLog oldItem, @NonNull JobLog newItem){
            if(!oldItem.getCompany().equals(newItem.getCompany())){
                return false;
            }
            if(!oldItem.getPosition().equals(newItem.getPosition())){
                return false;
            }
            if(!oldItem.getStatus().equals(newItem.getStatus())){
                return false;
            }
            if(!oldItem.getDateApplied().equals(newItem.getDateApplied())){
                return false;
            }

            return true;
        }

    }
}
