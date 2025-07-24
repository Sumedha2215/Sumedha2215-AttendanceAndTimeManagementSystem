package com.timeattendance.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("laborers")
public class Laborer {
    @Id
    private String id;
    private String name;
    private String role; // e.g., Mason, Electrician
    private String contact;
    private double dailyWage;

    // Getters and Setters
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

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }

    public double getDailyWage() {
        return dailyWage;
    }
    public void setDailyWage(double dailyWage) {
        this.dailyWage = dailyWage;
    }
}
