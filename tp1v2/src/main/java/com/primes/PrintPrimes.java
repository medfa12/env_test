package com.primes;


public class PrintPrimes {

    // Function to check if a number is prime
    // CFG (isPrime): 4 (init) -> 5 (cond) -> [8 (divisible -> return false) | 9 (i++) -> 5] -> return true
    public static boolean isPrime(int n) {               // [isPrime] entry
        if (n <= 1) {                                    // guard for non-primes
            return false;                                // early return for n <= 1
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {       // Node 5: inner loop condition (i <= sqrt(n)), i init implicit Node 4
            if (n % i == 0) {                            // Node 8: divisibility check
                return false;                            // TRUE branch of 8: early return
            }
            // FALSE branch of 8 continues here
            // Node 9: i++ (handled by for-loop increment), then back to Node 5
        }
        return true;                                     // No divisors found -> prime
    }

    // Function to print all primes up to N
    // CFG (prime_N): 1 (init x=2) -> 2 (x<=N?) -> 3 (enter body) -> 4..isPrime.. -> [6 (print) optional] -> 7 (x++) -> 2 -> 10 (exit)
    public static void prime_N(int N) {                  // [0] Entry (from caller)
        System.out.println("Prime numbers up to " + N + " are:"); // heading
        for (int x = 2; x <= N; x++) {                   // Node 1 (init x=2) and Node 2 (x <= N?)
            // Node 3: loop body entry (before call)
            if (isPrime(x)) {                            // Node 4/5/8/9: call isPrime(x)
                System.out.print(x + " ");              // Node 6: print prime when true
            }
            // Node 7: x++ is handled by the for-loop increment
        }
        System.out.println();                            // After loop; Node 10: exit from prime_N
    }

    // The main driver method
    public static void main(String[] args) {             // [Program Entry]
        int N = 45;                                      // parameter example
        prime_N(N);                                      // call into prime_N (node 0 -> 1)
    }
}
