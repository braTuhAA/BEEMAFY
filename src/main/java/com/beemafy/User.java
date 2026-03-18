package com.beemafy;

public class User {
    private String username;
    private String role; // e.g., "PATIENT", "HOSPITAL_STAFF", "INSURANCE_AGENT"

    public User(String username, String role) {
        this.username = username;
        this.role = role;
    }

    // Getters
    public String getUsername() { return username; }
    public String getRole() { return role; }

    public void displayProfile() {
        System.out.println("User: " + username + " | Role: " + role);
    }
}