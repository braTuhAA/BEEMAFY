package com.beemafy;

public enum ClaimStatus {
    SUBMITTED,      // Initial state when hospital creates the claim
    UNDER_REVIEW,   // Insurance agent has opened the claim
    APPROVED,       // Claim verified and funds allocated
    REJECTED,       // Claim denied (e.g., policy mismatch)
    INFO_REQUIRED,  // Hospital needs to provide more documentation
    SETTLED         // Payment has been successfully sent to the hospital
}