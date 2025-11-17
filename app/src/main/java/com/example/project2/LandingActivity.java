package com.example.project2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.project2.database.CareerNestRepository;
import com.example.project2.database.entities.User;
import com.example.project2.databinding.ActivityLandingBinding;

public class LandingActivity extends AppCompatActivity {

    private static final String EXTRA_USER_ID = "com.example.project2.EXTRA_USER_ID";
    private static final String PREFS_NAME = "app_prefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IS_ADMIN = "isAdmin";
    private static final String KEY_USER_ID  = "userId";

    private ActivityLandingBinding binding;
    private CareerNestRepository repository;
    private int userId = -1;

    public static Intent intentFactory(Context context, int userId) {

        Intent i = new Intent(context, LandingActivity.class);
        i.putExtra(EXTRA_USER_ID, userId);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityLandingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = CareerNestRepository.getRepository(getApplication());

        userId = getIntent().getIntExtra(EXTRA_USER_ID, -1);

        if (userId != -1) {
            LiveData<User> userLive = repository.getUserByUserId(userId);
            userLive.observe(this, user -> {
                userLive.removeObservers(this);
                if (user != null) {
                    UsersView(user.getUsername(), user.isAdmin(), user.getId());
                } else {
                    UserWelcome();
                }
            });
        } else
        {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            String uname = prefs.getString(KEY_USERNAME, "");
            boolean isAdmin = prefs.getBoolean(KEY_IS_ADMIN, false);
            int cachedId = prefs.getInt(KEY_USER_ID, -1);
            UsersView(uname, isAdmin, cachedId);
        }

        // The buttons after signing in as either admin or regular user
        binding.btnAdminArea.setOnClickListener(v -> toast("Admin Area"));
        binding.btnViewAllApps.setOnClickListener(v -> toast("View All Applications"));
        binding.btnAddNewApp.setOnClickListener(v -> toast("Add New Application"));

        binding.btnLogout.setOnClickListener(v -> {
            getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit().clear().apply();
            // This is what will let you go back to the login page
            startActivity(LoginActivity.loginIntentFactory(this));
            finish();
        });
    }

    private void UsersView(String uname, boolean isAdmin, int id) {
        userId = id;
        binding.tvGreeting.setText((uname != null && !uname.isEmpty()) ? "Hello, " + uname : "Hello");
        binding.tvRole.setText(isAdmin ? "Role: Admin" : "Role: User");
        binding.btnAdminArea.setVisibility(isAdmin ? View.VISIBLE : View.INVISIBLE);

        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .putString(KEY_USERNAME, uname == null ? "" : uname)
                .putBoolean(KEY_IS_ADMIN, isAdmin)
                .putInt(KEY_USER_ID, userId)
                .apply();
    }

    private void UserWelcome() {
        binding.tvGreeting.setText("Hello");
        binding.tvRole.setText("Role: User");
        binding.btnAdminArea.setVisibility(View.INVISIBLE);
    }

    private void toast(String m){ Toast.makeText(this, m, Toast.LENGTH_SHORT).show(); }
}
