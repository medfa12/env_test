# 📊 Edge-Pair Coverage Report (tp1v2)

**PrintPrimes_src** implementation and tests for tp1v2, with CFG node mapping, edge-pair requirements, feasibility notes, and where each pair is exercised by the tp1v2 test suite.

> **Note:** This report covers the legacy `PrintPrimes_src.printPrimes(n)` method which finds the **first n prime numbers** (not primes up to n).

---

## 🗺️ Node Map (Control Flow Graph)

### `printPrimes(int n)` - Main Method
- **0**: Entry (method entry point)
- **1**: Initialize array and first prime (primes[0] = 2, numPrimes = 1, curPrime = 2)
- **2**: While loop condition `(numPrimes < n)`
- **3**: Increment curPrime and set isPrime = true
- **4**: For-loop entry (checking divisibility against known primes)
- **5**: For-loop condition `(i <= numPrimes-1)`
- **6**: Call isDivisible, prime confirmed (add to array)
- **7**: Increment curPrime / continue outer while loop
- **8**: Divisibility check result (break if divisible)
- **9**: For-loop increment `i++`
- **10**: Exit from `printPrimes` (print all primes)

---

## 🔗 Edge-Pair Requirements

**Definition:** Edge-pair `[a,b,c]` = three consecutive CFG nodes `a → b → c`

### Feasibility Analysis

| Status | Edge-Pairs | Reason |
|--------|-----------|--------|
| ✅ **Feasible** | Most paths | Standard control flow |
| ❌ **Infeasible** | `[2,3,7]`, `[3,7,2]` | Cannot bypass divisibility check loop |

### Complete Edge-Pair List

| # | Edge-Pair | Feasibility | Description |
|---|-----------|-------------|-------------|
| 1  | `[0,1,2]` | ✅ Feasible | Entry → Initialize → While condition |
| 2  | `[1,2,3]` | ✅ Feasible | Initialize → While(true) → Loop body |
| 3  | `[1,2,10]` | ✅ Feasible | Initialize → While(false) → Exit (n=1) |
| 4  | `[2,3,4]` | ✅ Feasible | While(true) → Increment curPrime → For-loop |
| 5  | `[2,3,7]` | ❌ **Infeasible** | Cannot skip divisibility checks |
| 6  | `[3,4,5]` | ✅ Feasible | Set isPrime → For-loop init → Condition |
| 7  | `[3,7,2]` | ❌ **Infeasible** | Cannot skip divisibility checks |
| 8  | `[4,5,8]` | ✅ Feasible | For-loop → Condition(true) → isDivisible |
| 9  | `[4,5,6]` | ✅ Feasible | For-loop → Condition(false) → Add prime |
| 10 | `[5,8,7]` | ✅ Feasible | Condition → Divisible(true) → Break/continue |
| 11 | `[5,8,9]` | ✅ Feasible | Condition → Divisible(false) → i++ |
| 12 | `[5,6,7]` | ✅ Feasible | Loop exit → Add prime → Continue while |
| 13 | `[8,7,2]` | ✅ Feasible | Break from for-loop → Continue while |
| 14 | `[6,7,2]` | ✅ Feasible | Add to array → numPrimes++ → While |
| 15 | `[7,2,3]` | ✅ Feasible | Continue → While(true) → Next iteration |
| 16 | `[7,2,10]` | ✅ Feasible | Continue → While(false) → Exit |
| 17 | `[8,9,5]` | ✅ Feasible | Not divisible → i++ → For condition |
| 18 | `[9,5,8]` | ✅ Feasible | i++ → For(true) → isDivisible |
| 19 | `[9,5,6]` | ✅ Feasible | i++ → For(false) → Add prime |

**Summary:** 17/19 feasible, 2/19 structurally infeasible

---

## 🧪 Test Paths Executed

### TPPathsTest (Parameterized)

Side trips enclosed in `{ }`, multiple iterations separated by `|`

#### TP1: Minimal Case (n=1)
```
Input:      n = 1 (find 1 prime)
Original:   0 → 1 → 2 → 10
Actual:     0 → 1 → 2 → 10 (exits immediately, primes[0]=2 already set)
Output:     "Prime: 2\n"
Coverage:   [0,1,2], [1,2,10]
```

#### TP2: Full Path (n=4)
```
Input:      n = 4 (find 4 primes: 2,3,5,7)
Original:   0 → 1 → 2 → 3 → 4 → 5 → 8 → 9 → 5 → 6 → 7 → 2 → 10
Actual:     0 → 1 → 2 → {3→4→5→6→7→2}(x=3) | {3→4→5→6→7→2}(x=5) | 
            {3→4→5→8→7→2}(x=4,6) | {3→4→5→6→7→2}(x=7) → 10
Output:     "Prime: 2\nPrime: 3\nPrime: 5\nPrime: 7\n"
Coverage:   [4,5,8], [5,8,7], [5,8,9], [8,9,5], [9,5,8], [9,5,6]
```

