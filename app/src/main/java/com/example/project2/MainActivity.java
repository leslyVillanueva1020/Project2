package com.example.project2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.project2.database.CareerNestRepository;
import com.example.project2.database.entities.User;
import com.example.project2.databinding.ActivityMainBinding;
import com.example.project2.viewHolders.JobLogViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String MAIN_ACTIVITY_USER_ID = "com.example.project2.MAIN_ACTIVITY_USER_ID";
    private static final String SAVED_INSTANCE_STATE_USERID_KEY = "com.example.project2.SAVED_USER_ID";
    private static final int LOGGED_OUT = -1;

//    SHARED PREFRENCES
    private static final String PREFS_NAME  = "app_prefs";
    private static final String KEY_USER_ID = "userId";
    private ActivityMainBinding binding;
    private CareerNestRepository repository;
    private JobLogViewModel jobLogViewModel;
    private int loggedInUserId = -1;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view); //this section sets main screen of Android App

        repository = CareerNestRepository.getRepository(getApplication());

//        binding.btnLogin.setOnClickListener(v ->
//                startActivity(LoginActivity.loginIntentFactory(this))
//        );
//
//        binding.btnCreateAccount.setOnClickListener(v -> {
//            startActivity(LoginActivity.loginIntentFactory(this));
//        });

        loginUser(savedInstanceState);

        if(loggedInUserId != LOGGED_OUT){
            startActivity(LandingActivity.intentFactory(this, loggedInUserId));
            finish();
        }
    }

    private void loginUser(Bundle savedInstanceState) {
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        loggedInUserId = sp.getInt(KEY_USER_ID, LOGGED_OUT);

        if(loggedInUserId == LOGGED_OUT && savedInstanceState != null &&
                savedInstanceState.containsKey(SAVED_INSTANCE_STATE_USERID_KEY)){
            loggedInUserId = savedInstanceState.getInt(SAVED_INSTANCE_STATE_USERID_KEY, LOGGED_OUT);
        }
        if (loggedInUserId == LOGGED_OUT) {
            loggedInUserId = getIntent().getIntExtra(MAIN_ACTIVITY_USER_ID, LOGGED_OUT);
        }
        if(loggedInUserId == LOGGED_OUT){
            return;
        }

        LiveData<User> userObserver = repository.getUserByUserId(loggedInUserId);
        userObserver.observe(this, u -> {
            this.user = u;
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        outState.putInt(SAVED_INSTANCE_STATE_USERID_KEY, loggedInUserId);
        super.onSaveInstanceState(outState);
    }

    static Intent mainActivityIntentFacotry(Context context, int userId){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MAIN_ACTIVITY_USER_ID, userId);
        return intent;
    }
}
