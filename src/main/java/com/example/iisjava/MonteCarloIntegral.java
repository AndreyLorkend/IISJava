package com.example.iisjava;

public class MonteCarloIntegral {
    private double _randomValues[][];
    private CurrentCalculator _currentCalculator;
    private int _testCount;
    MonteCarloIntegral(double randomValues[][], int N) {
        this._randomValues = randomValues;
        this._currentCalculator = new CurrentCalculator();
        this._testCount = N;
    }

    public double calculateIntegral(double xL, double xH, double yL, double yH, double Z, double U)
    {
        double b[] = { xH, yH };
        double a[] = { xL, yL };
        double result = 0.0;
        double V = (b[0] - a[0]) * (b[1] - a[1]);
        double X[] = { 0.0, 0.0 };
        boolean flag;

        for (int i = 0; i < _testCount; i++)
        {
            for (int j = 0; j < 2; j++) {
                X[j] = a[j] + (b[j] - a[j]) * _randomValues[i][j];
            }

            flag = true;

            for (int j = 0; j < 2; j++) {
                if ((X[j] < a[j]) && (X[j]>b[j])) {
                    flag = false;
                }
            }

            if (flag) {
                result += this._currentCalculator.calculateJ(X[0], X[1], Z, U);
            }
        }

        return result * V / _testCount;
    }
}
