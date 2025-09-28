package com.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for Calculator class.
 * Tests both normal behavior and exception handling for addition and multiplication operations.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Calculator Tests")
class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Nested
    @DisplayName("Addition Tests")
    class AdditionTests {

        @Nested
        @DisplayName("Integer Addition - Normal Behavior")
        class IntegerAdditionNormal {

            @Test
            @DisplayName("Should add two positive integers")
            void testAddPositiveIntegers() {
                assertEquals(8, calculator.add(3, 5));
                assertEquals(100, calculator.add(25, 75));
            }

            @Test
            @DisplayName("Should add two negative integers")
            void testAddNegativeIntegers() {
                assertEquals(-8, calculator.add(-3, -5));
                assertEquals(-100, calculator.add(-25, -75));
            }

            @Test
            @DisplayName("Should add positive and negative integers")
            void testAddMixedIntegers() {
                assertEquals(2, calculator.add(5, -3));
                assertEquals(-2, calculator.add(-5, 3));
                assertEquals(0, calculator.add(5, -5));
            }

            @Test
            @DisplayName("Should add zero to integers")
            void testAddWithZero() {
                assertEquals(5, calculator.add(5, 0));
                assertEquals(5, calculator.add(0, 5));
                assertEquals(0, calculator.add(0, 0));
                assertEquals(-5, calculator.add(-5, 0));
            }

            @ParameterizedTest
            @CsvSource({
                "1, 1, 2",
                "10, 20, 30",
                "-5, 5, 0",
                "100, -50, 50",
                "-10, -20, -30"
            })
            @DisplayName("Should add integers correctly with parameterized tests")
            void testParameterizedAddition(int a, int b, int expected) {
                assertEquals(expected, calculator.add(a, b));
            }
        }

        @Nested
        @DisplayName("Double Addition - Normal Behavior")
        class DoubleAdditionNormal {

            @Test
            @DisplayName("Should add two positive doubles")
            void testAddPositiveDoubles() {
                assertEquals(8.7, calculator.add(3.2, 5.5), 0.0001);
                assertEquals(100.25, calculator.add(25.75, 74.5), 0.0001);
            }

            @Test
            @DisplayName("Should add two negative doubles")
            void testAddNegativeDoubles() {
                assertEquals(-8.7, calculator.add(-3.2, -5.5), 0.0001);
                assertEquals(-100.25, calculator.add(-25.75, -74.5), 0.0001);
            }

            @Test
            @DisplayName("Should add mixed positive and negative doubles")
            void testAddMixedDoubles() {
                assertEquals(1.8, calculator.add(5.3, -3.5), 0.0001);
                assertEquals(-1.8, calculator.add(-5.3, 3.5), 0.0001);
                assertEquals(0.0, calculator.add(5.5, -5.5), 0.0001);
            }

            @Test
            @DisplayName("Should add zero to doubles")
            void testAddDoublesWithZero() {
                assertEquals(5.5, calculator.add(5.5, 0.0), 0.0001);
                assertEquals(5.5, calculator.add(0.0, 5.5), 0.0001);
                assertEquals(0.0, calculator.add(0.0, 0.0), 0.0001);
            }
        }

        @Nested
        @DisplayName("Addition Exception Handling")
        class AdditionExceptions {

            @Test
            @DisplayName("Should throw ArithmeticException on integer overflow")
            void testIntegerAdditionOverflow() {
                ArithmeticException exception = assertThrows(
                    ArithmeticException.class,
                    () -> calculator.add(Integer.MAX_VALUE, 1)
                );
                assertTrue(exception.getMessage().contains("overflow"));
            }

            @Test
            @DisplayName("Should throw ArithmeticException on integer underflow")
            void testIntegerAdditionUnderflow() {
                ArithmeticException exception = assertThrows(
                    ArithmeticException.class,
                    () -> calculator.add(Integer.MIN_VALUE, -1)
                );
                assertTrue(exception.getMessage().contains("underflow"));
            }

            @Test
            @DisplayName("Should throw IllegalArgumentException when adding NaN doubles")
            void testAddNaNDoubles() {
                IllegalArgumentException exception1 = assertThrows(
                    IllegalArgumentException.class,
                    () -> calculator.add(Double.NaN, 5.0)
                );
                assertEquals("Cannot add NaN values", exception1.getMessage());

                IllegalArgumentException exception2 = assertThrows(
                    IllegalArgumentException.class,
                    () -> calculator.add(5.0, Double.NaN)
                );
                assertEquals("Cannot add NaN values", exception2.getMessage());

                IllegalArgumentException exception3 = assertThrows(
                    IllegalArgumentException.class,
                    () -> calculator.add(Double.NaN, Double.NaN)
                );
                assertEquals("Cannot add NaN values", exception3.getMessage());
            }

            @Test
            @DisplayName("Should throw IllegalArgumentException when adding infinite doubles")
            void testAddInfiniteDoubles() {
                IllegalArgumentException exception1 = assertThrows(
                    IllegalArgumentException.class,
                    () -> calculator.add(Double.POSITIVE_INFINITY, 5.0)
                );
                assertEquals("Cannot add infinite values", exception1.getMessage());

                IllegalArgumentException exception2 = assertThrows(
                    IllegalArgumentException.class,
                    () -> calculator.add(5.0, Double.NEGATIVE_INFINITY)
                );
                assertEquals("Cannot add infinite values", exception2.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("Multiplication Tests")
    class MultiplicationTests {

        @Nested
        @DisplayName("Integer Multiplication - Normal Behavior")
        class IntegerMultiplicationNormal {

            @Test
            @DisplayName("Should multiply two positive integers")
            void testMultiplyPositiveIntegers() {
                assertEquals(15, calculator.multiply(3, 5));
                assertEquals(1875, calculator.multiply(25, 75));
            }

            @Test
            @DisplayName("Should multiply two negative integers")
            void testMultiplyNegativeIntegers() {
                assertEquals(15, calculator.multiply(-3, -5));
                assertEquals(1875, calculator.multiply(-25, -75));
            }

            @Test
            @DisplayName("Should multiply positive and negative integers")
            void testMultiplyMixedIntegers() {
                assertEquals(-15, calculator.multiply(3, -5));
                assertEquals(-15, calculator.multiply(-3, 5));
            }

            @Test
            @DisplayName("Should multiply by zero")
            void testMultiplyByZero() {
                assertEquals(0, calculator.multiply(5, 0));
                assertEquals(0, calculator.multiply(0, 5));
                assertEquals(0, calculator.multiply(0, 0));
                assertEquals(0, calculator.multiply(-5, 0));
                assertEquals(0, calculator.multiply(0, -5));
            }

            @Test
            @DisplayName("Should multiply by one")
            void testMultiplyByOne() {
                assertEquals(5, calculator.multiply(5, 1));
                assertEquals(5, calculator.multiply(1, 5));
                assertEquals(-5, calculator.multiply(-5, 1));
                assertEquals(-5, calculator.multiply(1, -5));
            }

            @ParameterizedTest
            @CsvSource({
                "2, 3, 6",
                "4, 5, 20",
                "-2, 3, -6",
                "-2, -3, 6",
                "0, 100, 0",
                "7, 1, 7"
            })
            @DisplayName("Should multiply integers correctly with parameterized tests")
            void testParameterizedMultiplication(int a, int b, int expected) {
                assertEquals(expected, calculator.multiply(a, b));
            }
        }

        @Nested
        @DisplayName("Double Multiplication - Normal Behavior")
        class DoubleMultiplicationNormal {

            @Test
            @DisplayName("Should multiply two positive doubles")
            void testMultiplyPositiveDoubles() {
                assertEquals(17.6, calculator.multiply(3.2, 5.5), 0.0001);
                assertEquals(1931.25, calculator.multiply(25.75, 75.0), 0.0001);
            }

            @Test
            @DisplayName("Should multiply two negative doubles")
            void testMultiplyNegativeDoubles() {
                assertEquals(17.6, calculator.multiply(-3.2, -5.5), 0.0001);
                assertEquals(1931.25, calculator.multiply(-25.75, -75.0), 0.0001);
            }

            @Test
            @DisplayName("Should multiply mixed positive and negative doubles")
            void testMultiplyMixedDoubles() {
                assertEquals(-17.6, calculator.multiply(3.2, -5.5), 0.0001);
                assertEquals(-17.6, calculator.multiply(-3.2, 5.5), 0.0001);
            }

            @Test
            @DisplayName("Should multiply doubles by zero")
            void testMultiplyDoublesByZero() {
                assertEquals(0.0, calculator.multiply(5.5, 0.0), 0.0001);
                assertEquals(0.0, calculator.multiply(0.0, 5.5), 0.0001);
                assertEquals(0.0, calculator.multiply(-5.5, 0.0), 0.0001);
            }

            @Test
            @DisplayName("Should handle infinity in multiplication")
            void testMultiplyWithInfinity() {
                assertEquals(Double.POSITIVE_INFINITY, calculator.multiply(Double.POSITIVE_INFINITY, 2.0));
                assertEquals(Double.NEGATIVE_INFINITY, calculator.multiply(Double.POSITIVE_INFINITY, -2.0));
                assertEquals(Double.POSITIVE_INFINITY, calculator.multiply(Double.NEGATIVE_INFINITY, -2.0));
            }
        }

        @Nested
        @DisplayName("Multiplication Exception Handling")
        class MultiplicationExceptions {

            @Test
            @DisplayName("Should throw ArithmeticException on integer multiplication overflow")
            void testIntegerMultiplicationOverflow() {
                ArithmeticException exception = assertThrows(
                    ArithmeticException.class,
                    () -> calculator.multiply(Integer.MAX_VALUE, 2)
                );
                assertTrue(exception.getMessage().contains("overflow"));
            }

            @Test
            @DisplayName("Should throw ArithmeticException on large negative multiplication")
            void testIntegerMultiplicationUnderflow() {
                ArithmeticException exception = assertThrows(
                    ArithmeticException.class,
                    () -> calculator.multiply(Integer.MIN_VALUE / 2, 3)
                );
                assertTrue(exception.getMessage().contains("underflow") || 
                          exception.getMessage().contains("overflow"));
            }

            @Test
            @DisplayName("Should throw IllegalArgumentException when multiplying NaN doubles")
            void testMultiplyNaNDoubles() {
                IllegalArgumentException exception1 = assertThrows(
                    IllegalArgumentException.class,
                    () -> calculator.multiply(Double.NaN, 5.0)
                );
                assertEquals("Cannot multiply NaN values", exception1.getMessage());

                IllegalArgumentException exception2 = assertThrows(
                    IllegalArgumentException.class,
                    () -> calculator.multiply(5.0, Double.NaN)
                );
                assertEquals("Cannot multiply NaN values", exception2.getMessage());

                IllegalArgumentException exception3 = assertThrows(
                    IllegalArgumentException.class,
                    () -> calculator.multiply(Double.NaN, Double.NaN)
                );
                assertEquals("Cannot multiply NaN values", exception3.getMessage());
            }

            @ParameterizedTest
            @ValueSource(ints = {Integer.MAX_VALUE, Integer.MAX_VALUE - 1, 1000000})
            @DisplayName("Should handle potential overflow scenarios")
            void testOverflowScenarios(int value) {
                if (value > Integer.MAX_VALUE / 2) {
                    assertThrows(ArithmeticException.class, () -> calculator.multiply(value, 2));
                }
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases and Integration Tests")
    class EdgeCasesAndIntegration {

        @Test
        @DisplayName("Should handle maximum and minimum integer values correctly")
        void testBoundaryValues() {
            // Test with maximum values that don't cause overflow
            assertEquals(Integer.MAX_VALUE - 1, calculator.add(Integer.MAX_VALUE, -1));
            assertEquals(Integer.MIN_VALUE + 1, calculator.add(Integer.MIN_VALUE, 1));
            
            // Test multiplication with 1 and -1
            assertEquals(Integer.MAX_VALUE, calculator.multiply(Integer.MAX_VALUE, 1));
            assertEquals(Integer.MIN_VALUE, calculator.multiply(Integer.MIN_VALUE, 1));
            assertEquals(-Integer.MAX_VALUE, calculator.multiply(Integer.MAX_VALUE, -1));
        }

        @Test
        @DisplayName("Should work with very small double values")
        void testSmallDoubleValues() {
            double result = calculator.add(Double.MIN_VALUE, Double.MIN_VALUE);
            assertEquals(Double.MIN_VALUE * 2, result, 0.0);
            
            result = calculator.multiply(Double.MIN_VALUE, 2.0);
            assertEquals(Double.MIN_VALUE * 2, result, 0.0);
        }

        @Test
        @DisplayName("Should work with large double values")
        void testLargeDoubleValues() {
            double large1 = 1.7976931348623157E308; // Close to Double.MAX_VALUE
            double large2 = 1.0E-10;
            
            assertDoesNotThrow(() -> calculator.add(large1, large2));
            assertDoesNotThrow(() -> calculator.multiply(large2, 1000.0));
        }
    }
}
