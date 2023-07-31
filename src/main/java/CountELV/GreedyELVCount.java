package CountELV;

import java.util.ArrayList;

/**
 * Create all possible encoding sequences with permutations
 */
public class GreedyELVCount {
    private static int countELV;

    /**
     * Find all possible values in an encoding sequence for a given n then recursively create
     * every valid sequence starting with each of these values
     * @param n counting ELV for the number n
     * @param sumOfExponents longest length permutation = sum of exponents of the primes whose product is n
     */
    public static void permuteSequence(int n, int sumOfExponents){
        countELV = 0;
        // Highest possible term in an encoding sequence after n is n/2
        int maxValue = n/2;

        // Add all possible values (1 to n/2 inclusive) to an arraylist
        ArrayList<Integer> possibleValues = new ArrayList<>();
        for (int i = 1; i <= maxValue ; i++) {
            possibleValues.add(i - 1, i);
        }

        // For each possible value, recursively create every possible sequence that starts with this value
        // and remove this value from the options for the rest of the sequence
        for (int possibleValue: possibleValues) {
            int[] permutation = new int[sumOfExponents];
            ArrayList<Integer> updatedValues = copyArrayList(possibleValues);
            updatedValues.remove((Integer) possibleValue);
            permutation[0] = possibleValue;
            recurse(permutation, updatedValues, 1, GCD(n, possibleValue));
        }
    }

    /**
     * Recursively create every possible sequence that starts with the inputted value
     * @param perm array of a permutation that will be filled
     * @param possibleValues all valid numbers in an encoding sequence
     * @param index current index
     * @param prevGCD GCD of the previous GCD and the current value
     */
    private static void recurse(int[] perm, ArrayList<Integer> possibleValues, int index, int prevGCD){
        // GCD of 1 means the end of a sequence is reached
        if (prevGCD == 1){
            countELV++;
            return;
        }

        // Recursively create the rest of the sequence by attempting to add every possible value one-at-a-time
        for (int possibleValue : possibleValues) {
            // The last element in the sequence must have a GCD of 1 with the previous value in a valid sequence
            if (index == (perm.length - 1) && GCD(perm[index - 1], possibleValue) != 1){
                continue;
            }

            // Only create sequences with strictly decreasing GCD
            int gcd = GCD(prevGCD, possibleValue);
            if (gcd >= prevGCD){
                continue;
            }

            // Clone the permutation and add a new value to the next available spot
            int[] clone = perm.clone();
            clone[index] = possibleValue;

            // Remove the current value from the options for the rest of the sequence
            ArrayList<Integer> updatedValues = copyArrayList(possibleValues);
            updatedValues.remove((Integer) possibleValue);

            // Recursively create every possible sequence that starts with the previously added values
            recurse(clone, updatedValues, index+1, gcd);
        }
    }

    /** Create a copy of an ArrayList */
    private static ArrayList<Integer> copyArrayList(ArrayList<Integer> source){
        return new ArrayList<>(source);
    }


    /**
     * Return the greatest common divisor (GCD) of 2 numbers
     * @param n1 first number
     * @param n2 second number
     * @return GCD of n1 and n2
     */
    private static int GCD(int n1, int n2){
        if (n2 == 0)
            return n1;
        return GCD(n2, n1 % n2);
    }

    public static int getCountELV() {
        return countELV;
    }
}