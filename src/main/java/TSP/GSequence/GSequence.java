package TSP.GSequence;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Create all G-Sequences
 */
public class GSequence {
    // ArrayList that holds every possible G-Sequence for a given n
    private static ArrayList<ArrayList<Integer>> gSequencesList = new ArrayList<>();

    /**
     * Create a sequence that starts with n then call traverseFactors to recursively build all possible sequences
     */
    private static void computeGSequences(int n){
        gSequencesList = new ArrayList<>();
        ArrayList<Integer> sequence = new ArrayList<>();
        sequence.add(n);
        traverseFactors(n, sequence);
    }

    /**
     * Recursively create every possible G-Sequence and add them to an ArrayList
     * @param currNum current number
     * @param sequence current sequence that is being built
     */
    private static void traverseFactors(int currNum, ArrayList<Integer> sequence){
        // add a sequence to the list when the currNum is 1 since that means it's the end of a sequence
        if (currNum == 1){
            gSequencesList.add(sequence);
            return;
        }

        // Obtain all factors of the current number
        ArrayList<Integer> factors = getFactors(currNum);

        // For each factor of the current number, clone the sequence, add that factor to the clone, and
        // recursively call traverseFactors to generate every possible sequence
        for (int factor : factors) {
            ArrayList<Integer> clone = copyArrayList(sequence);
            clone.add(factor);
            traverseFactors(factor, clone);
        }
    }

    /**
     * Find all factors of a given number
     * @param num number to get factors of
     * @return ArrayList of factors
     */
    private static ArrayList<Integer> getFactors(int num){
        ArrayList<Integer> factors = new ArrayList<>();

        // Start at 2 to exclude 1 and n from the factor list
        for(int i = 2; i <= num/i; ++i) {
            if(num % i == 0) {
                // If i is a factor, num/i is also a factor
                factors.add(i);
                if (num/i != i)
                    factors.add(num/i);
            }
        }

        // Add 1 to the end of the factors list
        factors.add(1);

        // Sort the factors
        Collections.sort(factors);

        return factors;
    }

    /** Create a copy of an ArrayList */
    private static ArrayList<Integer> copyArrayList(ArrayList<Integer> source){
        return new ArrayList<>(source);
    }

    /**
     * Generate all G-Sequences for n
     * @param n find all G-Sequences for n
     * @return ArrayList of all G-Sequences
     */
    public static ArrayList<ArrayList<Integer>> getGSequenceList(int n) {
        computeGSequences(n);
        return gSequencesList;
    }
}