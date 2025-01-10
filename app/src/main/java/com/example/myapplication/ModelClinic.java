package com.example.myapplication;

public class ModelClinic {
    String id, name, location, time;

    // Empty constructor for Firestore deserialization
    public ModelClinic() {
    }

    // Constructor with parameters
    public ModelClinic(String id, String name, String location, String time) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.time = time;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

