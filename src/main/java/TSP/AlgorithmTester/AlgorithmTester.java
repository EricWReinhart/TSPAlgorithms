package TSP.AlgorithmTester;

import TSP.CountELV.ComplementELVCount;
import TSP.CountELV.GreedyELVCount;
import TSP.CountELV.GridELVCount.GridELVCount;
import TSP.CountELV.PrimeELVCount;
import TSP.CountELV.SmartELVCount;
import TSP.CubeSolver.CubeSolver;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Test out algorithms that calculate the number of edge length vectors for different values of n
 */
public class AlgorithmTester {
    private static SmartELVCount smartAlg;

    public static void main(String[] args) {
        // List ELV for a given range of both Smart & Greedy and says when they're different
        compareGreedyVsSmart(10, 30);

        // n = p^3, a1 = p^2, a2 = p, a3 = relatively prime to n
        checkCubeSolver();

        // n = p1 * p2
        checkPrimeTimesPrime();

        // n = p^k
        checkPrimePowers();

        // n = p1^2 * p2 by complement
        checkP1SquaredP2();

        // n = p1 * p2 * p3 by complement
        checkP1P2P3();

        // n = p1^k * p2^j
        checkPrimeTimesPrimePowers(2, 2);

        // Show how quickly p1^k * p2^j grows, cannot check high values with SmartELVCount
        fastGrowthPrimeTimesPrimePowers(3, 5);

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
            GreedyELVCount.permuteSequence(i, 8);
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
    private static void checkPrimeTimesPrime() {
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
    private static void checkPrimePowers() {
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
    private static void checkP1SquaredP2() {
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
    private static void checkP1P2P3() {
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
     * Check if the CubeSolver algorithm works for a range of prime numbers and varying a3 values
     */
    public static void checkCubeSolver() {
        int[] primeList = {3, 5, 7, 11, 13, 17, 19, 23};
        ArrayList<Integer> wrongResults = new ArrayList<>();
        int primesChecked = 0;

        // Check each prime with varying a3 values
        for (int p : primeList) {
            double n = p * p * p;
            int largestA3 = (int) Math.floor(n / 2);
            for (int i = 0; i <= largestA3; i++) {
                // a3 cannot be a multiple of p
                if (i % p == 0)
                    continue;
                CubeSolver current = new CubeSolver(p, i, false);

                // Store values of n that produce a non-optimal solution
                if (!current.isOptimal()) {
                    current.printStats();
                    current.printTour();
                    wrongResults.add(i);
                    break;
                }
            }
            primesChecked++;
        }
        printResults(wrongResults, primesChecked, "Cube Solver");
    }

    /**
     * Check if the formula for counting ELV when n = p1^k * p2^j matches the smart algorithm for different prime numbers
     * @param k p1's exponent
     * @param j p2's exponent
     */
    private static void checkPrimeTimesPrimePowers(int k, int j){
        int[] primes = {2, 3, 5};
        ArrayList<Integer> wrongResults = new ArrayList<>();
        int valuesChecked = 0;
        for (int i = 0; i < primes.length-1; i++) {
            int p1 = primes[i];
            for (int l = i+1; l < primes.length; l++) {
                int p2 = primes[l];
                GridELVCount test = new GridELVCount(p1, k, p2, j);
                SmartELVCount smartAlg = new SmartELVCount((int) test.getN().longValue(), false);
                int smartCount = SmartELVCount.getCountELV();
                valuesChecked++;

                // Store values of n that produce wrong results
                if (test.getTotal().compareTo(BigInteger.valueOf(smartCount)) != 0) {
                    wrongResults.add((int) test.getN().longValue());
                }
            }
        }
        printResults(wrongResults, valuesChecked, "n = p1^k * p2^j");
    }

    /**
     * Show how quickly the ELV count grows for n = p1^k * p2^j with the GridELVCount algorithm
     * @param p1 prime
     * @param p2 different prime
     */
    private static void fastGrowthPrimeTimesPrimePowers(int p1, int p2){
        System.out.println("Fast Growth of p1^k * p2^j");
        for (int i = 1; i < 6; i++) {
            GridELVCount test = new GridELVCount(p1, i, p2, i);
            System.out.printf("n = %d^%d * %d^%d = %6d, count: %d%n", p1, i, p2, i, (long) (Math.pow(p1,i) * Math.pow(p2,i)), test.getTotal());
        }
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
