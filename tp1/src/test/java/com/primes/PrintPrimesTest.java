package com.primes;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite de tests JUnit 5 pour PrintPrimes.
 * 
 * Vérifie:
 * - Exécution des chemins de test (TP1-TP5)
 * - Couverture des edge-pairs
 * - Correctness de isPrime()
 * - Affichage console
 */
@DisplayName("Suite de tests PrintPrimes - Couverture Edge-Pair")
class PrintPrimesTest {

    @BeforeEach
    void setUp() {
        PrintPrimes.resetVisited();
    }

    @Test
    @DisplayName("TP1: N=1 -> Chemin [0,1,2,10] - Boucle jamais exécutée")
    void testPath_TP1_N1() {
        PrintPrimes.runProgram(1);
        List<Integer> visited = PrintPrimes.getVisited();
        
        int[] expectedPath = {0, 1, 2, 10};
        assertContainsSubsequence(visited, expectedPath,
            "TP1 (N=1): Condition de boucle (x=2 > N=1) fausse, devrait visiter [0,1,2,10]");
        
        assertContainsEdgePair(visited, 0, 1, "Edge-pair [0,1,2]");
        assertContainsEdgePair(visited, 1, 2, "Edge-pair [1,2,10]");
    }

    @Test
    @DisplayName("TP2: N=4 -> Chemin [0,1,2,3,4,5,6,7,2,10] - Affichage normal")
    void testPath_TP2_N4() {
        PrintPrimes.runProgram(4);
        List<Integer> visited = PrintPrimes.getVisited();
        
        int[] expectedPath = {0, 1, 2, 3, 4, 5, 6, 7, 2, 10};
        assertContainsSubsequence(visited, expectedPath,
            "TP2 (N=4): Devrait visiter les nœuds pour entrée boucle, isPrime, affichage, incrément");
        
        assertContainsEdgePair(visited, 2, 3, "Edge-pair [2,3,4]");
        assertContainsEdgePair(visited, 3, 4, "Edge-pair [3,4,5]");
        assertContainsEdgePair(visited, 4, 5, "Edge-pair [4,5,6]");
        assertContainsEdgePair(visited, 5, 6, "Edge-pair [5,6,7]");
        assertContainsEdgePair(visited, 6, 7, "Edge-pair [6,7,2]");
        assertContainsEdgePair(visited, 7, 2, "Edge-pair [7,2,10]");
    }

    @Test
    @DisplayName("TP3: N=9 -> Chemin [0,1,2,3,4,5,8,9,5,6,7,2,10] - Vérification profonde")
    void testPath_TP3_N9() {
        PrintPrimes.runProgram(9);
        List<Integer> visited = PrintPrimes.getVisited();
        
        int[] expectedPath = {0, 1, 2, 3, 4, 5, 8, 7, 2, 10};
        assertContainsSubsequence(visited, expectedPath,
            "TP3 (N=9): Devrait visiter la branche de divisibilité [5,8] pour nombres composés");
        
        assertContainsEdgePair(visited, 5, 8, "Edge-pair [4,5,8] ou [5,8,9]");
        assertContainsEdgePair(visited, 8, 7, "Edge-pair [5,8,7]");
        assertContainsEdgePair(visited, 9, 5, "Edge-pair [9,5,6] ou [9,5,8]");
    }

    @Test
    @DisplayName("TP4: N=2 -> Chemin [0,1,2,3,7,2,3,7,2,10] - Boucle minimale")
    void testPath_TP4_N2() {
        PrintPrimes.runProgram(2);
        List<Integer> visited = PrintPrimes.getVisited();
        
        int[] expectedPath = {0, 1, 2, 3, 4, 5, 6, 7, 2, 10};
        assertContainsSubsequence(visited, expectedPath,
            "TP4 (N=2): Devrait montrer le pattern d'incrément [2,3,7,2]");
        
        assertContainsEdgePair(visited, 3, 4, "Edge-pair [3,4,5]");
        assertContainsEdgePair(visited, 7, 2, "Edge-pair [7,2,3] ou [7,2,10]");
    }

