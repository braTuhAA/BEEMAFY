package com.beemafy;


import java.util.*;


public class InsuranceSystem {
    private static List<Claim> claimDatabase = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    
    // Create a dummy patient for this simulation
    private static Patient currentPatient = new Patient("JohnDoe", "P-101", 5000.0);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n========================================");
            System.out.println("   HEALTH-LINK: CLAIMS PORTAL 2026");
            System.out.println("========================================");
            System.out.println("1. HOSPITAL: Submit New Claim");
            System.out.println("2. INSURANCE: Process Pending Claims");
            System.out.println("3. PATIENT: Track My Claims");
            System.out.println("4. ADMIN: View System Analytics");
            System.out.println("5. EXIT");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear buffer

            switch (choice) {
                case 1 -> hospitalPortal();
                case 2 -> insurancePortal();
                case 3 -> patientPortal();
                case 4 -> SystemAnalytics.generateReport(claimDatabase);
                case 5 -> {
                    System.out.println("Shutting down system...");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void hospitalPortal() {
        System.out.println("\n--- HOSPITAL PORTAL ---");
        System.out.println("Available Treatments:");
        
        // Using the TreatmentData class to list options
        List<String> treatments = new ArrayList<>(TreatmentData.getMenu().keySet());
        for (int i = 0; i < treatments.size(); i++) {
            System.out.println((i + 1) + ". " + treatments.get(i) + " ($" + TreatmentData.getCost(treatments.get(i)) + ")");
        }

        System.out.print("Select Treatment Number: ");
        int tChoice = scanner.nextInt() - 1;
        
        if (tChoice >= 0 && tChoice < treatments.size()) {
            String name = treatments.get(tChoice);
            double cost = TreatmentData.getCost(name);
            
            Claim newClaim = new Claim(currentPatient.getUsername(), name, cost);
            claimDatabase.add(newClaim);
            System.out.println("SUCCESS: Claim " + newClaim.getClaimId() + " submitted to Insurance.");
        }
    }

    private static void insurancePortal() {
        System.out.println("\n--- INSURANCE REVIEW PORTAL ---");
        boolean found = false;

        for (Claim c : claimDatabase) {
            if (c.getStatus() == ClaimStatus.SUBMITTED) {
                found = true;
                System.out.println("Reviewing: " + c);
                
                // Automation: If within policy, auto-approve
                if (currentPatient.canCover(c.getAmount())) {
                    c.updateStatus(ClaimStatus.APPROVED);
                    currentPatient.addApprovedAmount(c.getAmount());
                    System.out.println("RESULT: [AUTO-APPROVED] based on policy limits.");
                } else {
                    c.updateStatus(ClaimStatus.REJECTED);
                    System.out.println("RESULT: [REJECTED] - Insufficient Coverage.");
                }
            }
        }
        if (!found) System.out.println("No pending claims to process.");
    }

    private static void patientPortal() {
        System.out.println("\n--- PATIENT TRACKING PORTAL ---");
        System.out.println("Remaining Coverage: $" + currentPatient.getRemainingCoverage());
        System.out.println("Claim History:");
        for (Claim c : claimDatabase) {
            System.out.println("- " + c.getClaimId() + " | " + c.getTreatmentName() + " | Status: " + c.getStatus());
        }
    }
}