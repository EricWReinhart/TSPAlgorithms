package CountELV;

import java.util.ArrayList;

/**
 * Test out algorithms that calculate the number of edge length vectors for different values of n
 */
public class AlgorithmTester {
    private static SmartELVCount smartAlg;

    public static void main(String[] args) {
        // List ELV for a given range of both Smart & Greedy and says when they're different
        compareGreedyVsSmart(10, 25);

        // n = p1 * p2
        checkPrimeTimesPrimeAccuracy();

        // n = p^k
        checkPrimePowersAccuracy();

        // n = p1^2 * p2 by complement
        checkP1SquaredP2Accuracy();

        // n = p1 * p2 * p3 by complement
        checkP1P2P3Accuracy();
    }

    /**
     * Compare Greedy & Smart algorithms for values of n ranging from start to end index (inclusive)
     * Print out the values of n where they differ
     * @param start start of range
     * @param end end of range
     */
    private static void compareGreedyVsSmart(int start, int end) {
        ArrayList<Integer> wrongResults = new ArrayList<>();
        int valuesChecked = 0;

        // Check values of n ranging from start to end (inclusive)
        for (int i = start; i <= end; i++) {
            smartAlg = new SmartELVCount(i, false);
            GreedyELVCount greedyAlg = new GreedyELVCount(i, 8);
            int smartCount = SmartELVCount.getCountELV();
            int greedyCount = GreedyELVCount.getCountELV();
            valuesChecked++;

            // Store values of n that produce wrong results
            if (smartCount != greedyCount) {
                wrongResults.add(i);
            }
        }
        printResults(wrongResults, valuesChecked, "Greedy vs Smart");
    }

    /**
     * Check if the formula for counting ELV when n = p1 * p2 matches the smart algorithm for a range of prime numbers
     */
    private static void checkPrimeTimesPrimeAccuracy() {
        int[] primeList = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43};
        int ELVFromCounting;
        int ELVFromFormula;
        int primeProduct;
        ArrayList<Integer> wrongResults = new ArrayList<>();
        int valuesChecked = 0;

        // Check every product of distinct primes from the lists above
        for (int p1 : primeList) {
            for (int p2 : primeList) {
                if (p2 > p1) {
                    primeProduct = p1 * p2;
                    smartAlg = new SmartELVCount(primeProduct, false);
                    ELVFromCounting = SmartELVCount.getCountELV();
                    ELVFromFormula = PrimeELVCount.primeTimesPrime(p1, p2);
                    valuesChecked++;

                    // Store values of n that produce wrong results
                    if (ELVFromCounting != ELVFromFormula) {
                        wrongResults.add(primeProduct);
                    }
                }
            }
        }
        printResults(wrongResults, valuesChecked, "n = p1 * p2");
    }

    /**
     * Check if the formula for counting ELV when n = p1^k matches the smart algorithm for a range of prime numbers
     * and those numbers raised to a power
     */
    private static void checkPrimePowersAccuracy() {
        // Exclude 1, 2 from list of primes
        int[] primeList = {3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43};
        int[] powerList = {1, 2, 3};
        int ELVFromCounting;
        int ELVFromFormula;
        int primePower;
        ArrayList<Integer> wrongResults = new ArrayList<>();
        int valuesChecked = 0;

        // Check every prime raised to a power from the lists above
        for (int prime : primeList) {
            for (int k : powerList) {
                // Smart algorithm cannot handle 13^3 or higher
                if (prime > 11 && k == 3) {
                    break;
                }
                primePower = (int) Math.pow(prime,k);
                smartAlg = new SmartELVCount(primePower,  false);
                ELVFromCounting = SmartELVCount.getCountELV();
                ELVFromFormula = PrimeELVCount.primeRaisedToK(prime, k);
                valuesChecked++;

                // Store values of n that produce wrong results
                if (ELVFromCounting != ELVFromFormula) {
                    wrongResults.add(primePower);
                }
            }
        }
        printResults(wrongResults, valuesChecked, "n = p1^k");
    }

    /**
     * Check if the formula for counting ELV when n = p1^2 * p2 matches the smart algorithm for a range of prime numbers
     * squared times a different prime
     */
    private static void checkP1SquaredP2Accuracy() {
        int[] primeList = {2, 3, 5, 7, 11, 13, 17};
        int ELVFromCounting;
        long ELVFromFormula;
        int product;
        ArrayList<Integer> wrongResults = new ArrayList<>();
        int valuesChecked = 0;

        // Check every product of p1^2 with distinct primes from the lists above
        for (int p1 : primeList) {
            for (int p2 : primeList) {
                if (p2 > p1) {
                    product = p1 * p1 * p2;
                    smartAlg = new SmartELVCount(product, false);
                    ELVFromCounting = SmartELVCount.getCountELV();
                    ELVFromFormula = ComplementELVCount.countComplementP1SP2(p1, p2);
                    valuesChecked++;

                    // Store values of n that produce wrong results
                    if (ELVFromCounting != ELVFromFormula) {
                        wrongResults.add(product);
                    }
                }
            }
        }
        printResults(wrongResults, valuesChecked, "n = p1^2 * p2");
    }

    /**
     * Check if the formula for counting ELV when n = p1 * p2 * p3 matches the smart algorithm for a range of prime numbers
     * squared times a different prime
     */
    private static void checkP1P2P3Accuracy() {
        int[] primeList = {2, 3, 5, 7, 11, 13};
        int ELVFromCounting;
        long ELVFromFormula;
        int product;
        ArrayList<Integer> wrongResults = new ArrayList<>();
        int valuesChecked = 0;

        // Check every product of p1^2 with distinct primes from the lists above
        for (int p1 : primeList) {
            for (int p2 : primeList) {
                for (int p3 : primeList) {
                    // only the product of distinct primes is checked
                    if (p3 > p2 && p2 > p1) {
                        product = p1 * p2 * p3;
                        smartAlg = new SmartELVCount(product, false);
                        ELVFromCounting = SmartELVCount.getCountELV();
                        ELVFromFormula = ComplementELVCount.countComplementP1P2P3(p1, p2, p3);
                        valuesChecked++;

                        // Store values of n that produce wrong results
                        if (ELVFromCounting != ELVFromFormula) {
                            wrongResults.add(product);
                        }
                    }
                }
            }
        }
        printResults(wrongResults, valuesChecked, "n = p1 * p2 * p3");
    }


    /**
     * Prints the number of values tested and states the values that failed if they exist
     * @param wrongResults ArrayList of incorrect results
     * @param valuesChecked number of values of n that were checked
     * @param n tested value of n
     */
    private static void printResults(ArrayList<Integer> wrongResults, int valuesChecked, String n) {
        if (wrongResults.size() > 0) {
            System.out.printf("Tested %3d values for %s : FAILED : Wrong values are %s %n", valuesChecked, n, wrongResults);
        }
        else {
            System.out.printf("Tested %3d values for %s : SUCCESS%n", valuesChecked, n);
        }
    }
}