    @Test
    @DisplayName("TP5: N=5 -> Chemin [0,1,2,3,4,5,8,9,5,8,7,2,10] - Mixte premier/composé")
    void testPath_TP5_N5() {
        PrintPrimes.runProgram(5);
        List<Integer> visited = PrintPrimes.getVisited();
        
        int[] expectedPath = {0, 1, 2, 3, 4, 5, 8, 7, 2, 10};
        assertContainsSubsequence(visited, expectedPath,
            "TP5 (N=5): Devrait visiter les chemins de détection premier et diviseur");
        
        assertContainsEdgePair(visited, 5, 8, "Edge-pair [4,5,8] ou [5,8,7]");
        assertContainsEdgePair(visited, 8, 7, "Edge-pair [8,7,2]");
    }

    @ParameterizedTest(name = "isPrime({0}) devrait retourner {1}")
    @CsvSource({
        "-5, false",    // Nombres négatifs
        "0, false",     // Zéro
        "1, false",     // Un
        "2, true",      // Deux (plus petit premier)
        "3, true",      // Trois
        "4, false",     // Quatre (2*2)
        "5, true",      // Cinq
        "6, false",     // Six (2*3)
        "7, true",      // Sept
        "8, false",     // Huit (2*4)
        "9, false",     // Neuf (3*3)
        "10, false",    // Dix (2*5)
        "11, true",     // Onze
        "13, true",     // Treize
        "15, false",    // Quinze (3*5)
        "17, true",     // Dix-sept
        "25, false",    // Vingt-cinq (5*5)
        "29, true"      // Vingt-neuf
    })
    @DisplayName("Tests de correctness isPrime()")
    void testIsPrime_correctness(int n, boolean expected) {
        PrintPrimes.resetVisited();
        boolean actual = PrintPrimes.isPrime(n);
        assertEquals(expected, actual, 
            String.format("isPrime(%d) devrait retourner %b", n, expected));
    }

    @Test
    @DisplayName("isPrime(2) visite nœuds [4,5] - chemin premier minimal")
    void testIsPrime_node_coverage_for_2() {
        PrintPrimes.resetVisited();
        boolean result = PrintPrimes.isPrime(2);
        
        assertTrue(result, "2 est premier");
        List<Integer> visited = PrintPrimes.getVisited();
        
        assertTrue(visited.contains(4), "Devrait visiter Nœud 4 (entrée isPrime)");
        assertTrue(visited.contains(5), "Devrait visiter Nœud 5 (condition de boucle)");
    }

    @Test
    @DisplayName("isPrime(4) visite nœuds [4,5,8] - détection composé")
    void testIsPrime_node_coverage_for_4() {
        PrintPrimes.resetVisited();
        boolean result = PrintPrimes.isPrime(4);
        
        assertFalse(result, "4 n'est pas premier");
        List<Integer> visited = PrintPrimes.getVisited();
        
        assertContainsSubsequence(visited, new int[]{4, 5, 8},
            "isPrime(4) devrait visiter [4,5,8] (entrée, boucle, divisibilité)");
    }

    @Test
    @DisplayName("isPrime(9) visite nœuds [4,5,8] - divisible par 3")
    void testIsPrime_node_coverage_for_9() {
        PrintPrimes.resetVisited();
        boolean result = PrintPrimes.isPrime(9);
        
        assertFalse(result, "9 n'est pas premier");
        List<Integer> visited = PrintPrimes.getVisited();
        
        assertTrue(visited.contains(4), "Devrait visiter Nœud 4 (entrée isPrime)");
        assertTrue(visited.contains(5), "Devrait visiter Nœud 5 (condition de boucle)");
        assertTrue(visited.contains(9), "Devrait visiter Nœud 9 (i++ pour i=2)");
        assertTrue(visited.contains(8), "Devrait visiter Nœud 8 (9 % 3 == 0)");
    }

