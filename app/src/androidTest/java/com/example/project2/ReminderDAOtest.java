package com.example.project2;

import static junit.framework.TestCase.assertNotNull;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.project2.database.CareerNestDatabase;
import com.example.project2.database.ReminderDAO;
import com.example.project2.database.entities.Reminder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;

/**
 * @author Adrik Renteria
 * <br>COURSE: CST 338 - Software Design
 * <br>DATE: 12/7/2025
 * <br>ASSIGNMENT: Project 02
 * <br>==========================
 * <br>DESCRIPTION: This file contains tests for the User table in CareerNestDatabase.
 * */
@RunWith(AndroidJUnit4.class)
public class ReminderDAOtest {

    private CareerNestDatabase db;
    private ReminderDAO reminderDAO;

    @Before
    public void setUp(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, CareerNestDatabase.class)
                .allowMainThreadQueries()
                .build();
        reminderDAO = db.reminderDAO();
    }

    @After
    public void tearDown(){
        db.close();
    }
    //this should test the insert
    @Test
    public void insert_addsReminder() {
        int beforeSize = reminderDAO.getAllReminders().size();
        Reminder reminder = new Reminder(
                1,
                1,
                LocalDateTime.now(),
                "Test Reminder"
        );
        reminderDAO.insert(reminder);
        int afterSize = reminderDAO.getAllReminders().size();
        assertEquals(beforeSize + 1, afterSize);
    }

}
