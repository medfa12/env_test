Here are the updated coverage tables and JUnit tests reflecting the specific selection where GACC utilizes different minor clause values for clause **a**.

### 1. Truth Table
**Predicate:** $P = a \land (b \lor c)$
*   $a$: `!isLetter`
*   $b$: `last == 'r'`
*   $c$: `last == 't'`

| Row | a | b | c | P |
| :---: | :---: | :---: | :---: | :---: |
| 1 | T | T | T | **T** |
| 2 | T | T | F | **T** |
| 3 | T | F | T | **T** |
| 4 | T | F | F | **F** |
| 5 | F | T | T | **F** |
| 6 | F | T | F | **F** |
| 7 | F | F | T | **F** |
| 8 | F | F | F | **F** |

---

### 2. RICC Test Pairs (Restricted)
*Minor clauses must remain constant.*

| Major Clause | Pair (T, F) | Test Data Description |
| :--- | :--- | :--- |
| **a** | **(2, 6)** | `(T,T,F)` vs `(F,T,F)` <br> Input: `"r "` vs `"rr"` |
| **b** | **(2, 4)** | `(T,T,F)` vs `(T,F,F)` <br> Input: `"r "` vs `"x "` |
| **c** | **(3, 4)** | `(T,F,T)` vs `(T,F,F)` <br> Input: `"t "` vs `"x "` |

---

### 3. GACC Test Pairs (General)
*Minor clauses may change, provided the major clause determines the result.*

| Major Clause | Pair (T, F) | Test Data Description |
| :--- | :--- | :--- |
| **a** | **(2, 7)** | `(T,T,F)` vs `(F,F,T)` <br> *Minors changed from (T,F) to (F,T)* <br> Input: `"r "` vs `"tt"` |
| **b** | **(2, 4)** | Same as RICC |
| **c** | **(3, 4)** | Same as RICC |

---
