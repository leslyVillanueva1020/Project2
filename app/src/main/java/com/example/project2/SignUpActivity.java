package com.example.project2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.project2.database.CareerNestRepository;
import com.example.project2.database.entities.User;
import com.example.project2.databinding.ActivitySignUpBinding;

/**
 * @author Marissa Benenati
 * <br>COURSE: CST 338 - Software Design
 * <br>DATE: 12/2/2025
 * <br>ASSIGNMENT: Project 02
 */
public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;

    private CareerNestRepository repository;

    static Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = CareerNestRepository.getRepository(getApplication());

        binding.buttonSignUpPage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addUser();
            }
        });

        /// Cancel button -> returns to LoginActivity
        binding.buttonSignUpCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                intent = LoginActivity.loginIntentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
    }

    /**
     * Adds new user's given credentials to the User database.
     * Is triggered on click of buttonSignUpPage.
     */
    private void addUser() {
        String username = binding.userNameSignUpEditText.getText().toString();
        String password = binding.passwordSignUpEditText.getText().toString();

        if(username.isEmpty()){
            toastMaker("Username may not be blank.");
            return;
        }

        if(password.isEmpty()){
            toastMaker("Password may not be blank.");
            return;
        }

        repository.getUserByUserName(username).observe(this, existingUser -> {
            if(existingUser != null){
                toastMaker("User Already Exists");
            }
            else{
                User newUser = new User(username, password);
                repository.insertUser(newUser);
                toastMaker("Account Successfully Created");
                intent = LoginActivity.loginIntentFactory(getApplicationContext());
                startActivity(intent);
            }
        });
    }

    private void toastMaker(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    static Intent signUpIntentFactory(Context context){
        return new Intent(context, SignUpActivity.class);
    }
}