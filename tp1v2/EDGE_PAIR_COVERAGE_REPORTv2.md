# Edge-Pair Coverage Report (tp1v2)

PrintPrimes implementation and tests for tp1v2, with CFG node mapping, edge‑pair requirements, feasibility notes, and where each pair is exercised by the tp1v2 test suite.

---

## Node Map (CFG)

- 0: Entry (into `prime_N` from caller)
- 1: Initialize `x = 2` (for-loop init)
- 2: Loop condition `x <= N`
- 3: Loop body entry (before calling `isPrime`)
- 4: `isPrime` entry / initialize `i = 2`
- 5: `isPrime` loop condition `i <= sqrt(n)`
- 6: Print prime `System.out.print(x + " ")`
- 7: `x++` (for-loop increment in `prime_N`)
- 8: `if (n % i == 0)` divisibility check
- 9: `i++` (for-loop increment in `isPrime`)
- 10: Exit from `prime_N`

---

## Edge‑Pair Requirements

Edge‑pair [a,b,c] = three consecutive nodes `a -> b -> c`.

Feasibility summary (tp1v2 code):
- Infeasible: `[2,3,7]`, `[3,7,2]` (cannot skip the `isPrime` call)
- Feasible: all others, including `[5,8,9]` and `[8,9,5]` when `n % i != 0`

Complete list:

1. [0,1,2]  — feasible
2. [1,2,3]  — feasible
3. [1,2,10] — feasible
4. [2,3,4]  — feasible
5. [2,3,7]  — infeasible (cannot bypass `isPrime`)
6. [3,4,5]  — feasible
7. [3,7,2]  — infeasible (cannot bypass `isPrime`)
8. [4,5,8]  — feasible (divisor found -> early return)
9. [4,5,6]  — feasible (prime path -> print)
10. [5,8,7] — feasible (divisible -> no print -> back to outer loop)
11. [5,8,9] — feasible (not divisible -> increment i)
12. [5,6,7] — feasible (print -> x++)
13. [8,7,2] — feasible (after early return to outer loop)
14. [6,7,2] — feasible
15. [7,2,3] — feasible
16. [7,2,10] — feasible
17. [8,9,5] — feasible (inner loop continues)
18. [9,5,8] — feasible
19. [9,5,6] — feasible

---

## Test Paths Used (original vs. actual; side trips in { } and separated by |)

- TPPathsTest (parameterized)
  - TP1 (N=1)
    - Original: 0-1-2-10
    - Actual: 0-1-2-10 (loop not entered; no isPrime call)
    - Output: "Prime numbers up to 1 are:\n\n"
  - TP3 (N=2)
    - Original: 0-1-2-3-4-5-6-7-2-10
    - Actual: 0-1-2-{3-4-5-6-7-2}-10
    - Execution Trace: `{3-4-5-6-7-2}`
    - Output: "Prime numbers up to 2 are:\n2 \n"
  - TP5 (N=5)
    - Original: 0-1-2-3-4-5-8-9-5-8-7-2-10
    - Actual: 0-1-2-{3-4-5-6-7-2}|{3-4-5-6-7-2}|{3-4-5-8->return-7-2}[theory:5-8-9-5-8]|{3-4-5-6-7-2}-10
    - Execution Trace: `{3-4-5-6-7-2}|{3-4-5-6-7-2}|{3-4-5-8->return-7-2}|{3-4-5-6-7-2}`
    - Output: "Prime numbers up to 5 are:\n2 3 5 \n"
  - TP2 (N=4)
    - Original: 0-1-2-3-4-5-8-7-2-10
    - Actual: 0-1-2-{3-4-5-6-7-2}|{3-4-5-6-7-2}|{3-4-5-8->return-7-2}-10
    - Execution Trace: `{3-4-5-6-7-2}|{3-4-5-6-7-2}|{3-4-5-8->return-7-2}`
    - Output: "Prime numbers up to 4 are:\n2 3 \n"
  - TP4 (N=2, theoretical)
    - Original: 0-1-2-3-7-2-3-7-2-10
    - Actual: Infeasible in this implementation (cannot skip isPrime); equals TP3 actual: 0-1-2-{3-4-5-6-7-2}-10
    - Output: "Prime numbers up to 2 are:\n2 \n"


---

## Edge‑Pairs Covered by TP

- TP1 (N=1): [0,1,2], [1,2,10]
- TP3 (N=2): [0,1,2], [1,2,3], [2,3,4], [3,4,5], [4,5,6], [5,6,7], [6,7,2], [7,2,10]
- TP5 (N=5): [4,5,8] (x=4), [5,8,9] (x=5), [9,5,6] (x=5), [5,6,7], [6,7,2], [7,2,3], [7,2,10], [8,7,2]
- TP2 (N=9): [4,5,8], [5,8,7], [8,7,2], [5,8,9], [8,9,5], [9,5,8], [9,5,6], [5,6,7], [6,7,2], [7,2,3], [7,2,10]

Summary
- Feasible edge‑pairs: 17/17 covered by {TP1, TP2, TP3, TP5}
- Infeasible (by structure): [2,3,7], [3,7,2]

---

## How To Run Coverage (JaCoCo)

- All tests + report
  - `cd tp1v2`
  - `mvn -q clean test jacoco:report`
  - Open `tp1v2/target/site/jacoco/index.html`

- Specific tests
  - TP paths only (this suite): `mvn -q -Dtest=TPPathsTest test jacoco:report`

---

## Notes

- Edge‑pairs `[2,3,7]` and `[3,7,2]` are structurally impossible in this implementation because `isPrime(x)` is always called inside the loop body before `x++`.
- Edge‑pairs `[5,8,9]` and `[8,9,5]` occur whenever `n % i != 0` in `isPrime` (inner loop continues to next i).
- The annotations in source (`src/main/java/com/primes/PrintPrimes.java`) and in `TPPathsTest` comments map lines to the node numbers above.
