package TSP.CountELV.GridELVCount;

import java.math.BigInteger;
import java.util.ArrayList;

class Box{
    /**powers that this box is responsible to represent*/
    int p1Power;
    int p2Power;

    /** how many ways can this box be reached from the current branch?
     *  Will need to be set by the parent box*/
    BigInteger multiple;

    /**how many ways can this box descent into other boxes, should be relatively constant*/
    BigInteger baseValue;

    /**represents the ways the box can factorize*/
    ArrayList<Box> childBoxes;

    /**the saved value of all (children total + baseValue) * multiple
     * if any of the multiples (including children's) change, computeTotal must be recall*/
    BigInteger total;

    public Box(int p1, int p2, BigInteger mul, BigInteger baseV){
        p1Power = p1;
        p2Power = p2;
        multiple = mul;
        baseValue = baseV;
        childBoxes = new ArrayList<>();
        total = baseValue.multiply(multiple);
    }

    public Box(int p1, int p2, BigInteger mul, BigInteger baseV, BigInteger total){
        p1Power = p1;
        p2Power = p2;
        multiple = mul;
        baseValue = baseV;
        this.total = total;
        childBoxes = new ArrayList<>();
    }

    /**this is the big one that carries this whole system
     * see individual cases for all the code paths
     * basically, given the parent and child powers, set the multiple/baseValue to specific values using the lookUpTable*/
    private void setChildMultiple(Box child){
        if (GridELVCount.print){
            System.out.println("starting decision making");
            System.out.println("parent: " + getName() + ", child: " + child.getName());
        }

        BigInteger multiple = BigInteger.ONE;
        //if the child has both p1 and p2 powers, then its option count is already stored inside the lookup table
        if (child.p1Power > 0 && child.p2Power > 0){
            //sum up the column that allows this to exist
            if (p1Power == child.p1Power){
                if (GridELVCount.print)
                    System.out.println("case 10: has both powers but p1 == child p1");
                multiple = GridELVCount.sumLookUpRow(child.p2Power, child.p1Power, -1);
            }
            else if (p2Power == child.p2Power){
                if (GridELVCount.print)
                    System.out.println("case 11: has both powers but p2 == child p2");
                multiple = GridELVCount.sumLookUpColumn(child.p1Power, child.p2Power, -1);
            }
            else{
                if (GridELVCount.print)
                    System.out.println("case 1: dropped both so direct lookup");
                multiple = GridELVCount.lookUpTable[child.p2Power][child.p1Power];
            }

        }
        else if (child.p1Power == 0 && child.p2Power == 0){
            if (p1Power != 0 && p2Power != 0){
                //if the parent has both powers, only actual relatively prime numbers will work
                if (GridELVCount.print)
                    System.out.println("case 2: is the e box");
                child.baseValue = GridELVCount.lookUpTable[0][0];
            }
            if (p1Power == 0){
                //now, e values AND multiples of p2 without a p1 in them will work
                if (GridELVCount.print)
                    System.out.println("case 8: parent can accept p1 and e to terminate");
                child.baseValue = GridELVCount.sumLookUpRow(0, 0, -1);
            }
            if (p2Power == 0){
                if (GridELVCount.print)
                    System.out.println("case 9: parent can accept p2 and e to terminate");
                //since no p2 in parent, e values and multiples of p1 without a p2 are valid
                child.baseValue = GridELVCount.sumLookUpColumn(0, 0, -1);
            }

        }
        /* after this point, there must be at least some summing shenanigans to get the proper multiple*/

        //if we are dropping a p2 factor
        else if (child.p2Power < p2Power){

            if (child.p1Power == p1Power){
                if (GridELVCount.print)
                    System.out.println("case 4: dropped only p2");
                //if we are only dropping a p2, then any power >= p1Power will still gcd down by the parent
                //example: parent = 2^3 * 3^3, child = 2^3 * 3^2. That 2^3 can come from 2^3, 2^4, 2^5...
                multiple = GridELVCount.sumLookUpRow(child.p2Power, child.p1Power, -1);
            }

            else if (child.p1Power < p1Power && p2Power > 0) {
                if (GridELVCount.print)
                    System.out.println("case 5: dropped both and still have parent p2");
                //if we are dropping both powers AND the parent has both factors, then both p1 and p2 are fixed
                //and we need a direct look-up to access the one value
                multiple = GridELVCount.lookUpTable[child.p2Power][child.p1Power];
            }
            else if (child.p1Power < p1Power) {
                if (GridELVCount.print)
                    System.out.println("case 6: dropped both and dont have p2");
                //if there are no remaining p2 powers in the parent, the child can choose any of them and not affect gcd
                //example: parent = 2^2, child = 2. That 2 can come from 2*3, 2*3^2, 2*3^3...
                multiple = GridELVCount.sumLookUpColumn(child.p1Power, child.p1Power, -1);
            }
        }
        //dropping a p1 factor now
        else if (child.p1Power < p1Power){
            if (child.p2Power == p2Power){
                if (GridELVCount.print)
                    System.out.println("case 7: dropped only p1");
                //if we are only dropping a p1, then any power >= p2Power will still gcd down by the parent
                //example: parent = 2^3 * 3^3, child = 2^2 * 3^3. That 3^3 can come from 3^3, 3^4, 3^5...
                multiple = GridELVCount.sumLookUpColumn(child.p1Power, child.p2Power, -1);
            }
            /* the drop both cases should *technically* be handled by the one in the drop p2 factor */
//
        }
        if (GridELVCount.print)
            System.out.println("setting multiple to " + multiple);
        child.multiple = multiple;
        child.computeTotal();
        if (GridELVCount.print)
            System.out.println("-------------");
    }

