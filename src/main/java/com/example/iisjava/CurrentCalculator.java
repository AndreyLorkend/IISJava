package com.example.iisjava;

public class CurrentCalculator {
    private final int K = 1; // Диэлектрическая постоянная
    private final double PHI = 4.5; // Локальный выход электронов
    private final double EF = 5.71; // Уровень Ферми

    public double calculateJ(double x, double y, double z, double u) {
        double currentZ = calculateZ(x, y, z);
        double s1 = 3 / (K * PHI);
        double s2 = currentZ * (1 - (23 / (((3 * PHI * K * currentZ) + 10) - (2 * u * K * currentZ)))) + s1;
        double j = 1620 * u * EF * Math.exp(-1.025 * currentZ * Math.sqrt(calculateAvgPhi(currentZ, s1, s2, u)));
        return j;
    }

    private double calculateZ(double x, double y, double z) {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }

    private double calculateAvgPhi(double z, double s1, double s2, double u) {
        double a = (u * (s1 + s2)) / (2 * z);
        double b = 2.86 / (K * (s2 - s1));
        double c = Math.log((s2 * (z - s1)) / (s1 * (z - s2)));
        double result = PHI - a - (b * c);
        return result;
    }

}
