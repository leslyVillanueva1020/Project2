package com.example.project2;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class IntentTests {

    // LoginActivity intent test
    @Test
    public void loginIntentFactory() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = LoginActivity.loginIntentFactory(context);

        assertNotNull(intent);
        assertEquals(LoginActivity.class.getName(),
                intent.getComponent().getClassName());
    }

    // SignUpActivity intent test
    @Test
    public void signUpIntentFactory() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = SignUpActivity.signUpIntentFactory(context);

        assertNotNull(intent);
        assertEquals(SignUpActivity.class.getName(),
                intent.getComponent().getClassName());
    }

    // LandingActivity intent test
    @Test
    public void landingIntentFactory() {
        Context context = ApplicationProvider.getApplicationContext();
        int userId = 42;

        Intent intent = LandingActivity.landingIntentFactory(context, userId);

        assertNotNull(intent);
        assertEquals(LandingActivity.class.getName(),
                intent.getComponent().getClassName());

        int actualUserId =
                intent.getIntExtra("com.example.project2.EXTRA_USER_ID", -1);
        assertEquals(userId, actualUserId);
    }

    // AllApplicationsActivity intent test
    @Test
    public void allApplicationsIntentFactory() {
        Context context = ApplicationProvider.getApplicationContext();
        Intent intent = AllApplicationsActivity.allApplicationsIntentFactory(context);

        assertNotNull(intent);
        assertEquals(AllApplicationsActivity.class.getName(),
                intent.getComponent().getClassName());
    }

    // NewApplicationActivity intent test
    @Test
    public void newAppIntentFactory() {
        Context context = ApplicationProvider.getApplicationContext();
        int userId = 44;

        Intent intent = NewApplicationActivity.newAppIntentFactory(context, userId);

        assertNotNull(intent);
        assertEquals(NewApplicationActivity.class.getName(),
                intent.getComponent().getClassName());

        int actualUserId = intent.getIntExtra("EXTRA_USER_ID", -1);
        assertEquals(userId, actualUserId);
    }

    //EditEntryActivity intent test
    @Test
    public void editEntryIntentFactory(){
        Context context = ApplicationProvider.getApplicationContext();
        int userId = 7;
        int jobId = 123;

        Intent intent = EditEntryActivity.editEntryIntentFactory(context, userId, jobId);

        assertNotNull(intent);

        //correct target activity
        assertEquals(EditEntryActivity.class.getName(), intent.getComponent().getClassName());

        // Correct extras
        int actualUserId = intent.getIntExtra("com.example.project2.EXTRA_USER_ID", -1);
        int actualJobId = intent.getIntExtra("EXTRA_JOB_ID", -1);

        assertEquals(userId, actualUserId);
        assertEquals(jobId, actualJobId);

    }

    //ReminderActivity intent test
    @Test
    public void reminderIntentFactory(){
        Context context = ApplicationProvider.getApplicationContext();
        int userId = 5;
        int applicationId = 23;

        Intent intent = ReminderActivity.reminderIntentFactory(context, userId, applicationId);

        assertNotNull(intent);
        // Correct target activity
        assertEquals(ReminderActivity.class.getName(), intent.getComponent().getClassName());

        // Correct extras
        int actualUserId =
                intent.getIntExtra("EXTRA_USER_ID", -1);
        int actualApplicationId =
                intent.getIntExtra("EXTRA_APPLICATION_ID", -1);

        assertEquals(userId, actualUserId);
        assertEquals(applicationId, actualApplicationId);
    }
}
