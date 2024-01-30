package com.example.medicalapp;

import java.util.Locale;

public class Appointment {

    private String department;
    private String doctor;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private String reason;

    public Appointment() {
        // Default constructor required for Firebase
    }

    public Appointment(String department, String doctor, int year, int month, int day, int hour, int minute, String reason) {
        this.department = department;
        this.doctor = doctor;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.reason = reason;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    public String getFormattedDate() {
        // Format the date and time as needed
        String formattedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d %02d:%02d",
                year, month + 1, day, hour, minute);
        return formattedDate;
    }
    public String getFormattedTime() {
        // Format the time as needed
        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
        return formattedTime;
    }
}
