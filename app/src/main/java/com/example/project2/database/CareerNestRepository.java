package com.example.project2.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.project2.database.entities.JobLog;
import com.example.project2.database.entities.Reminder;
import com.example.project2.database.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Lesly Villanueva
 * <br>COURSE: CST 338 - Software Design
 * <br>DATE: 11/14/2025
 * <br>ASSIGNMENT: Project 02
 */
public class CareerNestRepository {

    private static final String TAG = "CAREER_NEST_REPO";
    private final JobLogDAO jobLogDAO;
    private final UserDAO userDAO;
    private final ReminderDAO reminderDAO;
    private ArrayList<JobLog> allLogs;
    private static CareerNestRepository repository;

    public CareerNestRepository(Application application){
        CareerNestDatabase db = CareerNestDatabase.getDatabase(application);
        this.jobLogDAO = db.jobLogDAO();
        this.userDAO = db.userDAO();
        this.allLogs = (ArrayList<JobLog>) this.jobLogDAO.getAllRecords();
        reminderDAO = null;
    }

    public static CareerNestRepository getRepository(Application application){
        if(repository != null){
            return repository;
        }

        Future<CareerNestRepository> future = CareerNestDatabase.databaseWriteExecutor.submit(
                new Callable<CareerNestRepository>() {
                    @Override
                    public CareerNestRepository call() throws Exception {
                        return new CareerNestRepository(application);
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e){
            Log.d(TAG, "Problem getting CareerNestRepository, thread error.");
        }
        return null;
    }

    public ArrayList<JobLog> getAllLogs(){
        Future<ArrayList<JobLog>> future = CareerNestDatabase.databaseWriteExecutor.submit(
                new Callable<ArrayList<JobLog>>() {
                    @Override
                    public ArrayList<JobLog> call() throws Exception {
                        return (ArrayList<JobLog>) jobLogDAO.getAllRecords();
                    }
                }
        );
        try{
            return future.get();
        }catch (InterruptedException | ExecutionException e){
            Log.i(TAG, "Problem when getting all JobLogs in the repository.");
        }
        return null;
    }

    public void insertJobLog(JobLog jobLog){
        CareerNestDatabase.databaseWriteExecutor.execute(() -> {
            jobLogDAO.insert(jobLog);
        });
    }

    public void deleteJobLog(JobLog jobLog){
        CareerNestDatabase.databaseWriteExecutor.execute(() -> {
            jobLogDAO.delete(jobLog);
        });
    }

    public void insertUser(User... user){
        CareerNestDatabase.databaseWriteExecutor.execute(() -> {
            userDAO.insert(user);
        });
    }

    public LiveData<User> getUserByUserName(String username) {
        return userDAO.getUserByUserName(username);
    }

    public LiveData<User> getUserByUserId(int userId) {
        return userDAO.getUserByUserId(userId);
    }

    public LiveData<List<JobLog>> getAllLogsByUserIdLiveData (int loggedInUserId) {
        return jobLogDAO.getRecordsByUserIdLiveData(loggedInUserId);
    }

    // === Add these to CareerNestRepository ===

    // Return all users as LiveData (RecyclerView observes this)
    public LiveData<List<User>> getAllUsers() {
        return userDAO.getAllUsers();
    }
    public void insertUser(User user){
        CareerNestDatabase.databaseWriteExecutor.execute(()->userDAO.insert(user)
        );
    }
    // Promote/update a user
    public void updateUser(User user) {
        CareerNestDatabase.databaseWriteExecutor.execute(() -> userDAO.update(user));
    }

    // Delete a user
    public void deleteUser(User user) {
        CareerNestDatabase.databaseWriteExecutor.execute(() -> userDAO.delete(user));
    }


    //reminder methods
    public void insertReminder(Reminder... reminder){
        CareerNestDatabase.databaseWriteExecutor.execute(() -> {
            reminderDAO.insert(reminder);
        });
    }
    public void deleteReminder(Reminder reminder){
        CareerNestDatabase.databaseWriteExecutor.execute(() -> {
            reminderDAO.delete(reminder);
        });
    }
    public void updateReminder(Reminder reminder){
        CareerNestDatabase.databaseWriteExecutor.execute(() -> {
            reminderDAO.update(reminder);
        });
    }

    public LiveData<List<Reminder>> getAllReminders() {
        return reminderDAO.getAllReminders();
    }
    public void deleteAll() {
        CareerNestDatabase.databaseWriteExecutor.execute(reminderDAO::deleteAll);
    }

    public LiveData<List<Reminder>> getRemindersByUserId(int userId){
        return reminderDAO.getRemindersByUserId(userId);
    }

    public LiveData<Reminder> getReminderById(int reminderId){
        return reminderDAO.getReminderById(reminderId);
    }




    //unsure of whether or not to include
//    public ArrayList<JobLog> getAllLogsByUserId(int loggedInUserId) {
//        Future<ArrayList<JobLog>> future = CareerNestDatabase.databaseWriteExecutor.submit(
//                new Callable<ArrayList<JobLog>>() {
//                    @Override
//                    public ArrayList<JobLog> call() throws Exception {
//                        return (ArrayList<JobLog>) jobLogDAO.getRecordsByUserId(loggedInUserId);
//                    }
//                }
//        );
//        try{
//            return future.get();
//        } catch (InterruptedException | ExecutionException e){
//            Log.i(TAG, "Problem when getting all GymLogs in the repository");
//        }
//        return null;
//    }
}
