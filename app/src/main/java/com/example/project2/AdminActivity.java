package com.example.project2;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.database.CareerNestRepository;
import com.example.project2.database.entities.User;
import com.example.project2.viewHolders.UsersAdapter;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Estrella Ortiz
 * <br>COURSE: CST 338 - Software Design
 * <br>DATE: 12/3/2025
 * <br>ASSIGNMENT: Project 02
 */

public class AdminActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "app_prefs";
    private static final String KEY_IS_ADMIN = "isAdmin";

    private CareerNestRepository repository;
    private UsersAdapter adapter;
    private List<User> allUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (!sp.getBoolean(KEY_IS_ADMIN, false)) {
            Toast.makeText(this, "Admin only", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        repository = CareerNestRepository.getRepository(getApplication());
        RecyclerView rvUsers = findViewById(R.id.rvUsers);
        EditText etSearch = findViewById(R.id.etSearch);
        Button btnBackHome = findViewById(R.id.btnBackHome);

        adapter = new UsersAdapter(new ArrayList<>(), new UsersAdapter.OnUserAction() {
        @Override public void onPromote(User user) { promoteUser(user); }
           @Override public void onDelete(User user)  { confirmDelete(user); }

            @Override public void onDemote(User user) { demoteUser(user); }
        });

        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        rvUsers.setAdapter(adapter);

        btnBackHome.setOnClickListener(v -> finish());

        repository.getAllUsers().observe(this, users -> {
            allUsers = users;
            adapter.updateList(users);
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {

            }
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {
                filter(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {

            }
        });
    }

    private void filter(String q) {
        if (q == null || q.isEmpty()) { adapter.updateList(allUsers); return; }

        List<User> filtered = new ArrayList<>();
        for (User u : allUsers) {
            String name = u.getUsername();
            if (name != null && name.toLowerCase().contains(q.toLowerCase())) {
                filtered.add(u);
            }
        }
        adapter.updateList(filtered);
    }

    private void promoteUser(User user) {
        if (user.isAdmin()) {
            Toast.makeText(this, user.getUsername() + " is already an Admin", Toast.LENGTH_SHORT).show();
            return;
        }
        user.setAdmin(true);
        repository.updateUser(user);
        Toast.makeText(this, "Promoted " + user.getUsername() + " to Admin", Toast.LENGTH_SHORT).show();
    }

    private void demoteUser(User user) {
        if (!user.isAdmin()) {
            Toast.makeText(this,
                    user.getUsername() + " is no longer an Admin",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        user.setAdmin(false);
        repository.updateUser(user);
        Toast.makeText(this,
                "Demoted " + user.getUsername() + " from Admin",
                Toast.LENGTH_SHORT).show();
    }

    private void confirmDelete(User user) {
        new AlertDialog.Builder(this)
                .setTitle("Delete " + user.getUsername() + "?")
                .setMessage("This action cannot be undone.")
                .setPositiveButton("Delete", (d, w) -> deleteUser(user))
                .setNegativeButton("Cancel", null)
                .show();
    }
    private void deleteUser(User user) {
        repository.deleteUser(user);
        Toast.makeText(this, "Deleted " + user.getUsername(), Toast.LENGTH_SHORT).show();
    }
}
