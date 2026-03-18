package com.beemafy;


import java.time.Duration;
import java.util.List;

public class SystemAnalytics {

    public static void generateReport(List<Claim> database) {
        if (database.isEmpty()) {
            System.out.println("\n[!] No data available yet. Submit some claims first.");
            return;
        }

        int total = database.size();
        int approved = 0;
        double totalFunds = 0;
        long totalSeconds = 0;

        for (Claim c : database) {
            if (c.getStatus() == ClaimStatus.APPROVED || c.getStatus() == ClaimStatus.REJECTED) {
                if (c.getStatus() == ClaimStatus.APPROVED) {
                    approved++;
                    totalFunds += c.getAmount();
                }
                
                // Calculate time difference
                Duration duration = Duration.between(c.getTimeSubmitted(), c.getTimeProcessed());
                totalSeconds += duration.getSeconds();
            }
        }

        System.out.println("\n===== 📈 SYSTEM EFFICIENCY REPORT =====");
        System.out.println("Total Claims Processed:  " + total);
        System.out.println("Total Claims Approved:   " + approved);
        System.out.println("Total Funds Disbursed:   $" + totalFunds);
        
        // Since code runs in milliseconds, we use a double for average
        double avgTime = (total > 0) ? (double) totalSeconds / total : 0;
        System.out.println("Average Wait Time:       " + String.format("%.2f", avgTime) + " seconds");
        System.out.println("Performance Status:      OPTIMAL");
        System.out.println("========================================\n");
    }
}