package CountELV;

import java.util.ArrayList;

public class SmartELVCount {
    private static int countELV;
    private final boolean save;
    private final ArrayList<ArrayList<Integer>> gSequencesList;
    private final ArrayList<int[]> edgeLengthVectors;

    /**
     * Obtain a list of G-Sequences for the given n and call a function to count all the ELV from that list
     * @param n number that the G-Sequences and ELV are based on
     * @param save boolean to save ELV instead of just counting them
     */
    public SmartELVCount(int n, boolean save){
        countELV = 0;
        this.save = save;
        edgeLengthVectors = new ArrayList<>();
        gSequencesList = GSequence.getGSequenceList(n);
        gSequencesSetUp(n);
    }

    /**
     * Setup for finding ELV
     */
    private void gSequencesSetUp(int n){
        // For each G-Sequence, create a length n/2 edge vector and find its corresponding edge length vectors
        for (ArrayList<Integer> sequence : gSequencesList) {
            int[] edgeVector = new int[n/2];
            findELV(sequence, edgeVector, 1, n);
        }
    }

    /**
     * Find and count the number of edge length vectors (ELV) from a given G-Sequence
     * @param gSequence current G-Sequence used to build the ELV
     * @param edgeVector current edge vector that is being changed
     * @param index current index
     */
    private void findELV(ArrayList<Integer> gSequence, int[] edgeVector, int index, int n){
        if (index >= gSequence.size() ){
            if (save)
                edgeLengthVectors.add(copyArray(edgeVector));
            countELV++;
            return;
        }

        // Find all possible phi values / edge lengths
        ArrayList<Integer> possibleEdges = findAllGCDCandidates(gSequence.get(index-1), gSequence.get(index), n);

        // Place the difference of the G-Sequence's previous and current value at the index phi - 1
        // Then recursively finish each possible sequence
        for (int phi : possibleEdges) {
            int[] clone = copyArray(edgeVector);
            clone[phi - 1] = gSequence.get(index-1) - gSequence.get(index);
            findELV(gSequence, clone, index+1, n);
        }
    }

    /**
     * Find all possible edges (i) by adding them if the GCD(n,i) or GCD(phiValue,i) equal the target num
     * @param phiValue current phi value
     * @param targetNum target value
     * @param n counting ELV for number n
     * @return ArrayList of possible edges
     */
    private ArrayList<Integer> findAllGCDCandidates(int phiValue, int targetNum, int n){
        ArrayList<Integer> possibleEdges = new ArrayList<>();
        for (int i = 1; i <= n/2; i++) {
            if (GCD(n, i) == targetNum || GCD(phiValue, i) == targetNum){
                possibleEdges.add(i);
            }
        }
        return possibleEdges;
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

    /** Create a copy of an Array */
    private static int[] copyArray(int[] src){
        int[] output = new int[src.length];
        System.arraycopy(src, 0, output, 0, output.length);
        return output;
    }

    public static int getCountELV() {
        return countELV;
    }
}