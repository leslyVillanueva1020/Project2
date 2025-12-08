package com.example.project2;

import static junit.framework.TestCase.assertNotNull;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.lifecycle.LiveData;
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
import java.util.List;

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

    //test the update feature in the reminder
    @Test
    public void updateReminder() {
        Reminder reminder = new Reminder(
                1,
                1,
                LocalDateTime.now(),
                "Test Reminder"
        );
        //insets it
        reminderDAO.insert(reminder);

        //makes a copy
        Reminder saved = reminderDAO.getAllReminders().get(0);
        //makes changes
        saved.setNote("Updated Reminder");
        saved.setApplicationId(2);
        saved.setUserId(2);
        //test and replace
        reminderDAO.insert(saved);

        //confirms only one row exists
        List<Reminder> result = reminderDAO.getAllReminders();
        assertEquals(1, result.size());

        //confirms values were updated
        Reminder updated = result.get(0);
        assertEquals("Updated Reminder", updated.getNote());
        assertEquals(2, updated.getApplicationId());
        assertEquals(saved.getId(), updated.getId());

    }

    //this should test the delete
    @Test
    public void delete_removesReminder() {
        Reminder reminder = new Reminder(
                1,
                1,
                LocalDateTime.now(),
                "Test Reminder"
        );
        reminderDAO.insert(reminder);

        int beforeSize = reminderDAO.getAllReminders().size();

        reminderDAO.delete(reminderDAO.getAllReminders().get(0));

        int afterSize = reminderDAO.getAllReminders().size();
        assertEquals(beforeSize - 1, afterSize);

    }

    @Test
    public void deleteByApplicationId() {
        Reminder r1 = new Reminder(
                1,
                1,  // applicationId = 1
                LocalDateTime.now(),
                "App1 - User1"
        );
        Reminder r2 = new Reminder(
                1,
                2,  // applicationId = 2
                LocalDateTime.now(),
                "App2 - User1"
        );

        reminderDAO.insert(r1);
        reminderDAO.insert(r2);

        reminderDAO.deleteByApplicationId(1);

        List<Reminder> remaining = reminderDAO.getAllReminders(); //1 should remain
        assertEquals(1, remaining.size());

        Reminder only = remaining.get(0);
        assertEquals(2, only.getApplicationId());
        assertEquals("App2 - User1", only.getNote());
    }

    @Test
    public void getReminderById() {
        Reminder reminder = new Reminder(
                3,
                30,
                LocalDateTime.now(),
                "Some Reminder"
        );

        reminderDAO.insert(reminder);

        LiveData<Reminder> liveData = reminderDAO.getReminderById(1);
        assertNotNull(liveData);
    }

}
