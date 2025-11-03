package com.primes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Duration;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Paths (TP1-TP5) exercising prime_N paths for JaCoCo
 *
 * Markdown summary of paths (Original vs. Actual with sidetrips):
 *
 * | Case | N | Original Path | Actual Path (with sidetrips if any) |
 * |------|---|---------------|-------------------------------------|
 * | TP1  | 1 | 0-1-2-10 | 0-1-2-10 |
 * | TP2  | 9 | 0-1-2-3-4-5-8-9-5-6-7-2-10 | 0-1-2, then for x=2..9: 3-[isPrime]-(prime: 4-5(false)->6)-(composite: 4-5-8(true)), 7-2; finally 10 |
 * | TP3  | 2 | 0-1-2-3-4-5-6-7-2-10 | 0-1-2-3-4-5(false)-6-7-2(false)-10 |
 * | TP4  | 2 | 0-1-2-3-7-2-3-7-2-10 | Infeasible; actual equals TP3 minimal prime path |
 * | TP5  | 5 | 0-1-2-3-4-5-8-9-5-8-7-2-10 | 0-1-2, x=2:3-4-5(false)-6-7-2, x=3:3-4-5(false)-6-7-2, x=4:3-4-5-8(true)-7-2, x=5:3-4-5(false)-6-7-2, 10 |
 */
@ExtendWith(JacocoPerTestExtension.class)
class TPPathsTest {

    // Helper to capture output of prime_N and normalize newlines
    private String capturePrimeN(int N) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(baos));
        try {
            PrintPrimes.prime_N(N);
        } finally {
            System.setOut(originalOut);
        }
        return baos.toString().replace("\r\n", "\n");
    }

    static Stream<Arguments> cases() {
        return Stream.of(
            // TP1
            Arguments.of(
                "TP1: 0-1-2-10 (N=1)",
                1,
                "Prime numbers up to 1 are:\n\n",
                "0-1-2-10",
                "0-1-2-10"
            ),
            // TP2
            Arguments.of(
                "TP2: 0-1-2-3-4-5-8-9-5-6-7-2-10 (N=9)",
                9,
                "Prime numbers up to 9 are:\n2 3 5 7 \n",
                "0-1-2-3-4-5-8-9-5-6-7-2-10",
                "0-1-2, then per x in [2..9]: 3 -> [isPrime] -> (prime: 4-5(false)->6) | (composite: 4-5-8(true)), 7-2; finally 10"
            ),
            // TP2 variant with N=4
            Arguments.of(
                "TP2 (variant N=4): 0-1-2-3-4-5-8-7-2-10",
                4,
                "Prime numbers up to 4 are:\n2 3 \n",
                "0-1-2-3-4-5-8-7-2-10",
                "0-1-2, x=2:3-4-5(false)-6-7-2, x=3:3-4-5(false)-6-7-2, x=4:3-4-5(true)-8(true)->return-7-2, 10"
            ),
            // TP3
            Arguments.of(
                "TP3: 0-1-2-3-4-5-6-7-2-10 (N=2)",
                2,
                "Prime numbers up to 2 are:\n2 \n",
                "0-1-2-3-4-5-6-7-2-10",
                "0-1-2-3-4-5(false)-6-7-2(false)-10"
            ),
            // TP4 (theoretical infeasible path)
            Arguments.of(
                "TP4 (infeasible): 0-1-2-3-7-2-3-7-2-10 (N=2)",
                2,
                "Prime numbers up to 2 are:\n2 \n",
                "0-1-2-3-7-2-3-7-2-10",
                "Infeasible in code; actual equals TP3 minimal prime path"
            ),
            // TP5 (theoretical tail does not occur for N=5)
            Arguments.of(
                "TP5 (tail to 8 not reached for N=5): 0-1-2-3-4-5-8-9-5-8-7-2-10",
                5,
                "Prime numbers up to 5 are:\n2 3 5 \n",
                "0-1-2-3-4-5-8-9-5-8-7-2-10",
                "0-1-2, x=2:3-4-5(false)-6-7-2, x=3:3-4-5(false)-6-7-2, x=4:3-4-5-8(true)-7-2, x=5:3-4-5(false)-6-7-2, 10"
            )
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("cases")
    @DisplayName("TP paths (parameterized): asserts visible output; docs include original vs actual paths")
    void testTP_paths_parameterized(String label, int N, String expectedOutput,
                                    String originalPath, String actualPath) {
        // Original path (declared):
        //   " + originalPath
        // Actual path (with sidetrips):
        //   " + actualPath
        String out = capturePrimeN(N);
        assertEquals(expectedOutput, out);
    }

    @Test
    void primeNCompletesWithinTimeout() {
        long[] elapsed = new long[1];
        assertTimeout(Duration.ofMillis(100), () -> {
            long start = System.nanoTime();
            capturePrimeN(1000);
            elapsed[0] = System.nanoTime() - start;
        });
        System.out.println("assertTimeout prime_N(1000) completed in " + elapsed[0] / 1_000_000.0 + " ms");
    }

    @Test
    void primeNCompletesWithinPreemptiveTimeout() {
        long[] elapsed = new long[1];
        assertTimeoutPreemptively(Duration.ofMillis(100), () -> {
            long start = System.nanoTime();
            capturePrimeN(1000);
            elapsed[0] = System.nanoTime() - start;
        });
        System.out.println("assertTimeoutPreemptively prime_N(1000) completed in " + elapsed[0] / 1_000_000.0 + " ms");
    }
}
