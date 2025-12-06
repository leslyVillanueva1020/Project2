package com.example.project2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.project2.database.CareerNestDatabase;
import com.example.project2.database.CareerNestRepository;
import com.example.project2.database.UserDAO;
import com.example.project2.database.entities.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

/**
 * @author Marissa Benenati
 * <br>COURSE: CST 338 - Software Design
 * <br>DATE: 12/5/2025
 * <br>ASSIGNMENT: Project 02
 * <br>==========================
 * <br>DESCRIPTION: This file contains tests for the User table.
 * */
@RunWith(AndroidJUnit4.class)
public class UserDAOTest {

    private UserDAO userDAO;
    private CareerNestDatabase db;
    private CareerNestRepository repository;

    @Before
    public void createDb(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, CareerNestDatabase.class)
                //.allowMainThreadQueries()
                .build();
        userDAO = db.userDAO();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    /**
     * Insert a single user and retrieve by ID.
     */
    @Test
    public void insertAndGetUser() throws Exception{
        User testUser1 = new User("johndoe", "password1");
        repository.insertUser(testUser1);
        User retrievedUser = userDAO.getUserByUserId(testUser1.getId()).getValue();

        assertNotNull(retrievedUser);

        assertEquals("johndoe", retrievedUser.getUsername());
        assertEquals("password1", retrievedUser.getPassword());
    }


}
