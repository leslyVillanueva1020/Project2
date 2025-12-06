package com.example.project2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.lifecycle.LiveData;
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
import java.util.List;
import java.util.Objects;

/**
 * @author Marissa Benenati
 * <br>COURSE: CST 338 - Software Design
 * <br>DATE: 12/5/2025
 * <br>ASSIGNMENT: Project 02
 * <br>==========================
 * <br>DESCRIPTION: This file contains tests for the User table in CareerNestDatabase.
 * */
@RunWith(AndroidJUnit4.class)
public class UserDAOTest {

    private UserDAO userDAO;
    private CareerNestDatabase db;
    private CareerNestRepository repository;
    /// ======================================
    User testUser1;
    User testUser2;
    User testUser3;
    User testUser4;
    String pswd = "multipass";

    @Before
    public void createDb(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, CareerNestDatabase.class)
                //.allowMainThreadQueries()
                .build();
        userDAO = db.userDAO();

        testUser1 = new User("johndoe", "password");
        testUser2 = new User("alicesmith", "passwordA");
        testUser3 = new User("billybob", "passwordB");
        testUser4 = new User("charliebrown", "passwordC");
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    /**
     * Insert a single user and retrieve by ID.
     * @throws Exception
     */
    @Test
    public void insertAndGetUser() throws Exception{
        userDAO.insert(testUser1);
        //repository.insertUser(testUser1);
        User retrievedUser = userDAO.getUserByUserId(testUser1.getId()).getValue();

        assertNotNull(retrievedUser);

        assertEquals("johndoe", retrievedUser.getUsername());
        assertEquals("password1", retrievedUser.getPassword());
    }

    /**
     * Insert multiple users, retrieve all, then check count retrieved.
     * @throws Exception
     */
    @Test
    public void insertMultUsersAndGetAll() throws Exception{
        userDAO.insert(testUser2);
        userDAO.insert(testUser3);
        userDAO.insert(testUser4);

        LiveData<List<User>> allUsers = userDAO.getAllUsers();

        assertEquals(3, Objects.requireNonNull(allUsers.getValue()).size()); //using requireNonNull according to IDE recommendation
    }

    /**
     * Tests <code>UPDATE</code> query. Checks both username and password updates.
     * @throws Exception
     */
    @Test
    public void updateUser() throws Exception{
        /// Test for username update
        userDAO.insert(testUser1);
        User retrievedUser = userDAO.getUserByUserId(testUser1.getId()).getValue();
        assertNotNull(retrievedUser);
        retrievedUser.setUsername("korbendallas");
        userDAO.update(retrievedUser);

        User updatedUser = userDAO.getUserByUserId(testUser1.getId()).getValue();
        assertNotNull(updatedUser);
        assertEquals("korbendallas", updatedUser.getUsername());


        /// Test for password update
        userDAO.insert(testUser4);
        User retrievedUser2 = userDAO.getUserByUserId(testUser4.getId()).getValue();
        assertNotNull(retrievedUser2);
        retrievedUser2.setPassword(pswd);
        userDAO.update(retrievedUser2);

        User updatedUser2 = userDAO.getUserByUserId(testUser4.getId()).getValue();
        assertNotNull(updatedUser2);
        assertEquals("multipass", updatedUser2.getPassword());
    }

    /**
     * Tests <code>DELETE</code> query for a single user.
     * @throws Exception
     */
    @Test
    public void deleteUser() throws Exception{
        userDAO.insert(testUser2);
        User retrievedUser = userDAO.getUserByUserId(testUser2.getId()).getValue();
        assertNotNull(retrievedUser);
        userDAO.delete(retrievedUser);
        User deletedUser = userDAO.getUserByUserId(testUser2.getId()).getValue();
        assertNull(deletedUser);
    }
}
