package com.example.project2.viewHolders;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.project2.database.CareerNestRepository;
import com.example.project2.database.entities.JobLog;

import java.util.List;

import kotlinx.coroutines.Job;

public class JobLogViewModel extends AndroidViewModel {
    private final CareerNestRepository repository;
    public JobLogViewModel(Application application){
        super(application);
        repository = CareerNestRepository.getRepository(application);
    }

    public void insert(JobLog log){
        repository.insertJobLog(log);
    }

    public LiveData<List<JobLog>> getAllLogsById(int userId){
        return repository.getAllLogsByUserIdLiveData(userId);
    }

}
