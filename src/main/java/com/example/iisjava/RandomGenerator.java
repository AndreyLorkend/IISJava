package com.example.iisjava;

public class RandomGenerator {
    private int _x;
    private int _m;
    private int _a;
    RandomGenerator(int seed) {
        System.out.println("Init generator");
        this._x = seed;
        this._m = 32767; //2147483647
        this._a = 65539;
    }

    public int next() {
        this._x = (this._a * this._x) % this._m;
        return this._x;
    }
}
