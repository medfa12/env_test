# Incremental Coverage Analysis Results

## Analysis Summary

**Date**: 2025-11-09  
**Project**: tp1v2 - PrintPrimes_src  
**Method**: Manual incremental test execution  

## Coverage Progression

| Configuration | Lines Covered | Line % | Branches Covered | Branch % |
|--------------|--------------|--------|------------------|----------|
| **01-all-tests (baseline)** | **20/32** | **62.5%** | **12/14** | **85.7%** |
| 02-TP1-only | 8/32 | 25.0% | 3/14 | 21.4% |
| 03-TP1+TP2 | 20/32 | 62.5% | 12/14 | 85.7% |
| 04-TP1+TP2+TP2variant | 20/32 | 62.5% | 12/14 | 85.7% |
| 05-TP1-to-TP3 | 20/32 | 62.5% | 12/14 | 85.7% |
| 06-TP1-to-TP4 | 20/32 | 62.5% | 12/14 | 85.7% |
| 07-TP1-to-TP5 | 20/32 | 62.5% | 12/14 | 85.7% |

## Key Findings

### Test Contribution Analysis

1. **TP1 (n=1)**: 
   - Covers: 8/32 lines (25%), 3/14 branches (21.4%)
   - Minimal path, basic initialization

2. **TP2 (n=4)**: 
   - Adds: +12 lines (+37.5%), +9 branches (+64.3%)
   - **CRITICAL**: This single test contributes the majority of coverage
   - Full coverage achieved with TP1+TP2 alone

3. **TP2 variant, TP3, TP4, TP5**:
   - Add: 0 new lines, 0 new branches
   - These tests exercise already-covered paths
   - Value: Test different edge cases and input combinations
   - No new code coverage contribution

### Coverage Saturation

**Maximum achievable coverage**: 62.5% lines, 85.7% branches

**Uncovered code** (12 lines, 2 branches):
- Likely error handling paths
- Edge cases not exercised by current test suite
- May require additional test scenarios (n=0, negative n, very large n)

## Visualization

Open **coverage-graph.html** in a browser to see:
- Line coverage progression chart
- Branch coverage progression chart
- Interactive metrics table with color coding

## JaCoCo Reports

Individual HTML reports saved in folders:
```
coverage-analysis/
├── 01-all-tests/          # Baseline (all tests)
├── 02-TP1-only/           # Only TP1 test
├── 03-TP1+TP2/            # TP1 + TP2
├── 04-TP1+TP2+TP2variant/ # TP1 + TP2 + TP2variant
├── 05-TP1-to-TP3/         # TP1 through TP3
├── 06-TP1-to-TP4/         # TP1 through TP4
├── 07-TP1-to-TP5/         # TP1 through TP5 (all tests)
├── metrics.csv            # Raw metrics data
├── coverage-graph.html    # Interactive visualization
└── README.md              # This file
```

## Methodology

1. Started with baseline (all tests enabled)
2. Disabled all tests except TP1
3. Incrementally enabled tests: TP1 → TP1+TP2 → TP1+TP2+TP2variant → etc.
4. After each configuration:
   - Ran `mvn clean test`
   - Copied `target/site/jacoco/` to labeled folder
5. Extracted metrics from `jacoco.csv` files
6. Generated visualization with Chart.js

## Conclusion

**TP2 is the most valuable test** - it contributes 37.5% line coverage and 64.3% branch coverage beyond TP1. The remaining tests (TP2variant, TP3, TP4, TP5) add redundancy but no new coverage, making them valuable for regression testing but not for increasing code coverage metrics.

---
*Analysis performed manually to demonstrate test contribution to coverage metrics*
