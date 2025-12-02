package com.example.project2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.databinding.ActivityAllApplicationsBinding;
import com.example.project2.viewHolders.JobLogAdapter;
import com.example.project2.viewHolders.JobLogViewModel;

/**
 * @author Lesly Villanueva
 * <br>COURSE: CST 338 - Software Design
 * <br>DATE: 12/1/2025
 * <br>ASSIGNMENT: Project 02
 * Description: activity responsible for displayinh all job applications for logged in user.
 * It initializes Recycler View, connects it to the JobLogAdapter, and observes data provided by the ViewModel.
 * It also has buttons to send you back to landing page, details, or delete an entry.
 */
public class AllApplicationsActivity extends AppCompatActivity {
    // ---- SharedPreferences constants (must match Main & Landing) ----
    private static final String PREFS_NAME   = "app_prefs";
    private static final String KEY_USER_ID  = "userId";

    private JobLogViewModel jobLogViewModel;
    private JobLogAdapter adapter;

    private ActivityAllApplicationsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllApplicationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int userId = sp.getInt(KEY_USER_ID, -1);

        RecyclerView recyclerView = findViewById(R.id.recyclerAllApplications);
        adapter = new JobLogAdapter(new JobLogAdapter.JobLogDiff());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        jobLogViewModel = new ViewModelProvider(this).get(JobLogViewModel.class);

        jobLogViewModel.getAllLogsById(userId).observe(this, jobLogs -> {
            adapter.submitList(jobLogs);
        });

        binding.btnBackAllApplications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(LandingActivity.landingIntentFactory(AllApplicationsActivity.this, userId));
            }
        });
    }

    public static Intent allApplicationsIntentFactory(Context context){
        return new Intent(context, AllApplicationsActivity.class);
    }
}