    public void createChildren(){
        //if you are the e box, end of recursion
        if (p1Power == 0 && p2Power == 0){
            baseValue = GridELVCount.lookUpTable[0][0];
            multiple = BigInteger.ONE;
            return;
        }
        //for each combination of powers under yourself, create a box, create that child's underlings, then add it to your list
        for (int i = 0; i <= p1Power; i++) {
            for (int j = 0; j <= p2Power; j++) {

                if (i == p1Power && j == p2Power)
                    continue;
                Box lookUp = GridELVCount.boxTable[i][j];
                if (lookUp != null){
                    addChild(lookUp);
                }
                else{
                    Box child = new Box(i, j, GridELVCount.lookUpTable[i][j], BigInteger.ZERO);

                    child.createChildren();
                    child.setAllChildrenMultiples();
                    addChildNoCopy(child);
                    GridELVCount.boxTable[i][j] = child;
                }

            }
        }
        setAllChildrenMultiples();
    }


    public BigInteger getTotal(){
        return total;
    }

    /** set the multiples and baseValues of each child box
     * if each child does not get its multiple set at some point, the result will be wrong*/
    private void setAllChildrenMultiples(){
        for (Box child: childBoxes) {
            setChildMultiple(child);
        }
    }
    /**recomputes the total for this specific box
     * does NOT recompute all children, just itself*/
    public BigInteger computeTotal(){
        if (GridELVCount.print)
            System.out.printf("computing total for p1^%d * p2^%d, baseValue: %d, multiple: %d\n", p1Power, p2Power, baseValue, multiple);
        BigInteger value = baseValue;
        for (Box child: childBoxes) {
            if(GridELVCount.print){
                System.out.println("child " + child.getName() + " total: " + child.getTotal());
            }

            value = value.add(child.getTotal());
        }
        total = value.multiply(multiple);
        if (GridELVCount.print)
            System.out.println("total: " + total + " = " + value + " * " + multiple);
        return total;
    }
    public void addChild(Box child){
        Box copy = makeCopy(child);
        setChildMultiple(copy);
        childBoxes.add(copy);
    }
    public void addChildNoCopy(Box child){
        setChildMultiple(child);
        childBoxes.add(child);
    }
    private Box makeCopy(Box src){
        Box out = new Box(src.p1Power, src.p2Power, src.multiple, src.baseValue, src.total);
        out.childBoxes.addAll(src.childBoxes);
        return out;
    }

    @Override
    public String toString() {
        return "Box{" +
                "p1Power=" + p1Power +
                ", p2Power=" + p2Power +
                ", multiple=" + multiple +
                ", baseValue=" + baseValue +
                ", total=" + total +
                ", childBoxes=" + childBoxes +
                '}';
    }
    public String getName(){
        return "p1^" + p1Power + " * p2^" + p2Power;
    }
    public String listChildren(){
        StringBuilder s = new StringBuilder();
        for (Box child: childBoxes) {
            s.append(child.getName());
        }
        return s.toString();
    }
}
