package TSP.CountELV.GridELVCount;

import TSP.CountELV.SmartELVCount;

import java.math.BigInteger;

/**
 * Algorithm that counts ELV much faster than a permutation approach, which provides more data by
 * solving larger values of n, when n = p1^k * p2^j (k, j > 0)
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

    public static void main(String[] args) {
        print = false;
        long startTime = System.nanoTime();
//        GridSlacker test = new GridSlacker(2, 3, 3, 3);
//        testCorrectAnswer();
        testRunTimes();
        long endTime = System.nanoTime();

        long duration = (endTime - startTime) / 1000000;

        print = true;
        //test.root.computeTotal();
        System.out.printf("execution took %d milliseconds, or as %f seconds%n",duration, duration/1000.0);
    }

    private static void testRunTimes(){
        for (int i = 1; i < 4; i++) {
            long startTime = System.nanoTime();
            GridELVCount test = new GridELVCount(17, i, 13, i);
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1000000;
            System.out.println("i: " + i + ", milliseconds: " + duration + ", count: " + test.getTotal());
        }
    }

    private static void testCorrectAnswer(){
        int[] primes = {2,3,5};
        for (int i = 0; i < primes.length-1; i++) {
            int p1 = primes[i];
            for (int j = i+1; j < primes.length; j++) {
                int p2 = primes[j];
                GridELVCount test = new GridELVCount(p1, 2, p2, 3);
                SmartELVCount smartAlg = new SmartELVCount((int) test.n.longValue(), false);
                int val = SmartELVCount.getCountELV();
                if (test.getTotal().compareTo(BigInteger.valueOf(val)) == 0){
                    System.out.println("passed for " + p1 + ", " + p2);
                }
                else{
                    System.out.println("FAILED for " + p1 + ", " + p2 + ", got " + test.getTotal());
                }
            }

        }
    }

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

    private void generateBoxes(){
        root = new Box(p1Power, p2Power, BigInteger.ONE, BigInteger.ZERO);
        root.createChildren();
        root.computeTotal();
    }

    public BigInteger getTotal(){
        return root.getTotal();
    }

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

    public static BigInteger sumLookUpRow(int row, int startingPower, int lastPower){
        if (lastPower == -1)
            lastPower = lookUpTable.length-1;
        BigInteger sum = BigInteger.ZERO;
        for (int i = startingPower; i <= lastPower; i++) {
            sum = sum.add(lookUpTable[row][i]);
        }
        return sum;
    }

    public static BigInteger sumLookUpColumn(int column, int startingPower, int lastPower) {
        if (lastPower == -1)
            lastPower = lookUpTable.length-1;
        BigInteger sum = BigInteger.ZERO;
        for (int i = startingPower; i <= lastPower; i++) {
            sum = sum.add(lookUpTable[i][column]);
        }
        return sum;
    }
}

