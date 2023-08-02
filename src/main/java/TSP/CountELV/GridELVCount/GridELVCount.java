package TSP.CountELV.GridELVCount;

import java.math.BigInteger;

/**
 * Algorithm that counts ELV much faster than a permutation approach, which provides more data by
 * solving larger values of n, when n = p1^k * p2^j (k, j > 0)
 * If you want a debug/more output, set print = true
 */
public class GridELVCount {
    private BigInteger n;
    private final int p1;
    private final int p1Power;
    private final int p2;
    private final int p2Power;
    private Box root;
    public static Box[][] boxTable;
    public static BigInteger[][] lookUpTable;
    public static boolean print;

    /**
     * Compute the ELV count for n = prime1^pow1 * prime2^pow2
     */
    public GridELVCount(int prime1, int pow1, int prime2, int pow2){
        p1 = prime1;
        p1Power = pow1;
        p2 = prime2;
        p2Power = pow2;

        if(print)
            System.out.printf("computing total for %d^%d * %d^%d\n", p1, p1Power, p2, p2Power);
        n = BigInteger.ONE;
        for (int i = 0; i < pow1; i++) {
            n = n.multiply(BigInteger.valueOf(prime1));
        }
        for (int i = 0; i < pow2; i++) {
            n = n.multiply(BigInteger.valueOf(prime2));
        }
        boxTable = new Box[p1Power+1][p2Power+1];
        makeLookUpTable();

        if (print){
            System.out.println("look up table");
        }

        generateBoxes();
    }

    /**
     * Kicks off computation of ELV count by creating the root box that
     * represents n and starts the chain of creating children
     */
    private void generateBoxes(){
        root = new Box(p1Power, p2Power, BigInteger.ONE, BigInteger.ZERO);
        root.createChildren();
        root.computeTotal();
    }

    /**
     * The lookup table is a grid that represents the different power combinations of p1 and p2
     * for example, lookupTable[1][2] is the amount of numbers < n/2 such that they have p1^1 * p2^2 as a factor
     * a number goes into the largest slot it possibly can fit
     * for example, if a number has p1^2 * p2^2, it would NOT be placed in lookupTable[1][1], It would instead be placed in lookupTable[2][2]
     */
    private void makeLookUpTable(){
        int smallerP = Math.min(p1, p2);
        BigInteger product = BigInteger.ONE;
        int count = 0;
        while (product.compareTo(n) < 1){
            product = product.multiply(BigInteger.valueOf(smallerP));
            count++;
        }
        lookUpTable = new BigInteger[count * 3][count * 3];
        for (int i = 0; i < lookUpTable.length; i++) {
            for (int j = 0; j < lookUpTable.length; j++) {
                lookUpTable[i][j] = BigInteger.ZERO;
            }
        }
        for (BigInteger i = BigInteger.ONE; i.compareTo(n.divide(BigInteger.TWO)) < 1; i = i.add(BigInteger.ONE)) {
            int[] coords = findLargestPowerFactor(i);
            lookUpTable[coords[0]][coords[1]] = lookUpTable[coords[0]][coords[1]].add(BigInteger.ONE);
        }
    }

    /**
     * Finds which spot in the lookupTable the number value belongs to
     * basically just continually divides by p1 and p2 and counts how much each one happens then returns those counts
     * as a length 2 array
     */
    private int[] findLargestPowerFactor(BigInteger value){
        int[] coords = new int[2];
        boolean triggered;
        do {
            triggered = false;
            if (value.mod(BigInteger.valueOf(p1)).compareTo(BigInteger.ZERO) == 0){
                coords[1]++;
                value = value.divide(BigInteger.valueOf(p1));
                triggered = true;
            }
            if (value.mod(BigInteger.valueOf(p2)).compareTo(BigInteger.ZERO) == 0){
                coords[0]++;
                value = value.divide(BigInteger.valueOf(p2));
                triggered = true;
            }

        } while(triggered);

        return coords;
    }

    /**
     * Summing a lookUpTable row answers the question "how many numbers have exactly this p2 power with any p1 power
     * example: summing row 1 gives the count of all numbers with p1^1 as its largest p1 power
     */
    public static BigInteger sumLookUpRow(int row, int startingPower, int lastPower){
        if (lastPower == -1)
            lastPower = lookUpTable.length-1;
        BigInteger sum = BigInteger.ZERO;
        for (int i = startingPower; i <= lastPower; i++) {
            sum = sum.add(lookUpTable[row][i]);
        }
        return sum;
    }

    /**
     * Summing a lookUpTable col answers the question "how many numbers have exactly this p1 power with any p2 power
     * example: summing col 2 gives the count of all numbers with p2^2 as its largest p2 power
     */
    public static BigInteger sumLookUpColumn(int column, int startingPower, int lastPower) {
        if (lastPower == -1)
            lastPower = lookUpTable.length-1;
        BigInteger sum = BigInteger.ZERO;
        for (int i = startingPower; i <= lastPower; i++) {
            sum = sum.add(lookUpTable[i][column]);
        }
        return sum;
    }

    public BigInteger getN() { return n;}

    public BigInteger getTotal(){
        return root.getTotal();
    }
}

