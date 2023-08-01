package TSP.CountELV;

/**
 * Count ELV when multiplying distinct primes & raising p to a power
 */
public class PrimeELVCount {
    /**
     * Count ELV when n = p1 * p2,  p2 > p1 >= 2
     * @param p1 prime
     * @param p2 prime that's greater than p1
     * @return ELV count
     */
    public static int primeTimesPrime(int p1, int p2) {
        int num = p1 * p2;
        int col0 = p1 / 2;
        int col1 = p2 / 2;
        int col2 = (num / 2) - col1 - col0;
        return col0 * (col1 + col2) + col2 * (col1 + 1)  + col0 * col1;
    }

    /**
     * Count ELV when n = p^k, p ≠ 1,2
     * @param p prime
     * @param k exponent
     * @return ELV count
     */
    public static int primeRaisedToK(int p, int k) {
        int total = (int) ( (p/2) * Math.pow(p, k-1) );
        for (int i = 0; i <= k-2; i++) {
            total *= (int) ( (p/2) * Math.pow(p, i) + 1);
        }
        return total;
    }
}
