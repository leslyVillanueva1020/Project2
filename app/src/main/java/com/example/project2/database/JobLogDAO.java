package com.example.project2.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.project2.database.entities.JobLog;

import java.util.List;


/**
 * @author Lesly Villanueva
 * <br>COURSE: CST 338 - Software Design
 * <br>DATE: 11/14/2025
 * <br>ASSIGNMENT: Project 02
 */
@Dao
public interface JobLogDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(JobLog jobLog);


    @Query("SELECT * FROM " + CareerNestDatabase.JOB_LOG_TABLE + " ORDER BY dateApplied DESC")
    List<JobLog> getAllRecords();

    @Query("SELECT * FROM " + CareerNestDatabase.JOB_LOG_TABLE + " WHERE userId = :loggedInUserId ORDER BY dateApplied DESC")
    List<JobLog> getRecordsByUserId(int loggedInUserId);

    @Query("SELECT * FROM " + CareerNestDatabase.JOB_LOG_TABLE + " WHERE userId = :loggedInUserId ORDER BY dateApplied DESC")
    LiveData<List<JobLog>> getRecordsByUserIdLiveData(int loggedInUserId);
}
