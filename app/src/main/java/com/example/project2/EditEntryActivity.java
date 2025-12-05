package com.example.project2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2.database.CareerNestDatabase;
import com.example.project2.database.JobLogDAO;
import com.example.project2.database.entities.JobLog;
import com.example.project2.databinding.ActivityEditEntryBinding;

import java.util.Calendar;
import java.util.Objects;

/**
 * @author Marissa Benenati
 * <br>COURSE: CST 338 - Software Design
 * <br>DATE: 12/4/2025
 * <br>ASSIGNMENT: Project 02
 */
public class EditEntryActivity extends AppCompatActivity {

    private ActivityEditEntryBinding binding;
    private EditText etCompanyName;
    private EditText etPosition;
    private Spinner spStatus;
    private Button btDate;
    private DatePickerDialog dpDateApplied;

    private JobLogDAO jobDAO;
    private int jobID;

    String[] statusOptions = {"Applied", "In Progress", "Rejected", "Offer, Interview"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setContentView(R.layout.activity_edit_entry);

        /// Initialize EditText widgets
        etCompanyName = findViewById(R.id.companyEditText);
        etPosition = findViewById(R.id.positionEditText);

        ///  Initialize DropDown widget
        spStatus = findViewById(R.id.statusDropDown);
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusOptions);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spStatus.setAdapter(adapter);

        ///  Initialize Date button text
        btDate = findViewById(R.id.dateButton);

        /// Opens Date Picker dialog via dateButton
        btDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openDialog();
            }
        });

        //dpDateApplied = findViewById(R.id.);

        /// Get database instance
        CareerNestDatabase db = CareerNestDatabase.getInstance(getApplicationContext());
        jobDAO = db.jobLogDAO();

        /// Get job log ID from intent
        jobID = getIntent().getIntExtra("JOB_ID", -1);

        /// Load existing data using helper function loadJobData(int)
        if(jobID != -1){
            loadJobData(jobID);
        }

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

    private void loadJobData(int id){
        new Thread(() -> {
            JobLog jobCurrent = jobDAO.getJobById(id);

            /// Update UI
            runOnUiThread(() -> {
                if(jobCurrent != null){
                    etCompanyName.setText(jobCurrent.getCompany());
                    etPosition.setText(jobCurrent.getPosition());
                    String setStatus = jobCurrent.getStatus();
                    /// use helper function to get desired index for drop down selection
                    spStatus.setSelection(getStatusPosition(spStatus, setStatus));
                    /// TODO: figure out how to set DatePickerDialog to previously set date upon opening

                }
            });
        });
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

    /// TODO: write openDialog() to open Date Picker to previously set date
    /// TODO: get previously set date via JobLog.getDateApplied
    private void openDialog(){}
}