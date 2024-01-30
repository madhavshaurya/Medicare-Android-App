package com.example.medicalapp;

public class TestItem {
    private String testName;
    private String description;
    private int price;

    // Default constructor (no-argument constructor) is required for Firebase
    public TestItem() {
        // Empty constructor for Firebase deserialization
    }

    public TestItem(String testName, String description, int price) {
        this.testName = testName;
        this.description = description;
        this.price = price;
    }

    public String getTestName() {
        return testName;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }
}
