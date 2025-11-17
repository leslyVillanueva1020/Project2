package com.example.project2.database.entities;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.project2.database.CareerNestDatabase;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Lesly Villanueva
 * <br>COURSE: CST 338 - Software Design
 * <br>DATE: 11/14/2025
 * <br>ASSIGNMENT: Project 02
 */
@Entity(tableName= CareerNestDatabase.JOB_LOG_TABLE)
public class JobLog {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String company;
    private String position;  //ex: full stack intern
    private String status; //ex applies, interviewed, offer, rejected
    private LocalDateTime dateApplied;
    private int userId; //foreign key to User

    public JobLog(String company, String position, String status, int userId) {
        this.company = company;
        this.position = position;
        this.status = status;
        this.userId = userId;
        dateApplied = LocalDateTime.now();
    }

    @NonNull
    @Override
    public String toString() {
        return company + '\n' +
                "Position: " + position + '\n' +
                "Status: " + status + '\n' +
                "Date Applied: " + dateApplied + '\n' +
                "=================================";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        JobLog jobLog = (JobLog) o;
        return id == jobLog.id && userId == jobLog.userId && Objects.equals(company, jobLog.company) && Objects.equals(position, jobLog.position) && Objects.equals(status, jobLog.status) && Objects.equals(dateApplied, jobLog.dateApplied);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, company, position, status, dateApplied, userId);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(LocalDateTime dateApplied) {
        this.dateApplied = dateApplied;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
