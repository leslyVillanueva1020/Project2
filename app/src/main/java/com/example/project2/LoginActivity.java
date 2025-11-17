package com.example.project2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;

import com.example.project2.database.CareerNestRepository;
import com.example.project2.database.entities.User;
import com.example.project2.databinding.ActivityLoginBinding;


/**
 * @author Lesly Villanueva
 * <br>COURSE: CST 338 - Software Design
 * <br>DATE: 11/14/2025
 * <br>ASSIGNMENT: Project 02
 */
public class LoginActivity extends AppCompatActivity {
    // ---- SharedPreferences constants (must match Main & Landing) ----
    private static final String PREFS_NAME   = "app_prefs";
    private static final String KEY_USER_ID  = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IS_ADMIN = "isAdmin";
    private ActivityLoginBinding binding;

    private CareerNestRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sp = getSharedPreferences("app_prefs", MODE_PRIVATE);
        int cachedUserId = sp.getInt("userId", -1);
        if (cachedUserId != -1) {
            startActivity(LandingActivity.intentFactory(this, cachedUserId));
            finish();
            return;
        }

        repository = CareerNestRepository.getRepository(getApplication());
        binding.buttonLoginPage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                verifyUser();
            }
        });
    }

    private void verifyUser() {
        String username = binding.userNameLoginEditText.getText().toString();

        if(username.isEmpty()){
            //TODO: come back and replace toast maker with actual msg thats displayed somewhere
            toastMaker("Username may not be blank.");
            return;
        }

        LiveData<User> userObserver = repository.getUserByUserName(username);
        userObserver.observe(this, user -> {
            if(user != null){
                String password = binding.passwordLoginEditText.getText().toString();

                if(password.equals(user.getPassword())){
                    SharedPreferences sp = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt(KEY_USER_ID, user.getId());
                    editor.putString(KEY_USERNAME, user.getUsername());
                    editor.putBoolean(KEY_IS_ADMIN, user.isAdmin());
                    editor.apply();
                    startActivity(LandingActivity.intentFactory(this, user.getId()));
                    //startActivity(MainActivity.mainActivityIntentFacotry(getApplicationContext(), user.getId()));
                    finish();
                }
                else{
                    toastMaker("Invalid Password.");
                    binding.passwordLoginEditText.setSelection(0);
                }
            }else {
                toastMaker(String.format("User %s is not valid username", username));
            }
        });
    }

    private void toastMaker(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    static Intent loginIntentFactory(Context context){
        return new Intent(context, LoginActivity.class);
    }
}