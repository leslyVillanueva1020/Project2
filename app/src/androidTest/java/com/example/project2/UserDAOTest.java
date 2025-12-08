package com.example.project2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.project2.database.CareerNestDatabase;
import com.example.project2.database.UserDAO;
import com.example.project2.database.entities.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

/**
 * @author Marissa Benenati
 * <br>COURSE: CST 338 - Software Design
 * <br>DATE: 12/5/2025
 * <br>ASSIGNMENT: Project 02
 * <br>==========================
 * <br>DESCRIPTION: This file contains tests for the User table in CareerNestDatabase.
 * */
@RunWith(AndroidJUnit4.class)
public class UserDAOTest{
    private CareerNestDatabase db;
    private UserDAO userDAO;
    /// ======================================
    User testUser1;
    User testUser2;
    User testUser3;
    User testUser4;
    String pswd = "multipass";
    static String TAG = "UserDAO_Test";

    @Before
    public void createDb(){
        Context context = ApplicationProvider.getApplicationContext();

        db = Room.inMemoryDatabaseBuilder(context, CareerNestDatabase.class)
                .allowMainThreadQueries()
                .build();

        userDAO = db.userDAO();

        testUser1 = new User("johndoe", "password");
        testUser2 = new User("alicesmith", "passwordA");
        testUser3 = new User("billybob", "passwordB");
        testUser4 = new User("charliebrown", "passwordC");
    }

    @After
    public void closeDb() throws IOException{
        db.close();
    }

    /**
     * Tests <code>INSERT</code> query.
     */
    @Test
    public void insertAndGetUser(){
        int preInsert = userDAO.getAllUsersList().size();
        userDAO.insert(testUser1);
        int postInsert = userDAO.getAllUsersList().size();
        assertEquals(preInsert+1, postInsert);

        User retrievedUser = userDAO.getUserByUserNameAlt("johndoe");
        assertNotNull("Retrieved user is null", retrievedUser);
        assertEquals("johndoe", retrievedUser.getUsername());
        assertEquals("password", retrievedUser.getPassword());
    }

    /**
     * Inserts multiple users, retrieves all, then checks count retrieved.
     */
    @Test
    public void insertMultUsersAndGetAll(){
        userDAO.insert(testUser2);
        userDAO.insert(testUser3);
        userDAO.insert(testUser4);

        List<User> allUsers = userDAO.getAllUsersList();
        assertNotNull("Users list is null", allUsers);

        assertEquals(3, allUsers.size());
    }

    /**
     * Tests <code>UPDATE</code> query. Checks both username and password updates.
     */
    @Test
    public void updateUser(){
        /// Test for username update
        userDAO.insert(testUser1);
        User retrievedUser = userDAO.getUserByUserNameAlt("johndoe");
        assertNotNull("Retrieved user is null", retrievedUser);
        retrievedUser.setUsername("korbendallas");
        userDAO.update(retrievedUser);

        User updatedUser = userDAO.getUserByUserNameAlt("korbendallas");
        assertNotNull("Updated user is null", updatedUser);
        assertEquals("korbendallas", updatedUser.getUsername());


        /// Test for password update
        userDAO.insert(testUser4);
        User retrievedUser2 = userDAO.getUserByUserNameAlt("charliebrown");
        assertNotNull("Retrieved user 2 is null", retrievedUser2);
        retrievedUser2.setPassword(pswd);
        userDAO.update(retrievedUser2);

        User updatedUser2 = userDAO.getUserByUserNameAlt("charliebrown");
        assertNotNull("Updated user 2 is null", updatedUser2);
        assertEquals("multipass", updatedUser2.getPassword());
    }

    /**
     * Tests <code>DELETE</code> query for a single user.
     */
    @Test
    public void deleteUser(){
        int preInsert = userDAO.getAllUsersList().size();
        userDAO.insert(testUser2);
        User retrievedUser = userDAO.getUserByUserNameAlt(testUser2.getUsername());
        assertNotNull("Retrieved user is null", retrievedUser);
        int postInsert = userDAO.getAllUsersList().size();
        assertEquals(preInsert+1, postInsert);

        userDAO.delete(retrievedUser);
        int postDelete = userDAO.getAllUsersList().size();
        assertEquals(postInsert-1, postDelete);
    }
}
