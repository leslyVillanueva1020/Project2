package com.example.project2;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.project2.database.CareerNestDatabase;
import com.example.project2.database.ReminderDAO;

import org.junit.Before;
import org.junit.runner.RunWith;

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

    }

}
