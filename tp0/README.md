# TP0 Calculator Project

A simple Java calculator implementation with comprehensive JUnit testing for addition and multiplication operations.

## Project Structure

```
tp0/
├── src/
│   ├── main/java/com/calculator/
│   │   └── Calculator.java          # Main calculator implementation
│   └── test/java/com/calculator/
│       └── CalculatorTest.java      # Comprehensive test suite
├── pom.xml                          # Maven build configuration
└── README.md                        # This file
```

## Features

### Calculator Operations
- **Addition**: Supports both integer and double addition with overflow/underflow detection
- **Multiplication**: Supports both integer and double multiplication with overflow detection
- **Exception Handling**: Proper validation for edge cases like NaN, infinity, and overflow conditions

### Test Coverage
The test suite includes:
- **Normal Behavior Tests**: Standard operations with positive, negative, and zero values
- **Exception Handling Tests**: Validation of error conditions and edge cases
- **Parameterized Tests**: Multiple test cases with different input combinations
- **Edge Case Tests**: Boundary value testing and integration scenarios

## Building and Running

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher

### Build the Project
```bash
mvn clean compile
```

### Run Tests
```bash
mvn test
```

### Run All Tests with Detailed Output
```bash
mvn test -Dtest=CalculatorTest
```

## Test Categories

### Addition Tests
- **Integer Addition**: Normal behavior with positive, negative, zero, and mixed values
- **Double Addition**: Floating-point arithmetic with precision handling
- **Addition Exceptions**: Overflow, underflow, NaN, and infinity handling

### Multiplication Tests
- **Integer Multiplication**: Standard multiplication operations
- **Double Multiplication**: Floating-point multiplication including infinity cases
- **Multiplication Exceptions**: Overflow detection and NaN handling

### Edge Cases
- Boundary value testing with `Integer.MAX_VALUE` and `Integer.MIN_VALUE`
- Very small and large double value handling
- Integration tests combining multiple operations

## Exception Handling

The calculator properly handles:
- **ArithmeticException**: For integer overflow/underflow conditions
- **IllegalArgumentException**: For invalid inputs like NaN or inappropriate infinity values

## Usage Example

```java
Calculator calc = new Calculator();

// Basic operations
int sum = calc.add(5, 3);           // Returns 8
int product = calc.multiply(4, 7);   // Returns 28

// Double operations
double doubleSum = calc.add(3.14, 2.86);     // Returns 6.0
double doubleProduct = calc.multiply(2.5, 4.0); // Returns 10.0

// Exception handling
try {
    calc.add(Integer.MAX_VALUE, 1); // Throws ArithmeticException
} catch (ArithmeticException e) {
    System.out.println("Overflow detected: " + e.getMessage());
}
```
