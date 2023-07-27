package CountELV;

/**
 * Count edge length vectors (ELV) by complement for n = p1^2 * p2 and n = p1 * p2 * p3, where p1, p2, p3 are distinct primes
 */
public class ComplementELVCount {

    /**
     * Count ELV by complement for n = p1^2 * p2
     * @param p1 any prime
     * @param p2 any other prime
     */
    public static long countComplementP1SP2(int p1, int p2) {
        // Find size of each set
        int prod = p1 * p2 * p1;
        int halfProd = prod / 2;
        int P1S = halfProd / p1;
        int P2S = halfProd / p2;
        int P1Squared = p2 / 2;
        // P1 intersects P2
        int P1P2 = p1 / 2;
        // Relatively prime to prod: subtract prime multiples and add back the overlap that was removed
        int RPS = halfProd - P1S - P2S + P1P2;

        // Total possible permutations
        long allPermsTotal = permutationCount(halfProd, 3);
        allPermsTotal += permutationCount(halfProd, 2);
        allPermsTotal += halfProd;

        // n/2 - e
        int illegalSingle = halfProd - RPS;

        // e, any - 1
        int illegalDouble = RPS * (halfProd - 1);
        // exclusive P1, P1
        illegalDouble += (P1S - P1P2) * (P1S - 1);
        // exclusive P2, P2
        illegalDouble += (P2S - P1P2) * (P2S - 1);
        // P1 intersect P2, P1 u P2
        illegalDouble += (P1P2) * (halfProd - RPS - 1);

        // (not p1^2) u P1P2, any - 1, any - 2
        int illegalTriple = (halfProd - P1Squared - P1P2) * (halfProd - 1) * (halfProd - 2);
        // p1^2, (not p1) u p1^2 - 1, any - 2
        illegalTriple += P1Squared * (halfProd - P1S + P1Squared - 1) * (halfProd - 2);
        // p1^2, p1^1 u (p1 intersect p2), p1 - 2
        illegalTriple += P1Squared * (P1S - P1Squared) * (P1S - 2);
        // P1 intersect P2, e, any - 2
        illegalTriple += P1P2 * RPS * (halfProd - 2);
        // P1 intersect P2, exclusive P1, p1 - 2
        illegalTriple += P1P2 * (P1S - P1P2) * (P1S - 2);
        // P1 intersect P2, exclusive P2, p2 - 2
        illegalTriple += P1P2 * (P2S - P1P2) * (P2S - 2);
        // P1 intersect P2, P1 intersect P2 - 1, any - 2
        illegalTriple += P1P2 * (P1P2 - 1) * (halfProd - 2);

        // Sum total illegal moves and calculate total number of ELV
        long illegalTotal = illegalSingle + illegalDouble + illegalTriple;
        long ELVTotal = allPermsTotal - illegalTotal;
        return ELVTotal;
    }

    /**
     * Count ELV by complement for n = p1 * p2 * p3
     * @param p1 any prime
     * @param p2 any other prime
     * @param p3 any other unused prime
     */
    public static long countComplementP1P2P3(int p1, int p2, int p3) {
        // Find size of each set
        int prod = p1 * p2 * p3;
        int prodHalf = prod/2;
        int P1S = (p2 * p3) / 2;
        int P2S = (p1 * p3) / 2;
        int P3S = (p1 * p2) / 2;
        // P1 intersects P2
        int P1P2 = p3 / 2;
        // P1 intersects P3
        int P1P3 = p2 / 2;
        // P2 intersects P3
        int P2P3 = p1 / 2;
        // Relatively prime to prod: subtract prime multiples and add back the overlap that was removed twice
        int RPS = prodHalf - P1S - P2S - P3S + P1P2 + P1P3 + P2P3;

        // Total possible permutations
        long allPermsTotal = permutationCount(prodHalf, 3);
        allPermsTotal += permutationCount(prodHalf, 2);
        allPermsTotal += prodHalf;

        // n/2 - e
        int illegalSingle = prodHalf - RPS;

        // exclusives p1 / p2 / p3
        int illegalDouble = (P1S - (p2 / 2) - (p3 / 2)) * (P1S - 1) + (P2S - (p1 / 2) - (p3 / 2)) * (P2S - 1) + (P3S - (p1 / 2) - (p2 / 2)) * (P3S - 1);
        // size 2 intersections of p1, p2, p3
        illegalDouble += P1P2 * (P1S + P2S - P1P2 - 1) + P2P3 * (P2S + P3S - P2P3 - 1) + P1P3 * (P1S + P3S - P1P3 - 1);
        // relatively prime
        illegalDouble += RPS * (prodHalf - 1);

        // formulas 1 / 4 / 5
        int illegalTriple = (p3 / 2) * ((P1S - 2) * (P1S - (p3 / 2)) + (P2S - 2) * (P2S - (p3 / 2)) + (prodHalf - P1S - P2S + (p3 / 2) + P1P2 - 1) * (prodHalf - 2));
        // formulas 2 / 6 / 7
        illegalTriple += (p2 / 2) * ((P1S - 2) * (P1S - (p2 / 2)) + (P3S - 2) * (P3S - (p2 / 2)) + (prodHalf - P1S - P3S + (p2 / 2) + P1P3 - 1) * (prodHalf - 2));
        // formulas 3 / 8 / 9
        illegalTriple += (p1 / 2) * ((P2S - 2) * (P2S - (p1 / 2)) + (P3S - 2) * (P3S - (p1 / 2)) + (prodHalf - P2S - P3S + (p1 / 2) + P2P3 - 1) * (prodHalf - 2));
        // formula 10
        illegalTriple += (prodHalf - (P1P2 + P1P3 + P2P3) ) * (prodHalf - 1) * (prodHalf - 2);

        // Sum total illegal moves and calculate total number of ELV
        long illegalTotal = illegalSingle + illegalDouble + illegalTriple;
        long ELVTotal = allPermsTotal - illegalTotal;
        return ELVTotal;
    }

    /**
     * Count all possible permutations without repetition
     * @param num number that is permuted
     * @param length length of permutation
     * @return count of permutations
     */
    public static long permutationCount(int num, int length) {
        int prod = 1;
        int lower = num - length;
        for (int i = num; i > lower; i--) {
            prod *= i;
        }
        return prod;
    }
}