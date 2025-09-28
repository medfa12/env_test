package com.calculator;

/**
 * Simple Calculator class with basic arithmetic operations.
 * Includes validation and exception handling for edge cases.
 */
public class Calculator {
    
    /**
     * Adds two integers.
     * 
     * @param a first operand
     * @param b second operand
     * @return sum of a and b
     * @throws ArithmeticException if the result would overflow
     */
    public int add(int a, int b) {
        // Check for overflow
        if (a > 0 && b > 0 && a > Integer.MAX_VALUE - b) {
            throw new ArithmeticException("Integer overflow in addition: " + a + " + " + b);
        }
        if (a < 0 && b < 0 && a < Integer.MIN_VALUE - b) {
            throw new ArithmeticException("Integer underflow in addition: " + a + " + " + b);
        }
        return a + b;
    }
    
    /**
     * Adds two double values.
     * 
     * @param a first operand
     * @param b second operand
     * @return sum of a and b
     * @throws IllegalArgumentException if either operand is NaN or infinite
     */
    public double add(double a, double b) {
        if (Double.isNaN(a) || Double.isNaN(b)) {
            throw new IllegalArgumentException("Cannot add NaN values");
        }
        if (Double.isInfinite(a) || Double.isInfinite(b)) {
            throw new IllegalArgumentException("Cannot add infinite values");
        }
        return a + b;
    }
    
    /**
     * Multiplies two integers.
     * 
     * @param a first operand
     * @param b second operand
     * @return product of a and b
     * @throws ArithmeticException if the result would overflow
     */
    public int multiply(int a, int b) {
        // Handle special cases
        if (a == 0 || b == 0) {
            return 0;
        }
        
        // Check for overflow
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
    
    /**
     * Multiplies two double values.
     * 
     * @param a first operand
     * @param b second operand
     * @return product of a and b
     * @throws IllegalArgumentException if either operand is NaN
     */
    public double multiply(double a, double b) {
        if (Double.isNaN(a) || Double.isNaN(b)) {
            throw new IllegalArgumentException("Cannot multiply NaN values");
        }
        return a * b;
    }
}

