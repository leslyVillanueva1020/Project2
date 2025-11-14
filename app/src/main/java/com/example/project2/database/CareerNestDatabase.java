package com.example.project2.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.project2.database.entities.JobLog;
import com.example.project2.database.entities.User;
import com.example.project2.database.typeConverters.LocalDateTypeConverter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Marissa Benenati
 * <br>COURSE: CST 338 - Software Design
 * <br>DATE: 11/13/2025
 * <br>ASSIGNMENT: Project 02
 */
@TypeConverters (LocalDateTypeConverter.class)
@Database(entities = {User.class, JobLog.class}, version = 1, exportSchema = false)
public abstract class CareerNestDatabase extends RoomDatabase{

    public static final String USER_TABLE = "UserTable";
    private static final String DATABASE_NAME = "CareerNestDatabase";
    public static final String JOB_LOG_TABLE = "JobLogTable";

    private static volatile CareerNestDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static CareerNestDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (CareerNestDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    CareerNestDatabase.class,
                                    DATABASE_NAME
                            )
                            .fallbackToDestructiveMigration()
                            .addCallback(addDefaultValues)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback addDefaultValues = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
            Log.i("CareerNestDatabase.java", "DATABASE CREATED!");
            databaseWriteExecutor.execute(() -> {
                UserDAO dao = INSTANCE.userDAO();
                dao.deleteAll();
                // add predefined users
                User admin = new User("admin2", "admin2");
                admin.setAdmin(true);
                dao.insert(admin);
                User testUser1 = new User("testuser1", "testuser1");
                dao.insert(testUser1);
            });
        }
    };
    public abstract UserDAO userDAO();
    public abstract JobLogDAO jobLogDAO();
}
