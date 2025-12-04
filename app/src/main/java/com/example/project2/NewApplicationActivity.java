package com.example.project2;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
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

import com.example.project2.database.entities.JobLog;
import com.example.project2.database.entities.User;
import com.example.project2.databinding.ActivityNewApplicationBinding;

import java.time.LocalDateTime;

public class NewApplicationActivity extends AppCompatActivity {

    private String companyName = "";
    private final String status = "";
    private final String dateApplied = "";
    private ActivityNewApplicationBinding binding;
    private Button button;

    private JobLog jobLog;
    private User user;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewApplicationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //this should get the use and joblog from the intent
        user = getIntent().getParcelableExtra("user");
        jobLog = getIntent().getParcelableExtra("jobLog");





        //TODO implement company name and title

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
        //



        button = findViewById(R.id.saveButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();

            }
        });

        binding.dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastMaker("DATE BUTTON CLICKED!");
                finish();
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
                //month is 0-indexed (Jan = 0), so add 1 for display
                String dateString = year + "." + (month + 1) + "." + dayOfMonth;

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
    private void addJobLog(){
        companyName = binding.CompanyNameEditText.getText().toString();
        String position = binding.PositionEditText.getText().toString();
        String status = binding.dropDownMenu.getSelectedItem().toString();
        LocalDateTime dateApplied = LocalDateTime.parse(binding.dateButton.getText().toString());
        int userId = user.getId();
        //TODO needs some work
        /*
        if(companyName.isEmpty() || position.isEmpty() || status.isEmpty() || dateApplied.isEmpty()){
            toastMaker("All fields must be filled out.");
            return;
        }
        jobLog = new JobLog(companyName, position, status, dateApplied, userId);
        //TODO: add joblog to database */

    }
    //this should help switch between intents
    static Intent intentFactory(Context context){
        return new Intent(context, NewApplicationActivity.class);
    }
}