package TSP.CubeSolver;

/**
 * Solve Circulant TSP instances such that n = p^3, a1 = p^2, a2 = p, a3 = relatively prime to n
 */
public class CubeSolver {
    private static final int startIndex = 1;
    private final int prime;
    private final int a1;
    private final int a2;
    private final int a3;
    private final int n;
    private int lastNode;
    private int a1Count;
    private int a2Count;
    private int a3Count;
    private int[] tour;

    /**
     * Solve Circulant TSP instances such that n = p^3, a1 = p^2, a2 = p, a3 = relatively prime to n
     * @param p prime
     * @param a3 relatively prime to n
     * @param print boolean to print results
     */
    public CubeSolver(int p, int a3, boolean print){
        this.prime = p;
        this.a1 = prime * prime;
        this.a2 = prime;
        this.a3 = a3;
        this.n = prime * prime * prime;
        findTour(print);
    }

    /**
     * Check if the number of each type of move is optimal
     * @return boolean whether the move is optimal
     */
    public boolean isOptimal(){
        if (tour[lastNode] != 1)
            return false;
        if (a1Count != prime * prime * (prime -1))
            return false;
        if(a2Count  != prime * (prime-1))
            return false;
        return a3Count == prime;
    }

    /**
     * Finds the optimal tour
     * @param print boolean to print results
     */
    private void findTour(boolean print){
        int S = modN(startIndex - a3);

        int column = prime - modP(a3 - prime);
        int row = modP(prime - a3/prime) - 1;
        if (print){
            System.out.println("turn around node: " + S);
            System.out.println("target coord: (" + row + ", " + column + ")");
        }

        // recall use of 0 indexing, so starting at (0,0)
        int deltaRow = row;
        int deltaCol = column;

        int leftMoves;
        int rightMoves;
        int pairs;

         // if deltaCol is odd, then odd amount of +/- 1 can make that delta
         // if it is even, an odd number of 1's can't, so we must use the negative delta to go around the other side
         // since prime is odd, even - odd will be an odd number and thus can be constructed by adding +/- 1
        if (deltaCol % 2 == 1){
            pairs = (prime - deltaCol) / 2;
            leftMoves = deltaCol + pairs;
            rightMoves = prime - leftMoves;
        }
        else{
            pairs = deltaCol / 2;
            rightMoves = prime - deltaCol + pairs;
            leftMoves = prime - rightMoves;
        }

         // recall, an "up" is plus a row, "down" is minus a row
         // additionally, a left movement will cause a -1 to row since it wraps upwards around the side
         //               a right movement will cause a +1 to row since it wraps down the side
        int totalV = prime * prime;
        int latE;
        int upMoves = 0;
        int downMoves = 0;

         // the lateralEffect on vertical is the amount of +1 from right and -1 from going left, which can be
         //              * found using rightMoves - leftMoves
         // if left < right, that means a right movement is going to start from the first row, this means that
         // specific right does not actually "wrap around" the side and cause a +1 to row
         // thus if left < right, subtract 1 extra to account for this lack of +1 from that right movements
        if (leftMoves < rightMoves){
            latE = rightMoves - leftMoves - 1;
        }
        else{
            latE = rightMoves - leftMoves;
        }

        latE = latE * modP(a2/prime);

         // total vertical movements is the # columns per sheet * # sheets --> p * p. Since this is odd * odd, totalV must be odd
         // If the sum of deltaRow and latE is even, then totalV - sum = odd number
         // this odd number cannot be split into unproductive pairs (+1 row -1 row) that do nothing
         // thus we must switch deltaRow to its negative variant, which will then make totalV - sum = even number
        if ((latE + deltaRow) % 2 == 0){
            deltaRow -= (-prime);
        }
        pairs = (totalV - Math.abs(latE) - Math.abs(deltaRow)) / 2;

        // if latE has a net positive effect on rows, add additional downMoves to counter this
        if (latE > 0)
            downMoves += latE;
        // else latE must have net negative effect on rows, add more upMoves to cancel this out
        else
            upMoves += Math.abs(latE);
        // if deltaRow is positive, we need that many up moves to reach it
        if (deltaRow > 0)
            upMoves += deltaRow;
        // else we need that many more down moves to wrap around the other way
        else
            downMoves += Math.abs(deltaRow);
        // distribute the remaining vertical moves equally between up and down moves, cancelling out the effects
        upMoves += pairs;
        downMoves += pairs;

//        if (print){
//            System.out.printf("leftMoves: %d\nrightMoves: %d\nupMoves: %d\ndownMoves: %d%n", leftMoves, rightMoves, upMoves, downMoves);
//            System.out.printf("latE: %d\ndeltaRow: %d\ndeltaCol %d,\nRowPairs: %d%n", latE, deltaRow, deltaCol, pairs);
//        }

        // initialize an array that starts at 1
        tour = new int[n+1];
        tour[0] = 1;

        // loop over each sheet
        for (int sheetIndex = 0; sheetIndex < prime; sheetIndex++) {

            // if the left movements have not all been done, move left and decrement leftMoves
            if(leftMoves > 0){
                for (int i = 0; i < prime; i++) {
                    // if not all up movements have been done, move up and decrement upMoves
                    if (upMoves > 0) {
                        for(int j = 0; j < prime -1; j++){
                            setNext(Jump.Up);
                        }
                        upMoves--;
                    } else {
                        for(int j = 0; j < prime-1; j++){
                            setNext(Jump.Down);
                        }
                    }
                    // in the last column, left/right movement should not occur
                    // this statement will run for all columns except for the last column
                    if(i < prime-1){
                        setNext(Jump.Left);
                    }
                }
                leftMoves--;
            }
            // moving right
            else{
                for (int i = 0; i < prime; i++) {
                    if (upMoves > 0) {
                        for(int j = 0; j < prime-1; j++){
                            setNext(Jump.Up);
                        }
                        upMoves--;
                    } else {
                        for(int j = 0; j < prime-1; j++){
                            setNext(Jump.Down);
                        }
                    }
                    if(i < prime-1){
                        setNext(Jump.Right);
                    }
                }
            }
            setNext(Jump.Out);
        }
        if (print){
            printTour();
            printStats();
        }
    }

