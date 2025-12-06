package com.example.project2;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project2.database.CareerNestRepository;
import com.example.project2.database.entities.JobLog;
import com.example.project2.database.entities.User;
import com.example.project2.databinding.ActivityNewApplicationBinding;

import java.time.LocalDateTime;

public class NewApplicationActivity extends AppCompatActivity {

    private ActivityNewApplicationBinding binding;

    private JobLog jobLog;
    private static User user;
    int userId = -1;
    private LocalDateTime selectedDate;

    private CareerNestRepository repository;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewApplicationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //initialize the repository
        repository = CareerNestRepository.getRepository(getApplication());

        //this should get the user and joblog from the intent
        userId = getIntent().getIntExtra("EXTRA_USER_ID", -1);
        //makes sure the userId is valid
        if(userId == -1){
            toastMaker("FATAL ERROR: User not found");
            finish();
            return;
        }

        repository.getUserByUserId(userId).observe(this, fetchedUser -> {
            if(fetchedUser != null){
                user = fetchedUser;
            }else{
                toastMaker("FATAL ERROR: User not found");
                finish();
            }
                });

        jobLog = getIntent().getParcelableExtra("jobLog");

        //creates th options for the drop down menu
        String[] status = {"Applied", "In Progress", "Rejected", "Offer, Interview"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                status
        );
        //this setst he layout for the drop down menu
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //this sets the adapter to the drop down menu
        binding.dropDownMenu.setAdapter(adapter);
        //should run when you click the date button
        binding.dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastMaker("DATE BUTTON CLICKED!");
                openDialog();
            }
        });
        //TODO set up reminder page to connect to this
        Button button = findViewById(R.id.reminderButton);
        button.setOnClickListener(View -> {
            toastMaker("Reminder Button Clicked!");
        });

        //should run when you click save button
        button = findViewById(R.id.saveButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addJobLog()){
                    finish();
                }

            }
        });

        //this should run when you click CANCEL button
        binding.cancelButton.setOnClickListener(v -> finish());
    }

    private void openDialog(){
        //get current date so the calendar opens to today
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //sets the selected date
                selectedDate = LocalDateTime.of(year, month + 1, dayOfMonth, 12, 0);
                //month is 0-indexed (Jan = 0), so add 1 for display
                String dateString = year + "/" + (month + 1) + "/" + dayOfMonth;

                //show toast
                toastMaker(dateString);

                //update the text on the button to show the selected date
                binding.dateButton.setText(dateString);
            }
        }, year, month, day); //pass current date as default

        dialog.show();
    }

    private void toastMaker(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    //this should verify if the user is logged in
    private void verifyUser(){
        String username = user.getUsername();
        if(username.isEmpty()){
            //TODO: come back and replace toast maker with actual msg thats displayed somewhere
            toastMaker("Username may not be blank.");
            return;
        }


    }

    /**
     * this should add the information into the database if it is valid
     */
    private boolean addJobLog(){
        //makes sure the user is logged in
        if(user == null){
            toastMaker("User is null");
            return false;
        }

        String companyName = binding.CompanyNameEditText.getText().toString();
        String position = binding.PositionEditText.getText().toString();
        String status = binding.dropDownMenu.getSelectedItem().toString();
        String dateApplied = binding.dateButton.getText().toString();

        //LocalDateTime dateApplied = LocalDateTime.parse(binding.dateButton.getText().toString());
        int userId = user.getId();

        //these if statments should make sure the info isn't left blank
        if(companyName.isEmpty()){
            toastMaker("Company name may not be blank.");
            return false;
        }
        if(position.isEmpty()){
            toastMaker("Position may not be blank.");
            return false;
        }
        if(status.isEmpty()){
            toastMaker("Status may not be blank.");
            return false;
        }
        if(dateApplied.equals("Enter Date")){
            toastMaker("Date may not be blank.");
            return false;
        }



        //should then add it to the repo

        jobLog = new JobLog(companyName, position, status, selectedDate, userId);

        repository.insertJobLog(jobLog);
        return true;

    }
    //this should help switch between intents
    static Intent newAppIntentFactory(Context context, int userId){
        //use a constant for the key
        Intent intent = new Intent(context, NewApplicationActivity.class);intent.putExtra("EXTRA_USER_ID", userId);
        return intent;
    }
    }

