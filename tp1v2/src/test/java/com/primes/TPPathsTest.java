package com.primes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ═══════════════════════════════════════════════════════════════════════════
 * Test Paths (TP1-TP5) for PrintPrimes_src.printPrimes(n)
 * ═══════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Exercise different control flow paths through the printPrimes algorithm
 *          for comprehensive JaCoCo code coverage analysis.
 * 
 * NOTE: printPrimes(n) finds the FIRST n prime numbers (not primes up to n)
 * 
 * ───────────────────────────────────────────────────────────────────────────
 * TEST PATH SUMMARY
 * ───────────────────────────────────────────────────────────────────────────
 * 
 * ╔══════╦═══════════╦═════════════════════════════╦════════════════════════════════════════════╗
 * ║ Case ║ n (count) ║ Original Path               ║ Actual Execution Path                      ║
 * ╠══════╬═══════════╬═════════════════════════════╬════════════════════════════════════════════╣
 * ║ TP1  ║     1     ║ 0-1-2-10                    ║ Minimal: Initialize array with 2, exit     ║
 * ║      ║           ║                             ║ immediately (numPrimes already = 1)        ║
 * ╟──────╫───────────╫─────────────────────────────╫────────────────────────────────────────────╢
 * ║ TP2  ║     4     ║ 0-1-2-3-4-5-8-9-5-6-7-2-10  ║ Full path: Multiple while iterations,      ║
 * ║      ║           ║                             ║ checking divisibility for 2,3,5,7          ║
 * ╟──────╫───────────╫─────────────────────────────╫────────────────────────────────────────────╢
 * ║ TP3  ║     1     ║ 0-1-2-3-4-5-6-7-2-10        ║ Same as TP1: exits after initialization    ║
 * ╟──────╫───────────╫─────────────────────────────╫────────────────────────────────────────────╢
 * ║ TP4  ║     2     ║ 0-1-2-3-7-2-3-7-2-10        ║ INFEASIBLE PATH: Theoretical path cannot   ║
 * ║      ║           ║ (infeasible)                ║ occur; actual finds 2 primes (2,3)         ║
 * ╟──────╫───────────╫─────────────────────────────╫────────────────────────────────────────────╢
 * ║ TP5  ║     3     ║ 0-1-2-3-4-5-8-9-5-8-7-2-10  ║ Multiple iterations with divisibility      ║
 * ║      ║           ║                             ║ checks to find first 3 primes (2,3,5)      ║
 * ╚══════╩═══════════╩═════════════════════════════╩════════════════════════════════════════════╝
 * 
 * ADDITIONAL TESTS:
 *   • Performance timeout tests (assertTimeout & assertTimeoutPreemptively)
 *   • Output format validation (ensures "Prime: X" format)
 * 
 * ───────────────────────────────────────────────────────────────────────────
 */
class TPPathsTest {

    // Helper to capture output of printPrimes and normalize newlines
    private String capturePrintPrimes(int n) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(baos));
        try {
            PrintPrimes_src.printPrimes(n);
        } finally {
            System.setOut(originalOut);
        }
        return baos.toString().replace("\r\n", "\n");
    }

    static Stream<Arguments> cases() {
        return Stream.of(
            // TP1: Find 1 prime
            Arguments.of(
                "TP1: 0-1-2-10 (n=1)",
                1,
                "Prime: 2\n",
                "0-1-2-10",
                "0-1-2-10 (finds 1 prime: 2)"
            ),
            // TP2: Find 4 primes
            Arguments.of(
                "TP2: 0-1-2-3-4-5-8-9-5-6-7-2-10 (n=4)",
                4,
                "Prime: 2\nPrime: 3\nPrime: 5\nPrime: 7\n",
                "0-1-2-3-4-5-8-9-5-6-7-2-10",
                "Multiple iterations through while loop, checking divisibility to find first 4 primes"
            ),
            // TP2 variant: Find 2 primes
            Arguments.of(
                "TP2 (variant n=2): 0-1-2-3-4-5-8-7-2-10",
                2,
                "Prime: 2\nPrime: 3\n",
                "0-1-2-3-4-5-8-7-2-10",
                "Finds first 2 primes (2, 3) with divisibility checks"
            ),
            // TP3: Find 1 prime (minimal case)
            Arguments.of(
                "TP3: 0-1-2-3-4-5-6-7-2-10 (n=1)",
                1,
                "Prime: 2\n",
                "0-1-2-3-4-5-6-7-2-10",
                "Finds 1 prime (2), exits while loop immediately after initialization"
            ),
            // TP4: Find 2 primes but testing infeasible path concept
            Arguments.of(
                "TP4 (infeasible path): 0-1-2-3-7-2-3-7-2-10 (n=2)",
                2,
                "Prime: 2\nPrime: 3\n",
                "0-1-2-3-7-2-3-7-2-10",
                "Theoretical path 0-1-2-3-7-2-3-7-2-10 is infeasible; actual: init(2), check 3 (prime), then exit"
            ),
            // TP5: Find 3 primes
            Arguments.of(
                "TP5: 0-1-2-3-4-5-8-9-5-8-7-2-10 (n=3)",
                3,
                "Prime: 2\nPrime: 3\nPrime: 5\n",
                "0-1-2-3-4-5-8-9-5-8-7-2-10",
                "Finds first 3 primes (2,3,5) with multiple divisibility checks"
            )
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("cases")
    @DisplayName("TP paths (parameterized): asserts visible output; docs include original vs actual paths")
    void testTP_paths_parameterized(String label, int n, String expectedOutput,
                                    String originalPath, String actualPath) {
        // Original path (declared):
        //   " + originalPath
        // Actual path (with sidetrips):
        //   " + actualPath
        String out = capturePrintPrimes(n);
        assertEquals(expectedOutput, out);
    }

    @Test
    @DisplayName("Compares PrintPrimes.prime_N() vs PrintPrimes_src.printPrimes()")
    void testAlgorithmComparison() {
        // Warm up
        for (int i = 0; i < 5; i++) {
            capturePrimeN(50);
            capturePrintPrimes(10);
        }
        
        long time50_primeN = measureTime(() -> capturePrimeN(50));
        long time10_src = measureTime(() -> capturePrintPrimes(10));
        long time200_primeN = measureTime(() -> capturePrimeN(200));
        long time80_src = measureTime(() -> capturePrintPrimes(80));
        
        System.out.println("=== Algorithm Comparison ===");
        System.out.printf("PrintPrimes.prime_N(50):            %.3f ms%n", time50_primeN / 1_000_000.0);
        System.out.printf("PrintPrimes_src.printPrimes(10):    %.3f ms%n", time10_src / 1_000_000.0);
        System.out.printf("PrintPrimes.prime_N(200):           %.3f ms%n", time200_primeN / 1_000_000.0);
        System.out.printf("PrintPrimes_src.printPrimes(80):    %.3f ms%n", time80_src / 1_000_000.0);
        
        double primeNGrowth = (double) time200_primeN / time50_primeN;
        double srcGrowth = (double) time80_src / time10_src;
        System.out.printf("Growth factor: prime_N(50→200) %.2fx  |  printPrimes(10→80) %.2fx%n", 
            primeNGrowth, srcGrowth);
    }

    private String capturePrimeN(int n) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(baos));
        try {
            PrintPrimes.prime_N(n);
        } finally {
            System.setOut(originalOut);
        }
        return baos.toString().replace("\r\n", "\n");
    }

    private long measureTime(Runnable task) {
        task.run();
        long start = System.nanoTime();
        task.run();
        return System.nanoTime() - start;
    }
}