    /**
     * Based on the direction of the next move, place its corresponding value into the tour array
     * @param direction direction of the next "jump"
     */
    private void setNext(Jump direction){
        switch (direction) {
            case Left -> {
                tour[lastNode + 1] = modN(tour[lastNode] - a2);
                a2Count++;
            }
            case Right -> {
                tour[lastNode + 1] = modN(tour[lastNode] + a2);
                a2Count++;
            }
            case Up -> {
                tour[lastNode + 1] = modN(tour[lastNode] - a1);
                a1Count++;
            }
            case Down -> {
                tour[lastNode + 1] = modN(tour[lastNode] + a1);
                a1Count++;
            }
            case Out -> {
                tour[lastNode + 1] = modN(tour[lastNode] + a3);
                a3Count++;
            }
        }
        lastNode++;
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

    /**
     * Slightly different from Java's modulo method for _ % n, examples:
     * 5 mod 3 = 2 | 3 mod 3 = 3 | -2 mod 3 = 1
     */
    private int modN(int num){
        int out = num % n;
        if(out == 0)
            return n;
        if (out < 0)
            return out + n;
        return out;
    }

    /**
     * Slightly different from Java's modulo method for _ mod p, examples:
     * 5 mod 3 = 2 | 3 mod 3 = 3 | -2 mod 3 = 1
     */
    private int modP(int num){
        int out = (num % prime);
        if(out == 0)
            return prime;
        if (out < 0)
            return out + prime;
        return out;
    }

    public int[][][] createList(boolean print){
        int[][] firstSheet = createFirstSheet();
        int[][][] out = new int[prime][prime][prime];
        out[0] = firstSheet;
        for (int i = 1; i < firstSheet.length; i++) {
            out[i] = createSheet(i);

        }
        if (print)
            print3d(out);
        return out;
    }

    // methods to create visualization of the grids
    private int[][] createSheet(int index){
        return fillSheet(createRow(index));
    }

    private int[][] fillSheet(int[] firstRow) {
        int[][] out = new int[prime][prime];
        out[0] = firstRow;
        for (int i = 1; i < prime; i++) {
            int[] current = new int[prime];
            for (int j = 0; j < firstRow.length; j++) {
                current[j] = modN(firstRow[j] + (a1 * i));
            }

            out[i] = current;
        }

        return out;
    }

    private int[][] createFirstSheet() {
        int[] firstRow = createFirstRow();
        return fillSheet(firstRow);
    }

    private int[] createRow(int index){
        int[] out = new int[prime];
        for (int i = 0; i < out.length; i++) {
            out[i] = modN(startIndex + index * a3 + i * a2);
        }

        return out;
    }
    private int[] createFirstRow(){
        int[] out = new int[prime];
        for (int i = 0; i < out.length; i++) {
            out[i] = startIndex + i * a2;
        }

        return out;
    }

    // printing helper methods
    private static String print1d(int[] array){
        String s = "{";
        for (int j : array) {
            s += " " + j + ",";
        }
        System.out.println(s + "}");
        return s;
    }

    private static void print2d(int[][] array){
        String s = "{";
        System.out.print(s);
        for (int[] ints : array) {
            s += print1d(ints) + "\n";
        }
        System.out.print("}");
    }

    private static void print3d(int[][][] array){
        for (int[][] ints : array) {
            print2d(ints);
            System.out.println("--------------");
        }
    }

    public void printTour(){
        print1d(tour);
    }

    public void printStats(){
        if (GCD(n, a1) == 1){
            System.out.printf("\n-------------------------------------\nstats: a1 jumps: %d, optimal: %d\na2 jumps: %d, optimal: %d\na3 jumps: %d, optimal: %d\ncycle length: %d\nending node: %d%n",
                    a1Count, n, a2Count, 0, a3Count, 0, lastNode, tour[lastNode]);
        }
        else{
            System.out.printf("\n-------------------------------------\nstats: a1 jumps: %d, optimal: %d\na2 jumps: %d, optimal: %d\na3 jumps: %d, optimal: %d\ncycle length: %d\nending node: %d%n",
                    a1Count, prime * prime * (prime -1), a2Count, prime * (prime-1), a3Count, prime, lastNode, tour[lastNode]);
        }
    }

    public static void printSheets(int prime, int limit, int a3L){
        for (int i = 1; i < limit; i++) {
            CubeSolver current = new CubeSolver(prime, a3L, false);
            int[][][] list = current.createList(false);
            int[][] sheet = list[prime-1];
            int S = current.modN(startIndex - current.a3);
            System.out.println("a1: " + prime * prime + ", a2: " + i * prime + ", i: " + i);
            for (int j = 0; j < sheet.length; j++) {
                for (int k = 0; k < sheet[j].length; k++) {
                    if (sheet[j][k] == S)
                        System.out.println("turn around coords: (" + j + ", " + k + ")");
                }
            }
            print2d(sheet);
        }
    }
}