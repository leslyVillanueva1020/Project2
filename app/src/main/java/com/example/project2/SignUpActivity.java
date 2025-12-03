package com.example.project2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2.database.CareerNestDatabase;
import com.example.project2.database.UserDAO;
import com.example.project2.database.entities.User;
import com.example.project2.databinding.ActivitySignUpBinding;

/**
 * @author Marissa Benenati
 * <br>COURSE: CST 338 - Software Design
 * <br>DATE: 12/2/2025
 * <br>ASSIGNMENT: Project 02
 */
public class SignUpActivity extends AppCompatActivity {

    //private static final CareerNestDatabase INSTANCE = ;
    private ActivitySignUpBinding binding;

    //private CareerNestRepository repository;

    //UserDAO dao = INSTANCE.userDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //repository = CareerNestRepository.getRepository(getApplication());

        binding.buttonSignUpPage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {addUser();
            }
        });
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_sign_up);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
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

        User newUser = new User(username, password);
        CareerNestDatabase db = CareerNestDatabase.getDatabase(getApplicationContext());
        UserDAO userDAO = db.userDAO();
        ///  TODO: figure out why new user is not being added to database
        userDAO.insert(newUser);
        toastMaker("Account Successfully Created");
    }

    private void toastMaker(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    static Intent signUpIntentFactory(Context context){
        return new Intent(context, SignUpActivity.class);
    }
}