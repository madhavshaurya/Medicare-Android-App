package com.example.medicalapp;

import java.util.Calendar;

public class HomeVisitRequest {
    private String department;
    private String doctor;
    private long timestamp; // Store date and time as a timestamp
    private String reason;

    public HomeVisitRequest() {
        // Default constructor required for Firebase
    }

    public HomeVisitRequest(String department, String doctor, int year, int month, int day, int hour, int minute, String reason) {
        this.department = department;
        this.doctor = doctor;
        // Calculate the timestamp from date and time components
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        this.timestamp = calendar.getTimeInMillis();
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
