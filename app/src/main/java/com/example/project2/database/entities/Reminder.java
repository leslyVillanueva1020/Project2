package com.example.project2.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.project2.database.CareerNestDatabase;

import java.time.LocalDateTime;
import java.util.Objects;

/*
 * @author Adrik Renteria
 * <br>COURSE: CST 338 - Software Design
 * <br>DATE: 12/6/2025
 * <br>ASSIGNMENT: Project 02
 * Description: this is the Reminder entity, it should store
 * a reminder associated to an application
 */
@Entity(tableName = CareerNestDatabase.REMINDER_TABLE)
public class Reminder {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int userId;
    private int applicationId;
    private LocalDateTime time;
    private String note;

    public Reminder(int userId, int applicationId, LocalDateTime time, String note) {
        this.userId = userId;
        this.applicationId = applicationId;
        this.time = time;
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Reminder reminder = (Reminder) o;
        return id == reminder.id && userId == reminder.userId && applicationId == reminder.applicationId && Objects.equals(time, reminder.time) && Objects.equals(note, reminder.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, applicationId, time, note);
    }

    @NonNull
    @Override
    public String toString() {
        return "Reminder ID" + id +
                "Reminder{" +
                "userId=" + userId +
                ", applicationId=" + applicationId +
                ", time=" + time +
                ", note='" + note + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
