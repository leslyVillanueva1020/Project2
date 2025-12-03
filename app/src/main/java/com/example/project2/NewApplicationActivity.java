package com.example.project2;

import android.app.DatePickerDialog;
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

import com.example.project2.databinding.ActivityNewApplicationBinding;

public class NewApplicationActivity extends AppCompatActivity {

    private String companyName = "";
    private final String status = "";
    private final String dateApplied = "";
    private ActivityNewApplicationBinding binding;
    private Button button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewApplicationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
                toastMaker("Application Saved!");
                finish();
            }
        });
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


}