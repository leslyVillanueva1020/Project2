package com.example.project2;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2.database.CareerNestRepository;
import com.example.project2.database.JobLogDAO;
import com.example.project2.database.entities.JobLog;
import com.example.project2.databinding.ActivityEditEntryBinding;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author Marissa Benenati
 * <br>COURSE: CST 338 - Software Design
 * <br>DATE: 12/4/2025
 * <br>ASSIGNMENT: Project 02
 * <br>==========================
 * <br>DESCRIPTION: Activity responsible for allowing users to edit an existing Job Log entry within the database.
 * Widgets on this screen are pre-populated with data from the original log entry being edited.
 * <code>CANCEL</code> button returns the user to AllApplicationsActivity.
 */
public class EditEntryActivity extends AppCompatActivity {
    private static final String EXTRA_USER_ID = "com.example.project2.EXTRA_USER_ID";
    private static final String EXTRA_JOB_ID = "EXTRA_JOB_ID";
    private CareerNestRepository repository;
    private ActivityEditEntryBinding binding;
    static Intent intent;
    /// ==============================================
    private EditText etCompanyName;
    private EditText etPositionTitle;
    private Spinner spStatus;
    private Button btDate;
    /// ==============================================
    private JobLogDAO jobDAO;
    private int jobID;
    private JobLog currentJob;
    private LocalDateTime selectedDate;
    /// ==============================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /// Initialize EditText widgets
        etCompanyName = findViewById(R.id.companyEditText);
        etPositionTitle = findViewById(R.id.positionEditText);

        ///  Initialize DropDown widget - code snippet by Adrik Renteria ===============
        spStatus = findViewById(R.id.statusDropDown);
        String[] status = {"Applied", "In Progress", "Rejected", "Offer, Interview"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                status
        );
        //this sets the layout for the drop down menu
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //this sets the adapter to the drop down menu
        spStatus.setAdapter(adapter);
        /// ============================================================================

        ///  Initialize Date button text
        btDate = findViewById(R.id.dateButton);

        /// Get repository
        repository = CareerNestRepository.getRepository(getApplication());

        /// Get job log ID from intent
        jobID = getIntent().getIntExtra(EXTRA_JOB_ID, -1);

        /// Load existing data using helper function loadJobData(int)
        if(jobID != -1){
            loadJobData(jobID);
        }

        /// Opens Date Picker dialog via dateButton
        btDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openDialog();
            }
        });

        /// Save button -> update entry in table
        binding.saveChangesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                saveEdits();
            }
        });

        /// Cancel button -> returns to AllApplicationsActivity
        binding.cancelEditButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                intent = AllApplicationsActivity.allApplicationsIntentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
    }

    /**
     * Pre-populates widgets on screen with data from the Job Log being edited. Takes a Job Log ID.
     * @param id int
     */
    private void loadJobData(int id){
        new Thread(() -> {
            currentJob = repository.getJobById(id);

            /// Update UI
            runOnUiThread(() -> {
                if(currentJob != null){
                    etCompanyName.setText(currentJob.getCompany());
                    etPositionTitle.setText(currentJob.getPosition());

                    /// get String representation of application status
                    String statusText = currentJob.getStatus();
                    /// use helper function to get desired index for drop down selection, using statusText
                    int ddIndex = getStatusPosition(spStatus, statusText);
                    if(ddIndex > -1){            // must check if helper function returned a match
                        spStatus.setSelection(ddIndex);  // sets drop down menu to proper index
                    }

                    LocalDateTime dateApplied = currentJob.getDateApplied();
                    if(dateApplied != null){
                        selectedDate = dateApplied;  // need selectedDate for displaying Date Picker
                        updateDateButton(dateApplied);
                        /// TODO: change date format on button to be consistent across both EditEntry and NewApplication (Should be MM/dd/yyyy)
                    }
                    //btDate.setText(currentJob.getDateApplied());
                }
            });
        }).start();
    }

    /**
     * Matches 'Status' data point from DB table to a 0-indexed position in Drop Down options array.
      * @return int
     */
    private int getStatusPosition(Spinner spinner, String value){
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        if(adapter != null){
            for(int i = 0; i < adapter.getCount(); i++){
                if(Objects.equals(adapter.getItem(i), value)){
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Uses DateTimeFormatter to handle the LocalDateTime value fetched from <code>JobLog.getDateApplied</code>.
     * It then sets the Date Picker button text accordingly.
     * @param date LocalDateTime
     */
    private void updateDateButton(LocalDateTime date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedDate = date.format(formatter);
        btDate.setText(formattedDate);
    }

    /**
     * Opens the Date Picker Dialog. Is triggered onClick of <code>btDate</code>.
     */
    private void openDialog(){
        if(selectedDate == null){
            selectedDate = LocalDateTime.now();  // default to current system date if selectedDate is null
        }

        int year = selectedDate.getYear();
        int month = selectedDate.getMonthValue() - 1;
        int day = selectedDate.getDayOfMonth();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    selectedDate = LocalDateTime.of(
                            selectedYear,
                            selectedMonth + 1,
                            selectedDay,
                            selectedDate.getHour(),
                            selectedDate.getMinute(),
                            selectedDate.getSecond()
                    );
                    /// update Date Button text w/newly selected date
                    updateDateButton(selectedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    /**
     * Updates JobLog table with the edited <code>Job Log</code> object values.
     */
    private void saveEdits(){
        if(currentJob == null){
            return;
        }

        /// update Job Log object with the new values
        currentJob.setCompany(etCompanyName.getText().toString());
        currentJob.setPosition(etPositionTitle.getText().toString());
        currentJob.setStatus(spStatus.getSelectedItem().toString());
        if(selectedDate != null){
            currentJob.setDateApplied(selectedDate);
        }

        /// save to database on background thread
        new Thread(() -> {
            try {
                repository.updateJob(currentJob);
                runOnUiThread(() -> {
                    toastMaker("Application Entry Updated");
                    intent = AllApplicationsActivity.allApplicationsIntentFactory(getApplicationContext());
                    startActivity(intent);
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    toastMaker("Error saving changes: " + e.getMessage());
                    e.printStackTrace();
                });
            }
        }).start();
    }

    private void toastMaker(String s){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    public static Intent editEntryIntentFactory(Context context, int userId, int jobId){
        Intent intent = new Intent(context, EditEntryActivity.class);
        intent.putExtra(EXTRA_USER_ID, userId);
        intent.putExtra(EXTRA_JOB_ID, jobId);
        return intent;
    }

}