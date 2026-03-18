package com.beemafy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Claim {
    // Static counter to ensure every claim gets a unique ID automatically
    private static int idCounter = 1000;

    private String claimId;
    private String patientName;
    private double amount;
    private String treatmentName;
    private ClaimStatus status;
    
    // Time tracking fields to solve the "Time Delay" problem
    private LocalDateTime timeSubmitted;
    private LocalDateTime timeProcessed;

    // Constructor used by the Hospital Portal
    public Claim(String patientName, String treatmentName, double amount) {
        this.claimId = "CLM-" + (idCounter++);
        this.patientName = patientName;
        this.treatmentName = treatmentName;
        this.amount = amount;
        this.status = ClaimStatus.SUBMITTED; // Initial state
        this.timeSubmitted = LocalDateTime.now();
    }

    /**
     * Updates the status and automatically records the processing time.
     * This allows us to calculate exactly how long the delay was.
     */
    public void updateStatus(ClaimStatus newStatus) {
        this.status = newStatus;
        this.timeProcessed = LocalDateTime.now();
    }

    // --- GETTERS ---
    public String getClaimId() { return claimId; }
    public String getPatientName() { return patientName; }
    public double getAmount() { return amount; }
    public String getTreatmentName() { return treatmentName; }
    public ClaimStatus getStatus() { return status; }
    public LocalDateTime getTimeSubmitted() { return timeSubmitted; }
    public LocalDateTime getTimeProcessed() { return timeProcessed; }

    /**
     * Helper method to print the time in a readable format for the UI
     */
    public String getFormattedSubmissionTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return timeSubmitted.format(formatter);
    }

    @Override
    public String toString() {
        return String.format("[%s] Patient: %s | Treatment: %s | Amount: $%.2f | Status: %s", 
                claimId, patientName, treatmentName, amount, status);
    }
}