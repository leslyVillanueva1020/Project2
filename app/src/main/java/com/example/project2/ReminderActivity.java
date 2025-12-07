package com.example.project2;
/*
 * @author Adrik Renteria
 * <br>COURSE: CST 338 - Software Design
 * <br>DATE: 12/6/2025
 * <br>ASSIGNMENT: Project 02
 * Description: java activity file for the reminder page, it should
 * let you add a reminder and set the date and time for it. It should
 * then save it to the database. Afterwards you should return to
 * New Application Activity.
 */
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project2.database.CareerNestRepository;
import com.example.project2.database.entities.User;
import com.example.project2.databinding.ActivityReminderBinding;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReminderActivity extends AppCompatActivity {
    private ActivityReminderBinding binding;

    private static User user;
    int userId = -1;
    private LocalDate selectedDate;
    private LocalTime selectedTime;
    private CareerNestRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityReminderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        repository = CareerNestRepository.getRepository(getApplication());

        userId = getIntent().getIntExtra("EXTRA_USER_ID", -1);
        //makes sure the userId is valid
        if(userId == -1){
            toastMaker("FATAL ERROR: User not found");
            finish();
            return;
        }
        //makes sure the user is logged in
        repository.getUserByUserId(userId).observe(this, fetchedUser -> {
            if(fetchedUser != null){
                user = fetchedUser;
            }else{
                toastMaker("FATAL ERROR: User not found");
                finish();
            }
        });
        //should run when you click the date button
        binding.dateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                toastMaker("DATE BUTTON CLICKED!");
                openDateDialog();
            }
        });
        //should run when you click the time button
        binding.timeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                toastMaker("TIME BUTTON CLICKED!");
                openTimeDialog();
            }
        });
        Button button = findViewById(R.id.saveButton);

        //should run when you click save button
        button = findViewById(R.id.saveButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO implement the save button

                toastMaker("Save Button Clicked!");
                saveReminder();

            }
        });

        //this should run when you click CANCEL button
        binding.cancelButton.setOnClickListener(v -> finish());
    }

    /**
     * this should open the date dialog and set the date
     */
    private void openDateDialog(){
        Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            //save the date
            selectedDate = LocalDate.of(year, month + 1, dayOfMonth);

            //update Text
            binding.dateButton.setText(selectedDate.toString());
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }
    /**
     * this should open the time dialog and set the time
     */
    private void openTimeDialog() {
        Calendar c = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            //save the time
            selectedTime = LocalTime.of(hourOfDay, minute);

            //update Text
            binding.timeButton.setText(selectedTime.toString());
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false); //false = 12h format, true = 24h
        dialog.show();
    }

    /**
     * this should save the reminder to the database
     */
    private void saveReminder() {
        //validate Inputs
        if (selectedDate == null) {
            toastMaker("Please select a date.");
            return;
        }
        if (selectedTime == null) {
            toastMaker("Please select a time.");
            return;
        }

        //combine Date and Time
        LocalDateTime finalDateTime = LocalDateTime.of(selectedDate, selectedTime);

        //get Note Text
        String note = binding.noteEditText.getText().toString();

        if (note.isEmpty()) {
            toastMaker("Please enter a note.");
            return;
        }

        //TODO: Save to Database

        toastMaker("Reminder set for: " + finalDateTime.toString());

        //close screen
       // finish();
    }

    private void toastMaker(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
    static Intent reminderIntentFactory(Context context, int userId){
        //use a constant for the key
        Intent intent = new Intent(context, ReminderActivity.class);
        intent.putExtra("EXTRA_USER_ID", userId);
        return intent;
    }
}