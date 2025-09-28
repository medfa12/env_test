package com.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    void testAddPositiveIntegers() {
        assertEquals(8, calculator.add(3, 5));
        assertEquals(0, calculator.add(5, -5));
    }

    @Test
    void testAddDoubles() {
        assertEquals(8.7, calculator.add(3.2, 5.5), 0.0001);
        assertEquals(0.0, calculator.add(5.5, -5.5), 0.0001);
    }

    @ParameterizedTest
    @CsvSource({
        "1, 1, 2",
        "10, 20, 30",
        "-5, 5, 0",
        "-10, -20, -30"
    })
    void testParameterizedAddition(int a, int b, int expected) {
        assertEquals(expected, calculator.add(a, b));
    }

    @Test
    void testMultiplyIntegers() {
        assertEquals(15, calculator.multiply(3, 5));
        assertEquals(-15, calculator.multiply(3, -5));
        assertEquals(0, calculator.multiply(5, 0));
    }

    @Test
    void testMultiplyDoubles() {
        assertEquals(17.6, calculator.multiply(3.2, 5.5), 0.0001);
        assertEquals(-17.6, calculator.multiply(3.2, -5.5), 0.0001);
    }

    @Test
    void testIntegerAdditionOverflow() {
        ArithmeticException exception = assertThrows(
            ArithmeticException.class,
            () -> calculator.add(Integer.MAX_VALUE, 1)
        );
        assertTrue(exception.getMessage().contains("overflow"));
    }

    @Test
    void testIntegerAdditionUnderflow() {
        ArithmeticException exception = assertThrows(
            ArithmeticException.class,
            () -> calculator.add(Integer.MIN_VALUE, -1)
        );
        assertTrue(exception.getMessage().contains("underflow"));
    }

    @Test
    void testAddNaNDoubles() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> calculator.add(Double.NaN, 5.0)
        );
        assertEquals("Cannot add NaN values", exception.getMessage());
    }

    @Test
    void testAddInfiniteDoubles() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> calculator.add(Double.POSITIVE_INFINITY, 5.0)
        );
        assertEquals("Cannot add infinite values", exception.getMessage());
    }

    @Test
    void testIntegerMultiplicationOverflow() {
        ArithmeticException exception = assertThrows(
            ArithmeticException.class,
            () -> calculator.multiply(Integer.MAX_VALUE, 2)
        );
        assertTrue(exception.getMessage().contains("overflow"));
    }

    @Test
    void testMultiplyNaNDoubles() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> calculator.multiply(Double.NaN, 5.0)
        );
        assertEquals("Cannot multiply NaN values", exception.getMessage());
    }

    @Test
    void testBoundaryValues() {
        assertEquals(Integer.MAX_VALUE - 1, calculator.add(Integer.MAX_VALUE, -1));
        assertEquals(Integer.MIN_VALUE + 1, calculator.add(Integer.MIN_VALUE, 1));
        assertEquals(Integer.MAX_VALUE, calculator.multiply(Integer.MAX_VALUE, 1));
    }

    @Test
    void testDoubleInfinityMultiplication() {
        assertEquals(Double.POSITIVE_INFINITY, calculator.multiply(Double.POSITIVE_INFINITY, 2.0));
        assertEquals(Double.NEGATIVE_INFINITY, calculator.multiply(Double.POSITIVE_INFINITY, -2.0));
    }
}