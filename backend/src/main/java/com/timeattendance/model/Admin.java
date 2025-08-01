package com.timeattendance.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("admins")
public class Admin {
    @Id
    private String id;
    private String username;
    private String password;
    private String shift; // "Morning" or "Night"

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }
}
