package com.example.Database;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.project2.database.CareerNestDatabase;
import com.example.project2.database.JobLogDAO;
import com.example.project2.database.entities.JobLog;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


    @RunWith(AndroidJUnit4.class)
    public class JobLogDAOTest {

        private CareerNestDatabase db;
        private JobLogDAO jobLogDAO;

        @Before
        public void setUp() {
            Context context = ApplicationProvider.getApplicationContext();
            db = Room.inMemoryDatabaseBuilder(context, CareerNestDatabase.class)
                    .allowMainThreadQueries()
                    .build();
            jobLogDAO = db.jobLogDAO();
        }
        @After
        public void tearDown() throws IOException {
            db.close();
        }

        // Tests the insert
        @Test
        public void insert_addsJobLog() {
            int beforeSize = jobLogDAO.getAllRecords().size();

            JobLog job = new JobLog(
                    "Google",
                    "Junior Web Developer",
                    "Applied",
                    LocalDateTime.of(2025, 1, 10, 12, 0),
                    1
            );

            jobLogDAO.insert(job);

            int afterSize = jobLogDAO.getAllRecords().size();
            assertEquals(beforeSize + 1, afterSize);
        }

        //Tests the update feature
        @Test
        public void insert_sameId_updatesJobLog_insteadOfDuplicating() {
            JobLog job = new JobLog(
                    "Google",
                    "Junior Web Developer",
                    "Applied",
                    LocalDateTime.of(2025, 1, 10, 12, 0),
                    1
            );

            // Insert first time
            jobLogDAO.insert(job);

            // Retrieve from DB to get auto-generated ID
            JobLog saved = jobLogDAO.getAllRecords().get(0);

            // Modify fields (update)
            saved.setPosition("Senior Web Developer");
            saved.setStatus("Interviewing");

            // Insert updated version -> REPLACE strategy updates the row
            jobLogDAO.insert(saved);

            // Verify only one row exists
            List<JobLog> result = jobLogDAO.getAllRecords();
            assertEquals(1, result.size());

            JobLog updated = result.get(0);

            assertEquals("Senior Web Developer", updated.getPosition());
            assertEquals("Interviewing", updated.getStatus());
        }

        // Tests the delete
        @Test
        public void delete_removesJobLog() {
            JobLog job = new JobLog(
                    "Google",
                    "Junior Web Developer",
                    "Applied",
                    LocalDateTime.of(2025, 1, 10, 12, 0),
                    1
            );
            jobLogDAO.insert(job);

            int beforeSize = jobLogDAO.getAllRecords().size();

            jobLogDAO.delete(jobLogDAO.getAllRecords().get(0));

            int afterSize = jobLogDAO.getAllRecords().size();
            assertEquals(beforeSize - 1, afterSize);
        }
    }


