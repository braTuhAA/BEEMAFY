package com.beemafy;

import java.util.HashMap;
import java.util.Map;

public class TreatmentData {
    private static final Map<String, Double> treatmentMenu = new HashMap<>();

    static {
        treatmentMenu.put("General Consultation", 150.00);
        treatmentMenu.put("Blood Test", 85.00);
        treatmentMenu.put("X-Ray", 250.00);
        treatmentMenu.put("MRI Scan", 1200.00);
        treatmentMenu.put("Surgery", 5000.00);
    }

    public static Map<String, Double> getMenu() {
        return treatmentMenu;
    }

    public static double getCost(String treatment) {
        return treatmentMenu.getOrDefault(treatment, 0.0);
    }
}