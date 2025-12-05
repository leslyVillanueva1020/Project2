package com.example.project2;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2.database.CareerNestDatabase;
import com.example.project2.database.CareerNestRepository;
import com.example.project2.database.JobLogDAO;
import com.example.project2.database.entities.JobLog;
import com.example.project2.database.typeConverters.LocalDateTypeConverter;
import com.example.project2.databinding.ActivityEditEntryBinding;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author Marissa Benenati
 * <br>COURSE: CST 338 - Software Design
 * <br>DATE: 12/4/2025
 * <br>ASSIGNMENT: Project 02
 */
public class EditEntryActivity extends AppCompatActivity {

    private CareerNestRepository repository;
    private ActivityEditEntryBinding binding;
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
    String[] statusOptions = {"Applied", "In Progress", "Rejected", "Offer, Interview"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setContentView(R.layout.activity_edit_entry);

        /// Initialize EditText widgets
        etCompanyName = findViewById(R.id.companyEditText);
        etPositionTitle = findViewById(R.id.positionEditText);

        ///  Initialize DropDown widget
        spStatus = findViewById(R.id.statusDropDown);

        ///  Initialize Date button text
        btDate = findViewById(R.id.dateButton);

        /// Get repository
//        CareerNestDatabase db = CareerNestDatabase.getDatabase(getApplicationContext());
//        jobDAO = db.jobLogDAO();
        repository = CareerNestRepository.getRepository(getApplication());

        /// Get job log ID from intent
        jobID = getIntent().getIntExtra("JOB_ID", -1);

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

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

    /**
     * Pre-populates widgets on screen with data from the Job Log being edited. Takes a Job Log ID.
     * @param id int
     */
    private void loadJobData(int id){
        new Thread(() -> {
            currentJob = jobDAO.getJobById(id);

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

                    /// TODO: figure out how to set DatePickerDialog to previously set date upon opening
                    LocalDateTime dateApplied = currentJob.getDateApplied();
                    if(dateApplied != null){
                        selectedDate = dateApplied;  // need selectedDate for displaying Date Picker
                        updateDateButton(dateApplied);
                    }
                    //btDate.setText(currentJob.getDateApplied());
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
        /// TODO: may need to parse / process String fetched via JobLog.getStatus prior to below search?
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

    /// TODO: write openDialog() to open Date Picker to previously set date
    /// TODO: get previously set date via JobLog.getDateApplied
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
}