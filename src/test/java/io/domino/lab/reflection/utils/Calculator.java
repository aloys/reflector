package io.domino.lab.reflection.utils;

/**
 * Created by amazimpaka on 2018-05-09
 */
public class Calculator {

    public static final String DIVIDE_BY_ZERO_ERROR = "Cannot divide by zero";

    private double pi;

    public static double add(double x, double y) {
        return x + y;
    }

    public static double minus(double x, double y) {
        return x - y;
    }

    public static double multiply(double x, double y) {
        return x * y;
    }

    public static double divide(double x, double y) {
        if(y == 0){
            throw new ArithmeticException(DIVIDE_BY_ZERO_ERROR);
        }
        return x / y;
    }

    public void setPi(double pi) {
        this.pi = pi;
    }

    public double getPi() {
        return pi;

    }
}