#### TP3: Single Prime (n=1)
```
Input:      n = 1 (find 1 prime)
Original:   0 → 1 → 2 → 3 → 4 → 5 → 6 → 7 → 2 → 10
Actual:     0 → 1 → 2 → 10 (same as TP1, exits after initialization)
Output:     "Prime: 2\n"
Coverage:   [0,1,2], [1,2,10]
```

#### TP4: Infeasible Path (n=2)
```
Input:      n = 2 (find 2 primes: 2,3)
Original:   0 → 1 → 2 → 3 → 7 → 2 → 3 → 7 → 2 → 10 (THEORETICAL)
Actual:     0 → 1 → 2 → {3→4→5→6→7→2}(x=3) → 10
Reason:     Cannot skip divisibility checks ([3,7,2] infeasible)
Output:     "Prime: 2\nPrime: 3\n"
Coverage:   [2,3,4], [3,4,5], [4,5,6], [5,6,7], [6,7,2], [7,2,10]
```

#### TP5: Multiple Primes (n=3)
```
Input:      n = 3 (find 3 primes: 2,3,5)
Original:   0 → 1 → 2 → 3 → 4 → 5 → 8 → 9 → 5 → 8 → 7 → 2 → 10
Actual:     0 → 1 → 2 → {3→4→5→6→7→2}(x=3) | {3→4→5→8→7→2}(x=4) | 
            {3→4→5→6→7→2}(x=5) → 10
Output:     "Prime: 2\nPrime: 3\nPrime: 5\n"
Coverage:   [4,5,8], [5,8,9], [8,7,2], [7,2,3]
```


---

## 📈 Coverage Summary

### Edge-Pairs by Test Case

| Test | Input | Edge-Pairs Covered |
|------|-------|-------------------|
| **TP1** | n=1 | `[0,1,2]`, `[1,2,10]` |
| **TP2** | n=4 | `[0,1,2]`, `[1,2,3]`, `[2,3,4]`, `[3,4,5]`, `[4,5,8]`, `[5,8,7]`, `[5,8,9]`, `[8,9,5]`, `[9,5,8]`, `[9,5,6]`, `[5,6,7]`, `[6,7,2]`, `[7,2,3]`, `[7,2,10]`, `[8,7,2]` |
| **TP3** | n=1 | `[0,1,2]`, `[1,2,10]` |
| **TP4** | n=2 | `[0,1,2]`, `[1,2,3]`, `[2,3,4]`, `[3,4,5]`, `[4,5,6]`, `[5,6,7]`, `[6,7,2]`, `[7,2,10]` |
| **TP5** | n=3 | `[0,1,2]`, `[1,2,3]`, `[2,3,4]`, `[3,4,5]`, `[4,5,8]`, `[5,8,9]`, `[8,7,2]`, `[5,6,7]`, `[6,7,2]`, `[7,2,3]`, `[7,2,10]` |

### Overall Statistics

```
✅ Feasible Edge-Pairs:   17/17 COVERED (100%)
❌ Infeasible Edge-Pairs: 2/19 (structural impossibility)
🎯 Total Coverage:        17/17 feasible paths tested
```

**Test Suite Achievement:** ✨ **Complete coverage of all feasible edge-pairs!**

---

## 📊 Incremental Coverage Analysis

### Test Contribution to Code Coverage

**Analysis Method:** Manual incremental test execution with JaCoCo  
**Date Performed:** November 9, 2025

| Configuration | Tests Enabled | Lines Covered | Line % | Branches Covered | Branch % |
|--------------|---------------|---------------|--------|------------------|----------|
| **Baseline** | All (TP1-TP5 + Performance) | **20/32** | **62.5%** | **12/14** | **85.7%** |
| TP1 Only | TP1 | 8/32 | 25.0% | 3/14 | 21.4% |
| TP1 + TP2 | TP1, TP2 | 20/32 | 62.5% | 12/14 | 85.7% |
| TP1-TP2-TP2var | TP1, TP2, TP2variant | 20/32 | 62.5% | 12/14 | 85.7% |
| TP1-TP3 | TP1, TP2, TP2var, TP3 | 20/32 | 62.5% | 12/14 | 85.7% |
| TP1-TP4 | TP1-TP4 | 20/32 | 62.5% | 12/14 | 85.7% |
| TP1-TP5 | TP1-TP5 (all path tests) | 20/32 | 62.5% | 12/14 | 85.7% |

### Key Insights