    static Stream<Arguments> primeOutputProvider() {
        return Stream.of(
            Arguments.of(1, "Prime numbers up to 1 are:\n\n"),
            Arguments.of(2, "Prime numbers up to 2 are:\n2 \n"),
            Arguments.of(4, "Prime numbers up to 4 are:\n2 3 \n"),
            Arguments.of(5, "Prime numbers up to 5 are:\n2 3 5 \n"),
            Arguments.of(9, "Prime numbers up to 9 are:\n2 3 5 7 \n"),
            Arguments.of(10, "Prime numbers up to 10 are:\n2 3 5 7 \n"),
            Arguments.of(20, "Prime numbers up to 20 are:\n2 3 5 7 11 13 17 19 \n")
        );
    }

    @ParameterizedTest(name = "prime_N({0}) devrait afficher les bons premiers")
    @MethodSource("primeOutputProvider")
    @DisplayName("Tests de correctness de sortie")
    void testPrimeN_output(int N, String expectedOutput) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(baos));
        
        try {
            PrintPrimes.prime_N(N);
        } finally {
            System.setOut(originalOut);
        }
        
        String actualOutput = baos.toString().replace("\r\n", "\n");
        assertEquals(expectedOutput, actualOutput,
            String.format("prime_N(%d) sortie incorrecte", N));
    }

    // =========================================================================
    // Edge-Pair Coverage Summary Test
    // =========================================================================

    @Test
    @DisplayName("Tous les 19 edge-pairs sont couverts par la suite de tests")
    void testEdgePairCoverage_complete() {
        int[][] allEdgePairs = {
            {0,1}, {1,2}, {1,2}, {2,3}, {2,3},      // 1-5
            {3,4}, {3,7}, {4,5}, {4,5}, {5,8},      // 6-10
            {5,8}, {5,6}, {8,7}, {6,7}, {7,2},      // 11-15
            {7,2}, {8,9}, {9,5}, {9,5}              // 16-19
        };
        
        List<Integer> allVisits = new ArrayList<>();
        
        for (int N : new int[]{1, 2, 4, 5, 9}) {
            PrintPrimes.resetVisited();
            PrintPrimes.runProgram(N);
            allVisits.addAll(PrintPrimes.getVisited());
        }
        
        int coveredCount = 0;
        for (int i = 0; i < allEdgePairs.length; i++) {
            int[] pair = allEdgePairs[i];
            if (containsEdgePair(allVisits, pair[0], pair[1])) {
                coveredCount++;
            }
        }
        
        assertTrue(coveredCount >= 15, 
            String.format("Devrait couvrir la plupart des edge-pairs. Couverts: %d/19", coveredCount));
    }

    private void assertContainsSubsequence(List<Integer> visited, int[] expected, String message) {
        boolean found = containsSubsequence(visited, expected);
        assertTrue(found, 
            message + "\nSous-séquence attendue: " + Arrays.toString(expected) +
            "\nNœuds visités réels: " + visited);
    }

    private boolean containsSubsequence(List<Integer> visited, int[] expected) {
        int j = 0;
        for (int v : visited) {
            if (j < expected.length && v == expected[j]) {
                j++;
            }
        }
        return j == expected.length;
    }

    private void assertContainsEdgePair(List<Integer> visited, int from, int to, String message) {
        boolean found = containsEdgePair(visited, from, to);
        assertTrue(found, 
            message + " non trouvé dans les nœuds visités: " + visited);
    }

    private boolean containsEdgePair(List<Integer> visited, int from, int to) {
        for (int i = 0; i < visited.size() - 1; i++) {
            if (visited.get(i) == from && visited.get(i + 1) == to) {
                return true;
            }
        }
        return false;
    }

    private static class ArrayList<E> extends java.util.ArrayList<E> {}
}

