package com.calculator;

public class Calculator {
    
    public int add(int a, int b) {
        if (a > 0 && b > 0 && a > Integer.MAX_VALUE - b) {
            throw new ArithmeticException("Integer overflow in addition: " + a + " + " + b);
        }
        if (a < 0 && b < 0 && a < Integer.MIN_VALUE - b) {
            throw new ArithmeticException("Integer underflow in addition: " + a + " + " + b);
        }
        return a + b;
    }
    
    public double add(double a, double b) {
        if (Double.isNaN(a) || Double.isNaN(b)) {
            throw new IllegalArgumentException("Cannot add NaN values");
        }
        if (Double.isInfinite(a) || Double.isInfinite(b)) {
            throw new IllegalArgumentException("Cannot add infinite values");
        }
        return a + b;
    }
    
    public int multiply(int a, int b) {
        if (a == 0 || b == 0) {
            return 0;
        }
        if (a > 0 && b > 0 && a > Integer.MAX_VALUE / b) {
            throw new ArithmeticException("Integer overflow in multiplication: " + a + " * " + b);
        }
        if (a < 0 && b < 0 && a < Integer.MAX_VALUE / b) {
            throw new ArithmeticException("Integer overflow in multiplication: " + a + " * " + b);
        }
        if ((a > 0 && b < 0 && b < Integer.MIN_VALUE / a) || 
            (a < 0 && b > 0 && a < Integer.MIN_VALUE / b)) {
            throw new ArithmeticException("Integer underflow in multiplication: " + a + " * " + b);
        }
        
        return a * b;
    }
    
    public double multiply(double a, double b) {
        if (Double.isNaN(a) || Double.isNaN(b)) {
            throw new IllegalArgumentException("Cannot multiply NaN values");
        }
        return a * b;
    }
}