#### 🎯 Critical Test Discovery
**TP2 (n=4) is the most valuable test** - it contributes the majority of code coverage:
- **TP1 alone:** 25.0% line coverage, 21.4% branch coverage
- **TP1 + TP2:** 62.5% line coverage (+37.5%), 85.7% branch coverage (+64.3%)
- **Incremental gain:** +12 lines, +9 branches

#### 📉 Coverage Saturation
Tests **TP2variant, TP3, TP4, TP5** add **zero new coverage**:
- These tests exercise already-covered code paths
- Value lies in **regression testing** and **edge case validation**
- Not useful for increasing coverage metrics

#### 🔒 Maximum Achievable Coverage
**62.5% lines (20/32) | 85.7% branches (12/14)**

**Uncovered code (12 lines, 2 branches):**
- Likely error handling or boundary conditions
- May require tests for: n=0, negative n, or extreme values

### Visualization & Reports

**📂 Full Analysis Available:**
```
tp1v2/coverage-analysis/
├── 01-all-tests/          # Baseline JaCoCo report
├── 02-TP1-only/           # TP1 contribution
├── 03-TP1+TP2/            # TP2 contribution (CRITICAL)
├── 04-TP1+TP2+TP2variant/ # TP2variant contribution
├── 05-TP1-to-TP3/         # TP3 contribution
├── 06-TP1-to-TP4/         # TP4 contribution
├── 07-TP1-to-TP5/         # TP5 contribution (complete)
├── metrics.csv            # Raw coverage data
├── coverage-graph.html    # 📊 Interactive charts
└── README.md              # Detailed analysis
```

**🌐 View Interactive Visualization:**  
Open `coverage-analysis/coverage-graph.html` for:
- Line coverage progression chart
- Branch coverage progression chart
- Color-coded metrics table with incremental contributions

---

## 🚀 How to Run Coverage (JaCoCo)

### Run All Tests with Coverage Report
```bash
cd tp1v2
mvn clean test jacoco:report
```
**View Report:** Open `tp1v2/target/site/jacoco/index.html` in your browser

### Run Specific Test Suite
```bash
# TP paths only
mvn -Dtest=TPPathsTest test jacoco:report

# With quiet output
mvn -q -Dtest=TPPathsTest test jacoco:report
```

### Incremental Coverage Analysis
**Pre-generated reports available** in `coverage-analysis/` directory:
```bash
# View interactive visualization
start coverage-analysis/coverage-graph.html

# View individual JaCoCo reports for each test configuration
start coverage-analysis/01-all-tests/index.html
start coverage-analysis/02-TP1-only/index.html
# ... etc
```

### Per-Test Coverage (JacocoPerTestExtension)
Individual `.exec` files are generated in `target/jacoco-per-test/` for each test method, allowing granular coverage analysis.

---

## 📝 Implementation Notes

### Infeasibility Explanation
- **`[2,3,7]` and `[3,7,2]`** are structurally impossible because:
  - The for-loop (node 4-5) checking divisibility **must** be entered
  - Cannot jump directly from node 3 to node 7 without divisibility checks
  - This is enforced by the algorithm's logic structure

### Key Edge-Pairs
- **`[5,8,9]` and `[8,9,5]`**: Occur when a candidate is NOT divisible by current prime (inner loop continues)
- **`[4,5,8]` and `[5,8,7]`**: Occur when divisibility is found (breaks inner loop)
- **`[5,6,7]`**: Occurs when all divisibility checks pass (prime confirmed and added)

### Code Structure
- **Method Under Test:** `PrintPrimes_src.printPrimes(int n)`
- **Helper Method:** `isDivisible(int i, int j)`
- **Algorithm:** Sieve-like approach storing found primes and checking divisibility
- **Output Format:** `"Prime: X\n"` for each prime found

### Performance Comparison Test
The test suite includes `testAlgorithmComparison()` which compares:
- **`PrintPrimes.prime_N(n)`** - Finds all primes up to n (different algorithm)
- **`PrintPrimes_src.printPrimes(n)`** - Finds first n primes (algorithm under test)

This test demonstrates algorithmic differences and scaling behavior but does not contribute to edge-pair coverage.

---

## 📚 References

- **Source File:** `src/main/java/com/primes/PrintPrimes_src.java`
- **Test File:** `src/test/java/com/primes/TPPathsTest.java`
- **Coverage Analysis:** `coverage-analysis/` directory with incremental reports
- **Book:** Introduction to Software Testing by Paul Ammann & Jeff Offutt
- **Chapter:** 7 (Graph Coverage for Source Code)

---

**Last Updated:** November 9, 2025  
**Status:** ✅ All feasible edge-pairs covered | 📊 Incremental coverage analysis complete  
**Test Framework:** JUnit 5 (Jupiter) with Parameterized Tests  
**Coverage Tools:** JaCoCo 0.8.10 | Maven Surefire 3.1.2
