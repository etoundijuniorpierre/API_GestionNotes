package com.university.ManageNotes.util;

import com.university.ManageNotes.model.enums.AssessmentType;

public class GradeCalculator {
    
    // CC exam weight: 30%, SN exam weight: 70%
    private static final double CC_WEIGHT = 0.30;
    private static final double SN_WEIGHT = 0.70;
    
    /**
     * Calculate subject total score (CC + SN = 100)
     */
    public static double calculateSubjectTotal(Double ccScore, Double snScore) {
        double cc = (ccScore != null ? ccScore : 0) * CC_WEIGHT;
        double sn = (snScore != null ? snScore : 0) * SN_WEIGHT;
        return cc + sn;
    }
    
    /**
     * Convert score on 100 to GPA on 4.0 scale
     */
    public static double calculateGPA(double scoreOn100) {
        if (scoreOn100 < 35) return 0.0;
        if (scoreOn100 < 40) return 1.0;
        if (scoreOn100 < 45) return 1.3;
        if (scoreOn100 < 50) return 1.7;
        if (scoreOn100 < 55) return 2.0;
        if (scoreOn100 < 60) return 2.3;
        if (scoreOn100 < 65) return 2.7;
        if (scoreOn100 < 70) return 3.0;
        if (scoreOn100 < 75) return 3.3;
        if (scoreOn100 < 80) return 3.7;
        return 4.0; // 80-100
    }
    
    /**
     * Convert score on 100 to score on 20
     */
    public static double convertTo20Scale(double scoreOn100) {
        return scoreOn100 / 5.0;
    }
    
    /**
     * Calculate weighted GPA (GPA * credits)
     */
    public static double calculateWeightedGPA(double gpa, int credits) {
        return gpa * credits;
    }
    
    /**
     * Calculate weighted score on 20 (score * credits)
     */
    public static double calculateWeightedScore(double scoreOn20, int credits) {
        return scoreOn20 * credits;
    }
    
    /**
     * Check if student passed the subject (>= 50/100)
     */
    public static boolean hasPassed(double scoreOn100) {
        return scoreOn100 >= 50.0;
    }
    
    /**
     * Get exam weight based on assessment type
     */
    public static double getExamWeight(AssessmentType assessmentType) {
        return switch (assessmentType) {
            case CC_1, CC_2 -> CC_WEIGHT;
            case SN_1, SN_2 -> SN_WEIGHT;
        };
    }
}