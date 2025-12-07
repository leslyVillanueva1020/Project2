package com.example.project2.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.project2.database.entities.Reminder;

import java.util.List;

/*
 * @author Adrik Renteria
 * <br>COURSE: CST 338 - Software Design
 * <br>DATE: 12/6/2025
 * <br>ASSIGNMENT: Project 02
 * Description: this is the Reminder DAO, it should store
 * a reminder associated to an application
 */
@Dao
public interface ReminderDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Reminder reminder);

    @Delete
    void delete(Reminder reminder);

    @Update
    void update(Reminder reminder);
    @Query("SELECT * FROM " + CareerNestDatabase.REMINDER_TABLE)
    List<Reminder> getAllReminders();
    @Query("DELETE from " + CareerNestDatabase.REMINDER_TABLE)
    void deleteAll();


    @Query("SELECT * FROM " + CareerNestDatabase.REMINDER_TABLE + " WHERE userId == :userId")
    LiveData<List<Reminder>> getRemindersByUserId(int userId);

    @Query("SELECT * FROM " + CareerNestDatabase.REMINDER_TABLE + " WHERE id == :reminderId")
    LiveData<Reminder> getReminderById(int reminderId);


}
