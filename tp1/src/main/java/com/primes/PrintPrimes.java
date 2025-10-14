package com.primes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * PrintPrimes - Test de couverture Edge-Pair avec suivi des nœuds CFG.
 * 
 * Mapping des nœuds CFG (0-10):
 * - Nœud 0: Entrée programme
 * - Nœud 1: Initialisation x = 2
 * - Nœud 2: Condition de boucle (x <= N)
 * - Nœud 3: Entrée corps de boucle
 * - Nœud 4: Entrée isPrime, i = 2
 * - Nœud 5: Condition boucle interne (i <= sqrt(n))
 * - Nœud 6: Affichage nombre premier
 * - Nœud 7: Incrément x++
 * - Nœud 8: Vérification divisibilité (n % i == 0)
 * - Nœud 9: Incrément i++
 * - Nœud 10: Sortie programme
 */
public class PrintPrimes {

    // Liste thread-safe pour suivre les nœuds visités
    private static final List<Integer> visited = Collections.synchronizedList(new ArrayList<>());


    private static void visit(int node) {
        visited.add(node);
    }

    public static void resetVisited() {
        synchronized (visited) {
            visited.clear();
        }
    }

    
    public static List<Integer> getVisited() {
        synchronized (visited) {
            return Collections.unmodifiableList(new ArrayList<>(visited));
        }
    }

   
    public static void runProgram(int N) {
        visit(0);       
        prime_N(N);
        visit(10);     
    }

    /**
     * Vérifie si un nombre est premier.
     */
    public static boolean isPrime(int n) {
        visit(4);       // Nœud 4: Entrée isPrime
        
        if (n <= 1) {
            return false;
        }
        
        for (int i = 2; i <= Math.sqrt(n); i++) {
            visit(5);   // Nœud 5: Condition de boucle
            
            if (n % i == 0) {
                visit(8); // Nœud 8: Divisibilité trouvée
                return false;
            }
            
            visit(9);   // Nœud 9: Incrément i++
        }
        
        visit(5);       // Nœud 5: Dernière vérification de condition
        return true;
    }

    /**
     * Affiche tous les nombres premiers de 2 à N.
     */
    public static void prime_N(int N) {
        visit(1);       // Nœud 1: Entrée prime_N
        
        System.out.println("Prime numbers up to " + N + " are:");
        
        for (int x = 2; x <= N; x++) {
            visit(2);   // Nœud 2: Condition de boucle
            visit(3);   // Nœud 3: Entrée corps de boucle
            
            if (isPrime(x)) {
                visit(6); // Nœud 6: Affichage nombre premier
                System.out.print(x + " ");
            }
            
            visit(7);   // Nœud 7: Incrément x++
        }
        
        visit(2);       // Nœud 2: Dernière vérification de condition
        System.out.println();
    }

    /**
     * Méthode principale pour exécution autonome.
     */
    public static void main(String[] args) {
        int N = 45;
        runProgram(N);
        System.out.println("\nNœuds visités: " + getVisited());
    }
}

