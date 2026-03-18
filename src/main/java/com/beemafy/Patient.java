package com.beemafy;


public class Patient extends User {
    private String patientId;
    private double policyLimit;
    private double amountUsed;

    public Patient(String username, String patientId, double policyLimit) {
        // 'super' calls the constructor of the User class
        super(username, "PATIENT");
        this.patientId = patientId;
        this.policyLimit = policyLimit;
        this.amountUsed = 0.0;
    }

    // Business Logic: Check if the patient has enough coverage left
    public boolean canCover(double amount) {
        return (amountUsed + amount) <= policyLimit;
    }

    // Update the balance after an approval
    public void addApprovedAmount(double amount) {
        this.amountUsed += amount;
    }

    // Getters
    public String getPatientId() { return patientId; }
    public double getRemainingCoverage() { return policyLimit - amountUsed; }
    public double getPolicyLimit() { return policyLimit; }
}