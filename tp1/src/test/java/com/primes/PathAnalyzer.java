package com.primes;

import java.util.List;

/**
 * PathAnalyzer - Utilitaire pour analyser et afficher les chemins d'exécution réels.
 * Aide à déterminer quels chemins de test sont faisables vs infaisables.
 */
public class PathAnalyzer {

    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("  COUVERTURE EDGE-PAIR: ANALYSE DE FAISABILITÉ");
        System.out.println("=".repeat(80));
        System.out.println();

        int[] testInputs = {1, 2, 3, 4, 5, 9, 10};
        
        for (int N : testInputs) {
            PrintPrimes.resetVisited();
            PrintPrimes.runProgram(N);
            List<Integer> visited = PrintPrimes.getVisited();
            
            System.out.println("N = " + N + ":");
            System.out.println("  Chemin Réel: " + visited);
            System.out.println("  Longueur: " + visited.size() + " nœuds");
            System.out.println("  Edge-Pairs Couverts:");
            printEdgePairs(visited);
            System.out.println();
        }
        
        System.out.println("=".repeat(80));
        System.out.println("  CHEMINS DE TEST THÉORIQUES vs RÉELS");
        System.out.println("=".repeat(80));
        System.out.println();
        
        analyzeTestPath("TP1", 1, new int[]{0,1,2,10});
        analyzeTestPath("TP2", 4, new int[]{0,1,2,3,4,5,6,7,2,10});
        analyzeTestPath("TP3", 9, new int[]{0,1,2,3,4,5,8,9,5,6,7,2,10});
        analyzeTestPath("TP4", 2, new int[]{0,1,2,3,7,2,3,7,2,10});
        analyzeTestPath("TP5", 5, new int[]{0,1,2,3,4,5,8,9,5,8,7,2,10});
    }

    private static void printEdgePairs(List<Integer> path) {
        int count = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            int from = path.get(i);
            int to = path.get(i + 1);
            System.out.print("    [" + from + "," + to + "]");
            count++;
            if (count % 8 == 0) {
                System.out.println();
            }
        }
        if (count % 8 != 0) {
            System.out.println();
        }
    }

    private static void analyzeTestPath(String name, int N, int[] expectedPath) {
        PrintPrimes.resetVisited();
        PrintPrimes.runProgram(N);
        List<Integer> actualPath = PrintPrimes.getVisited();
        
        System.out.println(name + " (N=" + N + "):");
        System.out.println("  Attendu (Théorique): " + arrayToString(expectedPath));
        System.out.println("  Réel (Runtime):      " + actualPath);
        
        boolean matches = containsSubsequence(actualPath, expectedPath);
        if (matches) {
            System.out.println("  ✓ FAISABLE: Sous-séquence attendue trouvée dans le chemin réel");
        } else {
            System.out.println("  ✗ INFAISABLE: Sous-séquence attendue NON trouvée - chemin théorique impossible");
        }
        System.out.println();
    }

    private static String arrayToString(int[] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(arr[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    private static boolean containsSubsequence(List<Integer> path, int[] expected) {
        int j = 0;
        for (int node : path) {
            if (j < expected.length && node == expected[j]) {
                j++;
            }
        }
        return j == expected.length;
    }
}

