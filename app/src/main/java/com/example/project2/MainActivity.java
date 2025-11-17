/**
 * Author: Adrik Renteria
 * Date 11/13/2025
 * Project: Project 2
 * Abstract: this is the main activity for the project
 */
package com.example.project2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.project2.databinding.ActivityMainBinding;

public class MainActivity extends Activity {
    ActivityMainBinding binding;

    private static final String TAG = "MainActivity";
    String username = "";
    String password = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInformationFromDisplay();
                //updateDisplay();

                //Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                //startActivity(intent);
            }
        });
    }

    private void updateDisplay(){
        //String currentinfo = binding.UserNameEditText.toString();
    }

    private void getInformationFromDisplay(){
        //this should get the log in info
        username = binding.UserNameEditText.getText().toString();
        password = binding.passwordeEditText.getText().toString();

    }

}
