package com.example.project2.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;                 // <-- make sure this import exists

import com.example.project2.database.entities.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... user);

    @Delete
    void delete(User user);

    @Update
    void update(User user);

    @Query("SELECT * FROM " + CareerNestDatabase.USER_TABLE + " ORDER BY username")
    LiveData<List<User>> getAllUsers();
    @Query("DELETE from " + CareerNestDatabase.USER_TABLE)
    void deleteAll();

    @Query("SELECT * FROM " + CareerNestDatabase.USER_TABLE + " WHERE username == :username LIMIT 1")
    LiveData<User> getUserByUserName(String username);

    @Query("SELECT * FROM " + CareerNestDatabase.USER_TABLE + " WHERE id == :userId LIMIT 1")
    LiveData<User> getUserByUserId(int userId);

    /// === added below 3 queries for testing - Marissa Benenati =============================
    @Query("SELECT * FROM " + CareerNestDatabase.USER_TABLE + " ORDER BY username")
    List<User> getAllUsersList();

    @Query("SELECT * FROM " + CareerNestDatabase.USER_TABLE + " WHERE id == :userId LIMIT 1")
    User getUserByUserIdAlt(int userId);

    @Query("SELECT * FROM " + CareerNestDatabase.USER_TABLE + " WHERE username == :username LIMIT 1")
    User getUserByUserNameAlt(String username);
    /// =======================================================================================
}
