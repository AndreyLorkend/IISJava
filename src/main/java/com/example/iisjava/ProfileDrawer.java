package com.example.iisjava;

import com.example.iisjava.Point;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProfileDrawer implements Runnable {
    private ArrayList<Point> _researchArea = new ArrayList<Point>();

    private MonteCarloIntegral integral;
    private final int N = 10000;
    private final int SEED = 65539;
    private final double MAX_RANDOM = 32767.0;
    private RandomGenerator randomGenerator = new RandomGenerator(SEED);
    private Canvas canvas;
    private GraphicsContext ctx;
    private double z0 = 5;
    private double u0 = 0.01;
    private double[][] randomValues;

    ProfileDrawer(Canvas canvas) {
        this.canvas = canvas;
        this.ctx = canvas.getGraphicsContext2D();
        this.initCoordinates();
        this.initCalculationParams();
    }

    private void initCoordinates() {
        this._researchArea.add(new Point(0.0, 0.0));
        this._researchArea.add(new Point(10.0, 0.0));
        this._researchArea.add(new Point(10.0, 5.0));
        this._researchArea.add(new Point(26.0, 5.0));
        this._researchArea.add(new Point(30.0, 0.0));
        this._researchArea.add(new Point(33.0, 0.0));
        this._researchArea.add(new Point(33.0, 10.0));
        this._researchArea.add(new Point(43.0, 10.0));
    }

    private void initCalculationParams() {
        this.randomValues = new double[N][2];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < 2; j++) {
                this.randomValues[i][j] = (double)this.randomGenerator.next() / this.MAX_RANDOM;
            }
        }
        this.integral = new MonteCarloIntegral(this.randomValues, N);
    }

    private double calculateNewJ(double xp, double zp, double U) {
	    final int MAX = 100;
        double xH = 0.0, xL = 0.0, yH = 10.0, yL = -10.0;
        double Z = 0.0;
        double j = 0.0;
        boolean visiblePoints[] = new boolean[7];

        for (int i = 0; i < 7; i++) {
            visiblePoints[i] = false;
            if ((Math.abs(this._researchArea.get(i).getX() - xp) <= MAX) && ((this._researchArea.get(i).getY() - zp) <= MAX)
            && (Math.abs(this._researchArea.get(i+1).getX() - xp) <= MAX) && ((this._researchArea.get(i+1).getY() - zp) <= MAX)) {
                visiblePoints[i] = true;
            }
        }

        if (visiblePoints[0] && (zp > 0)) {
            xL = this._researchArea.get(0).getX() - xp;
            if (xL > MAX) xL = MAX;
            xH = this._researchArea.get(1).getX() - xp;
            if (xH > MAX) xH = MAX;
            Z = zp;
            j += this.integral.calculateIntegral(xL, xH, yL, yH, Z, U);
        }

        if (visiblePoints[1] && (xp < this._researchArea.get(1).getX()))
        {
            xL = this._researchArea.get(1).getY() - zp;
            if (xL > MAX) xL = MAX;
            xH = this._researchArea.get(2).getY() - zp;
            if (xH > MAX) xH = MAX;
            Z = this._researchArea.get(1).getX() - xp;
            j += integral.calculateIntegral(xL, xH, yL, yH, Z, U);
        }

        if (visiblePoints[2] && (zp > this._researchArea.get(2).getY())) {
            xL = this._researchArea.get(2).getX() - xp;
            if (xL > MAX) xL = MAX;
            xH = this._researchArea.get(3).getX() - xp;
            if (xH > MAX) xH = MAX;
            Z = zp - this._researchArea.get(2).getY();
            j += integral.calculateIntegral(xL, xH, yL, yH, Z, U);
        }

        if (visiblePoints[3] && (zp > this._researchArea.get(4).getX() - xp)) {
            Z = (zp - (this._researchArea.get(4).getX() - xp)) * Math.cos(Math.PI / 4);
            xL = (this._researchArea.get(3).getX() - (xp - Z * Math.cos(Math.PI / 4))) / Math.cos(Math.PI / 4);
            if (xL > MAX) xL = MAX;
            xH = (this._researchArea.get(4).getX() - (xp - Z * Math.cos(Math.PI / 4))) / Math.cos(Math.PI / 4);
            if (xH > MAX) xH = MAX;
            j += integral.calculateIntegral(xL, xH, yL, yH, Z, U);
        }

        if (visiblePoints[4] && (zp > this._researchArea.get(4).getY())) {
            xL = this._researchArea.get(4).getX() - xp;
            if (xL > MAX) xL = MAX;
            xH = this._researchArea.get(5).getX() - xp;
            if (xH > MAX) xH = MAX;
            Z = zp - this._researchArea.get(4).getY();
            j += integral.calculateIntegral(xL, xH, yL, yH, Z, U);
        }

        if (visiblePoints[5] && (xp < this._researchArea.get(5).getX())) {
            xL = this._researchArea.get(5).getY() - zp;
            if (xL > MAX) xL = MAX;
            xH = this._researchArea.get(6).getY() - zp;
            if (xH > MAX) xH = MAX;
            Z = this._researchArea.get(5).getX() - xp;
            j += integral.calculateIntegral(xL, xH, yL, yH, Z, U);
        }

        if (visiblePoints[6] && (zp > this._researchArea.get(6).getY())) {
            xL = this._researchArea.get(6).getX() - xp;
            if (xL > MAX) xL = MAX;
            xH = this._researchArea.get(7).getX() - xp;
            if (xH > MAX) xH = MAX;
            Z = zp - this._researchArea.get(7).getY();
            j += integral.calculateIntegral(xL, xH, yL, yH, Z, U);
        }

        return j;
    }

    public void drawProfile(double Z0, double U0) {
        final double e = 0.05;
        final double step = 0.1;

        double j0 = 0.0;
        double z[] = {0.0, 0.0, 0.0};
        double j[] = {0.0, 0.0, 0.0};
        ArrayList<Point> diagram = new ArrayList<Point>();
        for(int i = 0; i < 400; i++) {
            diagram.add(new Point(0, 0));
        }

        diagram.get(0).setX(0);
        diagram.get(0).setY(Z0);

        for (int i = 0; i < 10; i++) {
            j0 += this.calculateNewJ(0, Z0, U0) / 10.0;
        }

        this.ctx.fillOval(diagram.get(0).getX() * 11 + 5, 280 - diagram.get(0).getY() * 11, 1, 1);
        for (int i = 1; i < 400; i++) {
            diagram.get(i).setX(diagram.get(i-1).getX() + step);
            z[2] = diagram.get(i-1).getY();
            j[1] = this.calculateNewJ(diagram.get(i).getX(), z[2], U0);
            if ((j[1] < (j0 * (1 - e))) || (j[1] > (j0 * (1 + e)))) {
                z[1] = z[2] + step * ((j[1] - j0) / Math.abs(j[1] - j0));
                j[0] = this.calculateNewJ(diagram.get(i).getX(), z[1], U0);

                if ((j[0] < (j0 * (1 - e))) || (j[0] > (j0 * (1 + e)))) {
                    int k = 0;
                    do {
                        z[0] = z[1] - (j[0] - j0) * (z[1] - z[2]) / (j[0] - j[1]);
                        j[1] = j[0];
                        j[0] = calculateNewJ(diagram.get(i).getX(), z[0], U0);
                        z[2] = z[1];
                        z[1] = z[0];
                        k++;

                        if (k > 1000) {
                            break;
                        }

                    } while (!(j[0] >= j0 * (1 - e) && (j[0] <= j0 * (1 + e))));

                    diagram.get(i).setY(z[0]);

                    if (k > 1000) {
                        diagram.get(i).setY(diagram.get(i-1).getY());
                    }
                } else {
                    diagram.get(i).setY(z[1]);
                }
            } else {
                diagram.get(i).setY(z[2]);
            }

            this.ctx.fillOval(diagram.get(i).getX() * 11 + 5, 280 - diagram.get(i).getY() * 11, 1, 1);
        }
    }
    public void drawResearchArea() {
        this._researchArea.forEach((Point point) -> {
            this.ctx.lineTo(point.getX() * 11 + 5, 280 - point.getY() * 11);
            this.ctx.stroke();
        });
    }
    @Override
    public void run() {
        this.drawProfile(this.z0, this.u0);
        Thread.currentThread().interrupt();
    }
    public void setParams(double Z0, double U0) {
        this.z0 = Z0;
        this.u0 = U0;
    }
}
