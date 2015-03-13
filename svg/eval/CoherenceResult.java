package svg.eval;

import java.text.DecimalFormat;

/**
 *
 * @author Ivan Guerrero
 */
public class CoherenceResult implements IEvaluationResult {
    private double horizontalBalance, verticalBalance;
    private int numberOfRhythms, numberOfCollisions, numberOfIsolatedElements;
    private DecimalFormat formatter;
    private final double MAX_BALANCE = 10;
    private final int MAX_ISOLATED_ELEMS = 5;
    private final int MAX_RHYTHMS = 10;
    
    public CoherenceResult() {
        formatter = new DecimalFormat("#.##");
    }
    
    @Override
    public double getResult() {
        double horizontalRes = Math.log(horizontalBalance);
        double verticalRes = Math.log(verticalBalance);
        double res = 0;
        res += (Math.abs(horizontalRes) < MAX_BALANCE) ? 30 : 0;
        res += (Math.abs(verticalRes) < MAX_BALANCE) ? 30 : 0;
        res += (numberOfIsolatedElements < MAX_ISOLATED_ELEMS) ? 30 : 0;
        res += (numberOfRhythms < MAX_RHYTHMS) ? 10 : 0;
        return res;
    }

    /**
     * @return the horizontalBalance
     */
    public double getHorizontalBalance() {
        return horizontalBalance;
    }

    /**
     * @param horizontalBalance the horizontalBalance to set
     */
    public void setHorizontalBalance(double horizontalBalance) {
        this.horizontalBalance = horizontalBalance;
    }

    /**
     * @return the verticalBalance
     */
    public double getVerticalBalance() {
        return verticalBalance;
    }

    /**
     * @param verticalBalance the verticalBalance to set
     */
    public void setVerticalBalance(double verticalBalance) {
        this.verticalBalance = verticalBalance;
    }

    /**
     * @return the numberOfRhythms
     */
    public int getNumberOfRhythms() {
        return numberOfRhythms;
    }

    /**
     * @param numberOfRhythms the numberOfRhythms to set
     */
    public void setNumberOfRhythms(int numberOfRhythms) {
        this.numberOfRhythms = numberOfRhythms;
    }

    /**
     * @return the numberOfCollisions
     */
    public int getNumberOfCollisions() {
        return numberOfCollisions;
    }

    /**
     * @param numberOfCollisions the numberOfCollisions to set
     */
    public void setNumberOfCollisions(int numberOfCollisions) {
        this.numberOfCollisions = numberOfCollisions;
    }

    /**
     * @return the numberOfIsolatedElements
     */
    public int getNumberOfIsolatedElements() {
        return numberOfIsolatedElements;
    }

    /**
     * @param numberOfIsolatedElements the numberOfIsolatedElements to set
     */
    public void setNumberOfIsolatedElements(int numberOfIsolatedElements) {
        this.numberOfIsolatedElements = numberOfIsolatedElements;
    }
    
    @Override
    public String toString() {
        return "H-Balance: " +  formatter.format(horizontalBalance) + 
               "\nV-Balance: " + formatter.format(verticalBalance) + 
               "\nRhythms: " + numberOfRhythms + 
               "\nCollisions: " + numberOfCollisions + 
               "\nIsolated elements: " + numberOfIsolatedElements + 
               "\n\nBalance result is 1 if the masses are equivalent. " + 
               "\n\t>1 if the first half (left/upper) contains more mass" +
               "\n\t<1 if the second half (right/lower) contains more mass.";
    }
}